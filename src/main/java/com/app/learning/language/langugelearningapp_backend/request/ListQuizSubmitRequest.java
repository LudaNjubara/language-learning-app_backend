package com.app.learning.language.langugelearningapp_backend.request;

import lombok.Data;

import jakarta.validation.Valid;
import java.util.List;

@Data
public class ListQuizSubmitRequest {
    @Valid
    private List<QuizSubmitRequest> quizSubmitRequests;
}
