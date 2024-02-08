package com.app.learning.language.langugelearningapp_backend.security.dto;

import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import lombok.Data;

import java.util.List;

@Data
public class RegisterDTO {
        private final Long id;
        private final String username;
        private final List<String> authorities;
        private final SupportedLanguage selectedLanguage;
        private final String jwt;
}
