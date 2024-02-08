package com.app.learning.language.langugelearningapp_backend.scheduling;

import com.app.learning.language.langugelearningapp_backend.model.Quiz;
import com.app.learning.language.langugelearningapp_backend.model.QuizQuestion;
import com.app.learning.language.langugelearningapp_backend.repository.QuizRepository;
import com.app.learning.language.langugelearningapp_backend.response.QuizQuestionResponse;
import com.app.learning.language.langugelearningapp_backend.response.QuizResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GlobalScheduler {
    private final QuizRepository quizRepository;
    @Scheduled(fixedRate = 30000) // 30 seconds
    public void showAllQuizzes() {

    }
}
