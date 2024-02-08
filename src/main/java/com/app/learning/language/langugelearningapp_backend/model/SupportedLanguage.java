package com.app.learning.language.langugelearningapp_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "supported_languages")
@Data
public class SupportedLanguage {

    @Id
    @Column(name = "language_code", nullable = false)
    private String languageCode;

    @Column(name = "language_name", nullable = false)
    private String languageName;

}
