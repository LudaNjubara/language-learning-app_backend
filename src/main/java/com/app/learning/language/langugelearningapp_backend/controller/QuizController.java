package com.app.learning.language.langugelearningapp_backend.controller;

import com.app.learning.language.langugelearningapp_backend.configuration.AuditorConfig;
import com.app.learning.language.langugelearningapp_backend.request.ListQuizSubmitRequest;
import com.app.learning.language.langugelearningapp_backend.request.QuizPostRequest;
import com.app.learning.language.langugelearningapp_backend.response.QuizResponse;
import com.app.learning.language.langugelearningapp_backend.security.model.ApplicationUser;
import com.app.learning.language.langugelearningapp_backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/quiz")
//@CrossOrigin(origins = "http://localhost:3000")
public class QuizController {

    @Autowired
    private AuditorConfig auditorConfig;

    @Autowired
    private QuizService quizService;


    @PostMapping("/create")
    public ResponseEntity<Object> createQuiz(
            @Valid @RequestBody final QuizPostRequest req
    ) {
        try {
            ApplicationUser appUser = auditorConfig.getCurrentAuditor().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
            List<String> authorities = appUser.getAuthorities().stream().map(SimpleGrantedAuthority::getAuthority).toList();
            if (!authorities.contains("ROLE_USER")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to create a quiz!");
            }

            quizService.createQuiz(req, appUser);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/submit")
    public void submitQuiz(
            @Valid @RequestBody final ListQuizSubmitRequest req
    ) {
        quizService.submitQuiz(req);
    }

    @GetMapping("/fetch")
    public List<QuizResponse> fetchQuiz(
            @RequestParam final String languageCode,
            @RequestParam final Integer numOfQuestions
    ) {
        return quizService.fetchQuizzesByLanguageCode(languageCode, numOfQuestions);
    }

    @GetMapping("/get-all")
    public List<QuizResponse> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }
}
