package com.app.learning.language.langugelearningapp_backend.security.model;

import com.app.learning.language.langugelearningapp_backend.model.Quiz;
import com.app.learning.language.langugelearningapp_backend.model.QuizUserAnswer;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @JsonIgnore
    @ManyToOne
    @JoinTable(
            name = "USER__LEARNING_LANGUAGE",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "lang_code", referencedColumnName = "language_code")}
    )
    @BatchSize(size = 20)
    private SupportedLanguage selectedLanguage;

    @ManyToMany
    @JoinTable(name = "USER_TAKEN_QUIZ",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "quiz_id", referencedColumnName = "id")}
    )
    @BatchSize(size = 20)
    private List<Quiz> takenQuizzes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<QuizUserAnswer> userAnswers = new ArrayList<>();

    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities = new HashSet<>();

}
