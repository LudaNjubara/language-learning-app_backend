package com.app.learning.language.langugelearningapp_backend.security.controller;

import com.app.learning.language.langugelearningapp_backend.security.dto.LoginDTO;
import com.app.learning.language.langugelearningapp_backend.security.dto.RegisterDTO;
import com.app.learning.language.langugelearningapp_backend.security.request.LoginRequest;
import com.app.learning.language.langugelearningapp_backend.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public LoginDTO login(@Valid @RequestBody final LoginRequest req) throws Exception {
        return authService.login(req)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials"));
    }

    @PostMapping("/register")
    public RegisterDTO register(@Valid @RequestBody final LoginRequest req) {
        return authService.register(req)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials"));
    }

    @PostMapping("/authenticate")
    public boolean authenticate(@RequestHeader("Authorization") final String token) {
        return authService.authenticate(token);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") final String token) {
        authService.logout(token);
    }

}