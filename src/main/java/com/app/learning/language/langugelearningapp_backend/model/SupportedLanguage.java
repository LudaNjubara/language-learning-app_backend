package com.app.learning.language.langugelearningapp_backend.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "supported_languages")
@Data
public class SupportedLanguage {

    @Id
    @Column(name = "language_code", nullable = false)
    private String languageCode;

    @Column(name = "language_name", nullable = false)
    private String languageName;

}
