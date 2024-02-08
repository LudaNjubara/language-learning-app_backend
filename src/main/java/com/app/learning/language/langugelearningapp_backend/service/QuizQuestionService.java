package com.app.learning.language.langugelearningapp_backend.service;

import com.app.learning.language.langugelearningapp_backend.model.QuizQuestion;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.app.learning.language.langugelearningapp_backend.request.QuizQuestionPostRequestBody;

import java.util.List;

public interface QuizQuestionService {

    void createQuestion(QuizQuestionPostRequestBody req);
    List<QuizQuestion> getRandomQuizQuestionsByLanguageAndNumOfQuestions(SupportedLanguage language, Integer numOfQuestions);
}
