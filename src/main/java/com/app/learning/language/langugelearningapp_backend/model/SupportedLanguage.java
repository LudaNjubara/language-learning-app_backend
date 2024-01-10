package com.app.learning.language.langugelearningapp_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "supported_languages")
@Data
public class SupportedLanguage {

    @Id
    @Column(name = "language_code", nullable = false)
    private String languageCode;

    @Column(name = "language_name", nullable = false)
    private String languageName;

}
