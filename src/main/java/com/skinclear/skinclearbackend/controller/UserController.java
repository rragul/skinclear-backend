package com.skinclear.skinclearbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinclear.skinclearbackend.dto.RegisterDTO;
import com.skinclear.skinclearbackend.resource.Error;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.resource.UserResource;
import com.skinclear.skinclearbackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getUser(Principal principal) {
        try {
            logger.info("request - getUser | (URL: /api/v1/user) | (Method: GET) ");
            UserResource userResource = userService.getUser(principal);
            logger.info("response - getUser | (URL: /api/v1/user) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(userResource)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - getUser | (URL: /api/v1/user) | (Method: GET) | (status: 500)");
            return ResponseEntity.status(500).body(
                    GeneralResponse.builder()
                            .success(false)
                            .error(Error.builder()
                                    .message(e.getMessage())
                                    .build()
                            )
                            .build()
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<GeneralResponse> updateUser(
            Principal principal,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestPart("user") String userJson) {
        try {
            logger.info("request - updateUser | (URL: /api/v1/user/update) | (Method: PUT) ");
            RegisterDTO user = objectMapper.readValue(userJson, RegisterDTO.class);
            userService.updateUser(principal, user, image);
            logger.info("response - updateUser | (URL: /api/v1/user/update) | (Method: PUT) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .message("User updated successfully")
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - updateUser | (URL: /api/v1/user/update) | (Method: PUT) | (status: 500)");
            return ResponseEntity.status(500).body(
                    GeneralResponse.builder()
                            .success(false)
                            .error(Error.builder()
                                    .message(e.getMessage())
                                    .build()
                            )
                            .build()
            );
        }
    }
}
