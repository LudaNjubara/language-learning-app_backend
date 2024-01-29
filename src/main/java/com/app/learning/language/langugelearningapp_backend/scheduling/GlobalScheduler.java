package com.app.learning.language.langugelearningapp_backend.scheduling;

import com.app.learning.language.langugelearningapp_backend.model.Quiz;
import com.app.learning.language.langugelearningapp_backend.repository.QuizRepository;
import com.app.learning.language.langugelearningapp_backend.response.QuizResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GlobalScheduler {
    private final QuizRepository quizRepository;
    @Scheduled(fixedRate = 30000) // 30 seconds
    public void showAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();

        List<QuizResponse> quizResponses = quizzes.stream().map(quiz -> {
            QuizResponse quizResponse = new QuizResponse();

            quizResponse.setId(quiz.getId());
            quizResponse.setQuestion(quiz.getQuestion());
            quizResponse.setLanguage(quiz.getLanguage());

            quizResponse.setCreatedByUsername(quiz.getCreatedBy().getUsername());

            return quizResponse;
        }).toList();

        System.out.println("Quizzes: ");
        for (QuizResponse quizRes : quizResponses) {
            System.out.println(quizRes);
        }

        System.out.println();
    }
}
