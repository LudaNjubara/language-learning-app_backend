package com.app.learning.language.langugelearningapp_backend.service.impl;

import com.app.learning.language.langugelearningapp_backend.configuration.AuditorConfig;
import com.app.learning.language.langugelearningapp_backend.model.Quiz;
import com.app.learning.language.langugelearningapp_backend.model.QuizAnswer;
import com.app.learning.language.langugelearningapp_backend.model.QuizUserAnswer;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.app.learning.language.langugelearningapp_backend.repository.QuizRepository;
import com.app.learning.language.langugelearningapp_backend.repository.QuizUserAnswerRepository;
import com.app.learning.language.langugelearningapp_backend.repository.SupportedLanguagesRepository;
import com.app.learning.language.langugelearningapp_backend.request.ListQuizSubmitRequest;
import com.app.learning.language.langugelearningapp_backend.request.QuizPostRequest;
import com.app.learning.language.langugelearningapp_backend.request.QuizSubmitRequest;
import com.app.learning.language.langugelearningapp_backend.response.QuizResponse;
import com.app.learning.language.langugelearningapp_backend.security.model.ApplicationUser;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.app.learning.language.langugelearningapp_backend.security.repository.UserRepository;
import com.app.learning.language.langugelearningapp_backend.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    private final UserRepository userRepository;
    private final SupportedLanguagesRepository supportedLanguagesRepository;
    private final QuizUserAnswerRepository quizUserAnswerRepository;

    private final AuditorConfig auditorConfig;

    @Override
    public void createQuiz(QuizPostRequest req, ApplicationUser appUser) {
        try {
            Optional<JwtUser> user = userRepository.findByUsername(appUser.getUsername());
            Optional<SupportedLanguage> language = supportedLanguagesRepository.findByLanguageCode(req.getLanguageCode());


            if (user.isEmpty()) {
                throw new Exception("User with username " + appUser.getUsername() + " not found!");
            }

            if (language.isEmpty()) {
                throw new Exception("Language with code " + req.getLanguageCode() + " not found!");
            }

            Quiz quiz = new Quiz();
            quiz.setCreatedBy(user.get());
            quiz.setLanguage(language.get());
            quiz.setQuestion(req.getQuestion());
            for (String answer : req.getAnswers()) {
                QuizAnswer quizAnswer = new QuizAnswer();
                quizAnswer.setAnswer(answer);
                quizAnswer.setQuiz(quiz);

                quiz.getAnswers().add(quizAnswer);
            }

            quizRepository.save(quiz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<QuizResponse> getAllQuizzes() {
        try {
            List<Quiz> quizzes = quizRepository.findAll();

            List<QuizResponse> quizResponses = new ArrayList<>();

            for (Quiz quiz : quizzes) {
                QuizResponse quizResponse = new QuizResponse();

                quizResponse.setId(quiz.getId());
                quizResponse.setQuestion(quiz.getQuestion());
                quizResponse.setAnswers(quiz.getAnswers());
                quizResponse.setLanguage(quiz.getLanguage());
                quizResponse.setCreatedByUsername(quiz.getCreatedBy().getUsername());
                quizResponses.add(quizResponse);
            }

            return quizResponses;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public void submitQuiz(ListQuizSubmitRequest req) {
        for(QuizSubmitRequest quizSubmitReq : req.getQuizSubmitRequests()) {
            Quiz quiz = quizRepository.findById(quizSubmitReq.getQuizId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id " + quizSubmitReq.getQuizId() + " not found!")
            );

            ApplicationUser appUser = auditorConfig.getCurrentAuditor().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application user not found!"));
            JwtUser user = userRepository.findByUsername(appUser.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!") );

            QuizUserAnswer quizUserAnswer = new QuizUserAnswer();
            quizUserAnswer.setQuiz(quiz);
            quizUserAnswer.setUser(user);
            quizUserAnswer.setUserAnswer(quizSubmitReq.getAnswer());

            quiz.getUserAnswers().add(quizUserAnswer);

            user.getUserAnswers().add(quizUserAnswer);

            user.getTakenQuizzes().add(quiz);

        }
    }

    @Override
    public List<QuizResponse> fetchQuizzesByLanguageCode(String languageCode, Integer numOfQuestions) {

        SupportedLanguage language = supportedLanguagesRepository.findByLanguageCode(languageCode).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Language with code " + languageCode + " not found!")
        );

        ApplicationUser appUser = auditorConfig.getCurrentAuditor().orElse(null);
        JwtUser user = userRepository.findByUsername(appUser.getUsername()).orElse(null);

        List<Quiz> quizzes = quizRepository.findByLanguage(language);

        if (quizzes.size() < numOfQuestions) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough quizzes for " + language.getLanguageName() + " language!" + " Requested: " + numOfQuestions + ", available: " + quizzes.size());
        }

        List<QuizResponse> quizResponses = new ArrayList<>();

        Collections.shuffle(quizzes);

        for (Quiz quiz : quizzes.subList(0, numOfQuestions)) {
            QuizResponse quizResponse = new QuizResponse();


            quizResponse.setId(quiz.getId());
            quizResponse.setQuestion(quiz.getQuestion());
            quizResponse.setAnswers(quiz.getAnswers());

            Optional<QuizUserAnswer> userAnswer = quizUserAnswerRepository.findByQuizIdAndUserId(quiz.getId(), user.getId());

            userAnswer.ifPresent(quizUserAnswer -> quizResponse.setUserAnswer(quizUserAnswer.getUserAnswer()));

            quizResponse.setLanguage(quiz.getLanguage());
            quizResponse.setCreatedByUsername(quiz.getCreatedBy().getUsername());
            quizResponses.add(quizResponse);
        }

        return quizResponses;
    }

    @Override
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}
