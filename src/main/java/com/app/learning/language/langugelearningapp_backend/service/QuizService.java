package com.app.learning.language.langugelearningapp_backend.service;

import com.app.learning.language.langugelearningapp_backend.model.Quiz;
import com.app.learning.language.langugelearningapp_backend.request.ListQuizSubmitRequest;
import com.app.learning.language.langugelearningapp_backend.request.QuizPostRequest;
import com.app.learning.language.langugelearningapp_backend.response.QuizResponse;
import com.app.learning.language.langugelearningapp_backend.security.model.ApplicationUser;

import java.util.List;

public interface QuizService {
    void createQuiz(QuizPostRequest req, ApplicationUser appUser);
    List<QuizResponse> getAllQuizzes();

    void submitQuiz(ListQuizSubmitRequest req);

    List<QuizResponse> fetchQuizzesByLanguageCode(String languageCode, Integer numOfQuestions);
}
