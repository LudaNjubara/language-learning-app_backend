package com.app.learning.language.langugelearningapp_backend.dto;

import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private SupportedLanguage selectedLanguage;
}
