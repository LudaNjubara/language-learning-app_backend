package com.app.learning.language.langugelearningapp_backend.service.impl;

import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.app.learning.language.langugelearningapp_backend.repository.SupportedLanguagesRepository;
import com.app.learning.language.langugelearningapp_backend.service.SupportedLanguagesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportedLanguagesServiceImpl implements SupportedLanguagesService {

    private final SupportedLanguagesRepository supportedLanguagesRepository;

    public SupportedLanguagesServiceImpl(SupportedLanguagesRepository supportedLanguagesRepository) {
        this.supportedLanguagesRepository = supportedLanguagesRepository;
    }

    @Override
    public List<SupportedLanguage> getAll() {
        List<SupportedLanguage> supportedLanguages = supportedLanguagesRepository.findAll();
        System.out.println("serv" + supportedLanguages);
        return supportedLanguages;
    }
}
