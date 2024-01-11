package com.app.learning.language.langugelearningapp_backend.service;

import com.app.learning.language.langugelearningapp_backend.model.Quiz;
import com.app.learning.language.langugelearningapp_backend.request.QuizPostRequest;
import com.app.learning.language.langugelearningapp_backend.security.model.ApplicationUser;

import java.util.List;

public interface QuizService {
    void createQuiz(QuizPostRequest req, ApplicationUser appUser);
    List<Quiz> getAllQuizzes();

    List<Quiz> fetchQuizzesByLanguageCode(String languageCode, Integer numOfQuestions);
}
