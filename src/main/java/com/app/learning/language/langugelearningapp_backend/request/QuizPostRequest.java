package com.app.learning.language.langugelearningapp_backend.request;

import lombok.Data;

import java.util.List;

@Data
public class QuizPostRequest {
    private String question;
    private List<String> answers;
    private String languageCode;
}
