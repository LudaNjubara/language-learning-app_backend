package com.app.learning.language.langugelearningapp_backend.response;

import com.app.learning.language.langugelearningapp_backend.model.QuizQuestion;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import lombok.Data;

@Data
public class QuizQuestionResponse {
    private Long id;
    private String text;
    private SupportedLanguage language;

    public QuizQuestionResponse(QuizQuestion quizQuestion) {
        this.id = quizQuestion.getId();
        this.text = quizQuestion.getText();
        this.language = quizQuestion.getLanguage();
    }
}
