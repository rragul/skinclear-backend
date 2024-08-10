package com.skinclear.skinclearbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinclear.skinclearbackend.dto.RegisterDTO;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.resource.UserResource;
import com.skinclear.skinclearbackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserController userController;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUser_Success() throws Exception {
        // Arrange
        UserResource userResource = new UserResource();
        when(userService.getUser(principal)).thenReturn(userResource);

        // Act
        ResponseEntity<GeneralResponse> response = userController.getUser(principal);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(userResource, response.getBody().getData());
        verify(userService, times(1)).getUser(principal);
    }

    @Test
    void testGetUser_Failure() throws Exception {
        // Arrange
        when(userService.getUser(principal)).thenThrow(new RuntimeException("User not found"));

        // Act
        ResponseEntity<GeneralResponse> response = userController.getUser(principal);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals(false, response.getBody().isSuccess());
        assertEquals("User not found", response.getBody().getError().getMessage());
        verify(userService, times(1)).getUser(principal);
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        // Arrange
        String userJson = "{\"username\": \"john_doe\"}";
        RegisterDTO registerDTO = new RegisterDTO();
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[0]);

        when(objectMapper.readValue(userJson, RegisterDTO.class)).thenReturn(registerDTO);

        // Act
        ResponseEntity<GeneralResponse> response = userController.updateUser(principal, image, userJson);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        verify(userService, times(1)).updateUser(principal, registerDTO, image);
    }

    @Test
    void testUpdateUser_Failure() throws Exception {
        // Arrange
        String userJson = "{\"username\": \"john_doe\"}";
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[0]);

        when(objectMapper.readValue(userJson, RegisterDTO.class)).thenThrow(new RuntimeException("Invalid data"));

        // Act
        ResponseEntity<GeneralResponse> response = userController.updateUser(principal, image, userJson);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals(false, response.getBody().isSuccess());
        assertEquals("Invalid data", response.getBody().getError().getMessage());
        verify(userService, never()).updateUser(principal, null, image);
    }
}
