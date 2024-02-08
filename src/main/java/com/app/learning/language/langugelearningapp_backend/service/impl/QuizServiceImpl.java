package com.app.learning.language.langugelearningapp_backend.service.impl;

import com.app.learning.language.langugelearningapp_backend.configuration.AuditorConfig;
import com.app.learning.language.langugelearningapp_backend.model.*;
import com.app.learning.language.langugelearningapp_backend.repository.QuizRepository;
import com.app.learning.language.langugelearningapp_backend.repository.QuizUserAnswerRepository;
import com.app.learning.language.langugelearningapp_backend.repository.SupportedLanguagesRepository;
import com.app.learning.language.langugelearningapp_backend.request.ListQuizSubmitRequest;
import com.app.learning.language.langugelearningapp_backend.request.QuizPostRequest;
import com.app.learning.language.langugelearningapp_backend.request.QuizSubmitRequest;
import com.app.learning.language.langugelearningapp_backend.response.QuizQuestionResponse;
import com.app.learning.language.langugelearningapp_backend.response.QuizResponse;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.app.learning.language.langugelearningapp_backend.security.repository.UserRepository;
import com.app.learning.language.langugelearningapp_backend.service.QuizQuestionService;
import com.app.learning.language.langugelearningapp_backend.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizQuestionService questionService;

    private final QuizRepository quizRepository;

    private final UserRepository userRepository;
    private final SupportedLanguagesRepository supportedLanguagesRepository;
    private final QuizUserAnswerRepository quizUserAnswerRepository;

    private final AuditorConfig auditorConfig;

    @Override
    public List<QuizResponse> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        List<QuizResponse> quizResponses = new ArrayList<>();

        for (Quiz quiz : quizzes) {
            QuizResponse quizResponse = new QuizResponse();
            quizResponse.setId(quiz.getId());

            List<QuizQuestionResponse> quizQuestionResponses = new ArrayList<>();
            for (QuizQuestion question : quiz.getQuestions()) {
                quizQuestionResponses.add(new QuizQuestionResponse(question));
            }

            quizResponse.setQuestions(quizQuestionResponses);
            quizResponse.setLanguage(quiz.getLanguage());
            quizResponse.setTakenAt(quiz.getTakenAt());

            quizResponses.add(quizResponse);
        }

        return quizResponses;
    }

    @Override
    @Transactional
    public void submitQuiz(QuizSubmitRequest req) {
        Quiz quiz = quizRepository.findById(req.getQuizId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id " + req.getQuizId() + " not found!")
        );

        // TODO: errora
        JwtUser appUser = auditorConfig.getCurrentAuditor().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );

        quiz.setTakenBy(appUser);

        List<QuizUserAnswer> userAnswers = new ArrayList<>();

        req.getQuizQuestionSubmitRequests().forEach(quizQuestionSubmitRequest -> {
            QuizQuestion question = quiz.getQuestions().stream().filter(q -> q.getId().equals(quizQuestionSubmitRequest.getQuestionId())).findFirst().orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question with id " + quizQuestionSubmitRequest.getQuestionId() + " not found!")
            );

            QuizUserAnswer userAnswer = new QuizUserAnswer();
            userAnswer.setUserAnswer(quizQuestionSubmitRequest.getAnswer());
            userAnswer.setQuestion(question);
            userAnswer.setQuiz(quiz);
            userAnswer.setUser(appUser);

            userAnswers.add(userAnswer);
        });

        quiz.setUserAnswers(userAnswers);

        quizRepository.save(quiz);
    }

    @Override
    @Transactional
    public QuizResponse fetchQuiz(String languageCode, Integer numOfQuestions) {

        SupportedLanguage language = supportedLanguagesRepository.findByLanguageCode(languageCode).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Language with code " + languageCode + " not found!")
        );

        List<QuizQuestion> questions = questionService.getRandomQuizQuestionsByLanguageAndNumOfQuestions(language, numOfQuestions);

        Quiz quiz = new Quiz();
        quiz.setLanguage(language);
        quiz.setQuestions(questions);
        quiz.setTakenAt(LocalDateTime.now());

        return new QuizResponse(quizRepository.save(quiz));
    }

    @Override
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}
