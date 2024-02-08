package com.app.learning.language.langugelearningapp_backend.request;

import lombok.Data;

@Data
public class QuizQuestionSubmitRequest {
    private Long questionId;
    private String answer;
}
