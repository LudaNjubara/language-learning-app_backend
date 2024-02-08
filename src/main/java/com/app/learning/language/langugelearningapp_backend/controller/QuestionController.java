package com.app.learning.language.langugelearningapp_backend.controller;

import com.app.learning.language.langugelearningapp_backend.request.QuizQuestionPostRequestBody;
import com.app.learning.language.langugelearningapp_backend.service.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/question")
//@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {

    @Autowired
    private QuizQuestionService quizQuestionService;

    @PostMapping("/create")
    public void createQuestion(
            @Valid @RequestBody final QuizQuestionPostRequestBody req
    ) {
        quizQuestionService.createQuestion(req);
    }
}
