package com.app.learning.language.langugelearningapp_backend.model;

import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "taken_by")
    private JwtUser takenBy;

    private LocalDateTime takenAt;

    @ManyToOne
    @JoinColumn(name = "language_code")
    private SupportedLanguage language;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<QuizQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizUserAnswer> userAnswers = new ArrayList<>();
}
