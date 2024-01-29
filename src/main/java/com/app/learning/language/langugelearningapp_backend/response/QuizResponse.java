package com.app.learning.language.langugelearningapp_backend.response;

import com.app.learning.language.langugelearningapp_backend.model.QuizAnswer;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import lombok.Data;

import java.util.List;

@Data
public class QuizResponse {
    private Long id;
    private String question;
    private List<QuizAnswer> answers;
    private String userAnswer;
    private String createdByUsername;
    private SupportedLanguage language;
}
