package com.app.learning.language.langugelearningapp_backend.repository;

import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupportedLanguagesRepository extends JpaRepository<SupportedLanguage, String> {
    Optional<SupportedLanguage> findByLanguageCode(String languageCode);
}
