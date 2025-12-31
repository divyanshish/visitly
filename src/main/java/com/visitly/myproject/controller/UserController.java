package com.visitly.myproject.controller;

import com.visitly.myproject.dto.AuthResponse;
import com.visitly.myproject.dto.LoginRequest;
import com.visitly.myproject.dto.RegisterRequest;
import com.visitly.myproject.dto.UserResponse;
import com.visitly.myproject.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        log.info("User registration request for email: {}", request.getEmail());

        try {
            UserResponse response = authService.register(request);
            log.info("User registered successfully with id: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        log.info("User login request for email: {}", request.getEmail());

        try {
            AuthResponse response = authService.login(request);
            log.info("User logged in successfully: {}", response.getUser().getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
