package com.app.learning.language.langugelearningapp_backend.service.impl;

import com.app.learning.language.langugelearningapp_backend.dto.UserDTO;
import com.app.learning.language.langugelearningapp_backend.model.Quiz;
import com.app.learning.language.langugelearningapp_backend.model.QuizAnswer;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.app.learning.language.langugelearningapp_backend.repository.QuizRepository;
import com.app.learning.language.langugelearningapp_backend.repository.SupportedLanguagesRepository;
import com.app.learning.language.langugelearningapp_backend.request.QuizPostRequest;
import com.app.learning.language.langugelearningapp_backend.security.model.ApplicationUser;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.app.learning.language.langugelearningapp_backend.security.repository.UserRepository;
import com.app.learning.language.langugelearningapp_backend.service.QuizService;
import com.app.learning.language.langugelearningapp_backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    private final UserRepository userRepository;
    private final SupportedLanguagesRepository supportedLanguagesRepository;

    public QuizServiceImpl(QuizRepository quizRepository, UserRepository userRepository, SupportedLanguagesRepository supportedLanguagesRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.supportedLanguagesRepository = supportedLanguagesRepository;
    }

    @Override
    public void createQuiz(QuizPostRequest req, ApplicationUser appUser) {
        try {
            Optional<JwtUser> user = userRepository.findByUsername(appUser.getUsername());
            Optional<SupportedLanguage> language = supportedLanguagesRepository.findByLanguageCode(req.getLanguageCode());


            if(user.isEmpty()) {
                throw new Exception("User with username " + appUser.getUsername() + " not found!");
            }

            if(language.isEmpty()) {
                throw new Exception("Language with code " + req.getLanguageCode() + " not found!");
            }

            Quiz quiz = new Quiz();
            quiz.setCreatedBy(user.get());
            quiz.setLanguage(language.get());
            quiz.setQuestion(req.getQuestion());
            for(String answer : req.getAnswers()) {
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
    public List<Quiz> getAllQuizzes() {
        try {
            return quizRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Quiz> fetchQuizzesByLanguageCode(String languageCode, Integer numOfQuestions) {
        try {
            Optional<SupportedLanguage> language = supportedLanguagesRepository.findByLanguageCode(languageCode);

            if(language.isEmpty()) {
                throw new Exception("Language with code " + languageCode + " not found!");
            }

            List<Quiz> quizzes = quizRepository.findByLanguage(language.get());

            if(quizzes.size() < numOfQuestions) {
                throw new Exception("Not enough quizzes for " + language.get().getLanguageName() + " language!" + " Requested: " + numOfQuestions + ", available: " + quizzes.size());
            }

            // shuffle quizzes and return the first numOfQuestions
            quizzes.sort((a, b) -> 0.5 > Math.random() ? -1 : 1);
            return quizzes.subList(0, numOfQuestions);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
