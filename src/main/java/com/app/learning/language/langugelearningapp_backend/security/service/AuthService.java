package com.app.learning.language.langugelearningapp_backend.security.service;

import com.app.learning.language.langugelearningapp_backend.security.dto.LoginDTO;
import com.app.learning.language.langugelearningapp_backend.security.dto.RegisterDTO;
import com.app.learning.language.langugelearningapp_backend.security.request.LoginRequest;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

public interface AuthService {

    Optional<LoginDTO> login(LoginRequest req) throws Exception;

    Optional<RegisterDTO> register(LoginRequest req);

    boolean authenticate(String token);

    void logout(String token);

}