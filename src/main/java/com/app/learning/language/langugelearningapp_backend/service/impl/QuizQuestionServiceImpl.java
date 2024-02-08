package com.app.learning.language.langugelearningapp_backend.service.impl;

import com.app.learning.language.langugelearningapp_backend.configuration.AuditorConfig;
import com.app.learning.language.langugelearningapp_backend.dto.QuizQuestionRequestDTO;
import com.app.learning.language.langugelearningapp_backend.model.QuizAnswer;
import com.app.learning.language.langugelearningapp_backend.model.QuizQuestion;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.app.learning.language.langugelearningapp_backend.repository.QuizQuestionRepository;
import com.app.learning.language.langugelearningapp_backend.repository.SupportedLanguagesRepository;
import com.app.learning.language.langugelearningapp_backend.request.QuizQuestionPostRequestBody;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.app.learning.language.langugelearningapp_backend.service.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizQuestionServiceImpl implements QuizQuestionService {

    private final SupportedLanguagesRepository supportedLanguagesRepository;

    private final QuizQuestionRepository quizQuestionRepository;

    private final AuditorConfig auditorConfig;

    @Override
    public void createQuestion(QuizQuestionPostRequestBody req) {
        List<QuizQuestionRequestDTO> quizQuestionRequestDTOList = req.getQuizQuestionRequestDTOList();

        JwtUser appUser = auditorConfig.getCurrentAuditor().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        for (QuizQuestionRequestDTO quizQuestionRequestDTO : quizQuestionRequestDTOList) {
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setText(quizQuestionRequestDTO.getText());
            quizQuestion.setCreatedBy(appUser);
            quizQuestion.setCreatedAt(LocalDateTime.now());

            SupportedLanguage language = supportedLanguagesRepository.findByLanguageCode(quizQuestionRequestDTO.getLanguageCode())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Language with code " + quizQuestionRequestDTO.getLanguageCode() + " not found!"));
            quizQuestion.setLanguage(language);

            List<QuizAnswer> quizAnswerList = new ArrayList<>();
            for(String answerText : quizQuestionRequestDTO.getAnswers()) {
                QuizAnswer quizAnswer = new QuizAnswer();
                quizAnswer.setText(answerText);
                quizAnswer.setQuestion(quizQuestion);
                quizAnswerList.add(quizAnswer);
            }
            quizQuestion.setAnswers(quizAnswerList);

            quizQuestionRepository.save(quizQuestion);
        }
    }

    @Override
    public List<QuizQuestion> getRandomQuizQuestionsByLanguageAndNumOfQuestions(SupportedLanguage language, Integer numOfQuestions) {
        List<QuizQuestion> listOfQuestions = quizQuestionRepository.findAll();

        if (listOfQuestions.size() < numOfQuestions) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not enough questions found for " + language.getLanguageName() + " language! " + "Available questions: " + listOfQuestions.size() + " Requested questions: " + numOfQuestions);
        }

        // Select random questions from the list, max is the number of questions requested
        List<QuizQuestion> randomQuestions = new ArrayList<>();
        for (int i = 0; i < numOfQuestions; i++) {
            int randomIndex = (int) (Math.random() * listOfQuestions.size());
            randomQuestions.add(listOfQuestions.get(randomIndex));
            listOfQuestions.remove(randomIndex);
        }

        return randomQuestions;
    }
}
