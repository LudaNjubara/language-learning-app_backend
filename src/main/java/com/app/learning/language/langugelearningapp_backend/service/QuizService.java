package com.app.learning.language.langugelearningapp_backend.service;

import com.app.learning.language.langugelearningapp_backend.model.Quiz;
import com.app.learning.language.langugelearningapp_backend.request.ListQuizSubmitRequest;
import com.app.learning.language.langugelearningapp_backend.request.QuizPostRequest;
import com.app.learning.language.langugelearningapp_backend.request.QuizSubmitRequest;
import com.app.learning.language.langugelearningapp_backend.response.QuizResponse;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;

import java.util.List;

public interface QuizService {
    List<QuizResponse> getAllQuizzes();

    void submitQuiz(QuizSubmitRequest req);

    QuizResponse fetchQuiz(String languageCode, Integer numOfQuestions);

    void deleteQuiz(Long id);
}
