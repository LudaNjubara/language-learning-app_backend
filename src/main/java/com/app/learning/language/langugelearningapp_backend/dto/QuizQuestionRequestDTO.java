package com.app.learning.language.langugelearningapp_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionRequestDTO {
    private String text;
    private List<String> answers;
    private String languageCode;
}
