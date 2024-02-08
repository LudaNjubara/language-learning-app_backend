package com.app.learning.language.langugelearningapp_backend.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuizSubmitRequest {
    @NotNull
    private Long quizId;

    @NotEmpty
    private List<QuizQuestionSubmitRequest> quizQuestionSubmitRequests;
}
