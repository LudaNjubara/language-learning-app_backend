package com.app.learning.language.langugelearningapp_backend.security.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private final Long id;
    private final String username;
    private final String jwt;
}