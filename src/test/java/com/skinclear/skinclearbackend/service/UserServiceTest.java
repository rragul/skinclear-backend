package com.skinclear.skinclearbackend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.skinclear.skinclearbackend.dto.RegisterDTO;
import com.skinclear.skinclearbackend.entity.User;
import com.skinclear.skinclearbackend.repository.UserRepository;
import com.skinclear.skinclearbackend.resource.UserResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private S3Service mockS3Service;
    @Mock
    private FirebaseAuth mockFirebaseAuth;
    @Mock
    private FirebaseToken mockFirebaseToken;

    private UserService userServiceUnderTest;

    @BeforeEach
    void setUp() {
        userServiceUnderTest = new UserService(mockUserRepository, mockS3Service);
    }

//    @Test
    void testCheckEligibility() throws Exception {
        // Setup
        FirebaseAuth mockFirebaseAuth = mock(FirebaseAuth.class);
        FirebaseToken mockFirebaseToken = mock(FirebaseToken.class);

        when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth); // Mock static getInstance method
        when(mockFirebaseAuth.verifyIdToken(anyString())).thenReturn(mockFirebaseToken);
        when(mockFirebaseToken.getEmail()).thenReturn("email");

        final User user = new User();
        user.setEmail("email");
        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.of(user));

        // Run the test
        userServiceUnderTest.checkEligibility("idToken");

        // Verify the results
        verify(mockFirebaseAuth).verifyIdToken(anyString());
        verify(mockUserRepository).findByEmail("email");
    }

    @Test
    void testCheckEligibility_UserRepositoryReturnsAbsent() throws FirebaseAuthException {
        // Setup Firebase mocks
        FirebaseToken mockFirebaseToken = mock(FirebaseToken.class);
        when(mockFirebaseToken.getEmail()).thenReturn("email");

        FirebaseAuth mockFirebaseAuth = mock(FirebaseAuth.class);
        when(mockFirebaseAuth.verifyIdToken(anyString())).thenReturn(mockFirebaseToken);

        // Mock static methods if needed (consider using PowerMockito or similar)
        try (MockedStatic<FirebaseAuth> mockedAuth = Mockito.mockStatic(FirebaseAuth.class)) {
            mockedAuth.when(FirebaseAuth::getInstance).thenReturn(mockFirebaseAuth);

            // Setup UserRepository mock
            when(mockUserRepository.findByEmail("email")).thenReturn(Optional.empty());

            // Run the test
            assertThatThrownBy(() -> userServiceUnderTest.checkEligibility("idToken"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("User not found");
        }
    }


    @Test
    void testRegister_ThrowsRuntimeException() {
        // Setup
        final RegisterDTO user = new RegisterDTO();
        user.setEmail("email");

        final MultipartFile image = new MockMultipartFile("name", "content".getBytes());

        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.of(new User()));

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.register(user, image))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User already exists");
    }

    @Test
    void testRegister_UserRepositoryFindByEmailReturnsAbsent() {
        // Setup
        final RegisterDTO user = new RegisterDTO();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setUserName("userName");
        user.setEmail("email");
        user.setBio("bio");
        user.setLocation("location");
        user.setSkinType("skinType");
        user.setSkinConcerns("skinConcerns");
        user.setProductPreference("productPreference");

        final MultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "content".getBytes());

        // Mock UserRepository to return empty
        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.empty());

        // Mock S3Service to return a dummy URL
        when(mockS3Service.uploadFile(any(MultipartFile.class), any(String.class), eq("user")))
                .thenReturn("profilePictureUrl");

        // Run the test
        userServiceUnderTest.register(user, image);

        // Verify that the S3Service was called with the correct arguments
        verify(mockS3Service).uploadFile(eq(image), any(String.class), eq("user"));

        // Verify the UserRepository save method was called
        verify(mockUserRepository).save(any(User.class));

        // Optional: Verify that the User object was saved with the correct profile picture URL
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(mockUserRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("profilePictureUrl", savedUser.getProfilePicture());
    }



    @Test
    void testGetUser() {
        // Setup
        final Principal principal = new TestingAuthenticationToken("email", "pass", "ROLE_USER");

        final User user = new User();
        user.setEmail("email");
        user.setUserName("userName");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.of(user));

        // Run the test
        final UserResource result = userServiceUnderTest.getUser(principal);

        // Verify the results
        assertThat(result.getEmail()).isEqualTo("email");
        assertThat(result.getUserName()).isEqualTo("userName");
        assertThat(result.getFirstName()).isEqualTo("firstName");
        assertThat(result.getLastName()).isEqualTo("lastName");
    }

    @Test
    void testGetUser_UserRepositoryReturnsAbsent() {
        // Setup
        final Principal principal = new TestingAuthenticationToken("email", "pass", "ROLE_USER");
        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.getUser(principal))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void testUpdateUser() {
        // Setup
        final Principal principal = new TestingAuthenticationToken("email", "pass", "ROLE_USER");
        final RegisterDTO user = new RegisterDTO();
        user.setUserName("userName");

        final MultipartFile image = new MockMultipartFile("name", "filename.png", "image/png", "content".getBytes());

        final User existingUser = new User();
        existingUser.setEmail("email");
        existingUser.setUserName("userName");
        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.of(existingUser));

        when(mockS3Service.uploadFile(any(MultipartFile.class), eq("userName.png"), eq("user")))
                .thenReturn("profilePictureUrl");

        // Run the test
        userServiceUnderTest.updateUser(principal, user, image);

        // Verify the results
        verify(mockUserRepository).save(any(User.class));
        verify(mockS3Service).uploadFile(eq(image), eq("userName.png"), eq("user"));
    }

    @Test
    void testUpdateUser_UserRepositoryFindByEmailReturnsAbsent() {
        // Setup
        final Principal principal = new TestingAuthenticationToken("email", "pass", "ROLE_USER");
        final RegisterDTO user = new RegisterDTO();
        user.setEmail("email");

        final MultipartFile image = new MockMultipartFile("name", "content".getBytes());
        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.updateUser(principal, user, image))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }
}
