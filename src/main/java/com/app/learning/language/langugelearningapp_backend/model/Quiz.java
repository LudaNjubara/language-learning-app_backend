package com.app.learning.language.langugelearningapp_backend.model;

import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "quiz")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizAnswer> answers = new ArrayList<>();

    @ManyToOne
    @JoinTable(
            name = "QUIZ__USER",
            joinColumns = {@JoinColumn(name = "quiz_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    @BatchSize(size = 20)
    private JwtUser createdBy;

    @ManyToOne
    @JoinTable(
            name = "QUIZ__LEARNING_LANGUAGE",
            joinColumns = {@JoinColumn(name = "quiz_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "lang_code", referencedColumnName = "language_code")}
    )
    @BatchSize(size = 20)
    private SupportedLanguage language;
}
