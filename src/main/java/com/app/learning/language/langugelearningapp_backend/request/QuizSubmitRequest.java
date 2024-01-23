package com.app.learning.language.langugelearningapp_backend.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class QuizSubmitRequest {
    @NotNull
    private Long quizId;

    @NotBlank
    private String answer;
}
