package com.skinclear.skinclearbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinclear.skinclearbackend.dto.RegisterDTO;
import com.skinclear.skinclearbackend.resource.Error;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public AuthController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/eligibility/{idToken}")
    public ResponseEntity<GeneralResponse> checkEligibility(@PathVariable String idToken) {
        try {
            logger.info("request - checkEligibility | (URL: /api/v1/auth/eligibility/{idToken}) | (Method: GET) ");
            userService.checkEligibility(idToken);
            logger.info("response - checkEligibility | (URL: /api/v1/auth/eligibility/{idToken}) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - checkEligibility | (URL: /api/v1/auth/eligibility/{idToken}) | (Method: GET) | (status: 500)");
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

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> register(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestPart("user") String userJson) {
        try {
            logger.info("request - register | (URL: /api/v1/auth/register) | (Method: POST) ");
            RegisterDTO user = objectMapper.readValue(userJson, RegisterDTO.class);
            userService.register(user, image);
            logger.info("response - register | (URL: /api/v1/auth/register) | (Method: POST) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .message("User registered successfully")
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - register | (URL: /api/v1/auth/register) | (Method: POST) | (status: 500)");
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
