package com.app.learning.language.langugelearningapp_backend.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class QuizSubmitRequest {
    @NotNull
    private Long quizId;

    @NotEmpty
    private List<QuizQuestionSubmitRequest> quizQuestionSubmitRequests;
}
