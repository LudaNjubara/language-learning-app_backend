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
}
