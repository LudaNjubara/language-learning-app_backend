package com.app.learning.language.langugelearningapp_backend.controller;

import com.app.learning.language.langugelearningapp_backend.configuration.AuditorConfig;
import com.app.learning.language.langugelearningapp_backend.request.ListQuizSubmitRequest;
import com.app.learning.language.langugelearningapp_backend.request.QuizPostRequest;
import com.app.learning.language.langugelearningapp_backend.request.QuizSubmitRequest;
import com.app.learning.language.langugelearningapp_backend.response.QuizResponse;
import com.app.learning.language.langugelearningapp_backend.security.model.Authority;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.app.learning.language.langugelearningapp_backend.service.QuizService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class QuizController {

    private AuditorConfig auditorConfig;

    private final QuizService quizService;

    @PostMapping("/submit")
    public void submitQuiz(
            @Valid @RequestBody final QuizSubmitRequest req
    ) {
        quizService.submitQuiz(req);
    }

    @GetMapping("/fetch")
    public QuizResponse fetchQuiz(
            @RequestParam final String languageCode,
            @RequestParam final Integer numOfQuestions
    ) {
        return quizService.fetchQuiz(languageCode, numOfQuestions);
    }

    @GetMapping("/get-all")
    public List<QuizResponse> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
    }
}
