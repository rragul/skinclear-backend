package com.skinclear.skinclearbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinclear.skinclearbackend.dto.RegisterDTO;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckEligibility_Success() throws Exception {
        // Arrange
        String idToken = "testIdToken";
        doNothing().when(userService).checkEligibility(idToken);

        // Act
        ResponseEntity<GeneralResponse> response = authController.checkEligibility(idToken);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        verify(userService, times(1)).checkEligibility(idToken);
    }

    @Test
    void testCheckEligibility_Failure() throws Exception {
        // Arrange
        String idToken = "testIdToken";
        doThrow(new RuntimeException("Eligibility check failed")).when(userService).checkEligibility(idToken);

        // Act
        ResponseEntity<GeneralResponse> response = authController.checkEligibility(idToken);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals(false, response.getBody().isSuccess());
        assertEquals("Eligibility check failed", response.getBody().getError().getMessage());
        verify(userService, times(1)).checkEligibility(idToken);
    }

    @Test
    void testRegister_Success() throws Exception {
        // Arrange
        String userJson = "{\"username\": \"john_doe\"}";
        RegisterDTO registerDTO = new RegisterDTO();
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[0]);

        when(objectMapper.readValue(userJson, RegisterDTO.class)).thenReturn(registerDTO);
        doNothing().when(userService).register(registerDTO, image);

        // Act
        ResponseEntity<GeneralResponse> response = authController.register(image, userJson);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        verify(userService, times(1)).register(registerDTO, image);
    }

    @Test
    void testRegister_Failure() throws Exception {
        // Arrange
        String userJson = "{\"username\": \"john_doe\"}";
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[0]);

        when(objectMapper.readValue(userJson, RegisterDTO.class)).thenThrow(new RuntimeException("Invalid data"));

        // Act
        ResponseEntity<GeneralResponse> response = authController.register(image, userJson);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals(false, response.getBody().isSuccess());
        assertEquals("Invalid data", response.getBody().getError().getMessage());
        verify(userService, never()).register(any(), any());
    }
}
