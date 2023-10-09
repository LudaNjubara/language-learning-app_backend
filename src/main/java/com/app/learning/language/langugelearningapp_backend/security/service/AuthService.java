package com.app.learning.language.langugelearningapp_backend.security.service;

import com.app.learning.language.langugelearningapp_backend.security.dto.LoginDTO;
import com.app.learning.language.langugelearningapp_backend.security.request.LoginRequest;

import java.util.Optional;

public interface AuthService {

    Optional<LoginDTO> login(LoginRequest req);

    Optional<LoginDTO> register(LoginRequest req);

    boolean authenticate(String token);

    void logout(String token);

}