package com.app.learning.language.langugelearningapp_backend.request;

import com.app.learning.language.langugelearningapp_backend.model.QuizAnswer;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class QuizPostRequest {
    private String question;
    private List<String> answers;
    private String languageCode;
}
