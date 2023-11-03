package com.app.learning.language.langugelearningapp_backend.controller;

import com.app.learning.language.langugelearningapp_backend.exception.ErrorResponse;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.app.learning.language.langugelearningapp_backend.service.SupportedLanguagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/supported-languages")
@CrossOrigin(origins = "http://localhost:3000")
public class SupportedLanguagesController {

    private final SupportedLanguagesService supportedLanguagesService;


    public SupportedLanguagesController(SupportedLanguagesService supportedLanguagesService) {
        this.supportedLanguagesService = supportedLanguagesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SupportedLanguage>> getAll() {
        System.out.println("Inside getAll() of SupportedLanguagesController");
        try {
            List<SupportedLanguage> supportedLanguages = supportedLanguagesService.getAll();
            System.out.println(supportedLanguages);

            return ResponseEntity.ok(supportedLanguages);
        } catch (Exception e) {
            Logger.getLogger("Error: " + e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
    }
}
