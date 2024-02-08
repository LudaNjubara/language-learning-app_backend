package com.app.learning.language.langugelearningapp_backend.request;

import com.app.learning.language.langugelearningapp_backend.dto.QuizQuestionRequestDTO;
import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionPostRequestBody {
    private List<QuizQuestionRequestDTO> quizQuestionRequestDTOList;
}
