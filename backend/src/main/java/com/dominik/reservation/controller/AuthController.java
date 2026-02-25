package com.dominik.reservation.controller;

import com.dominik.reservation.dto.RegisterRequest;
import com.dominik.reservation.dto.UserResponse;
import com.dominik.reservation.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/me")
    public Map<String, String> me(Authentication auth) {
        return Map.of("email", auth.getName());
    }
}
