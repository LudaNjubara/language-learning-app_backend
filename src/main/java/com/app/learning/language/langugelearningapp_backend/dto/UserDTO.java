package com.app.learning.language.langugelearningapp_backend.dto;

import com.app.learning.language.langugelearningapp_backend.model.Quiz;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.app.learning.language.langugelearningapp_backend.response.QuizResponse;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String username;
    private SupportedLanguage selectedLanguage;
    private List<QuizResponse> takenQuizzes;
}
