package com.app.learning.language.langugelearningapp_backend.response;

import com.app.learning.language.langugelearningapp_backend.model.Quiz;
import com.app.learning.language.langugelearningapp_backend.model.QuizAnswer;
import com.app.learning.language.langugelearningapp_backend.model.QuizQuestion;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class QuizResponse {
    private Long id;
    private List<QuizQuestionResponse> questions;
    private LocalDateTime takenAt;
    private SupportedLanguage language;

    public QuizResponse(Quiz quiz) {
        this.id = quiz.getId();

        List<QuizQuestionResponse> quizQuestionResponses = new ArrayList<>();
        for (QuizQuestion question : quiz.getQuestions()) {
            quizQuestionResponses.add(new QuizQuestionResponse(question));
        }

        this.questions = quizQuestionResponses;
        this.takenAt = quiz.getTakenAt();
        this.language = quiz.getLanguage();
    }
}
