package com.app.learning.language.langugelearningapp_backend.service;


import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;

import java.util.List;

public interface SupportedLanguagesService {
    List<SupportedLanguage> getAll();
}
