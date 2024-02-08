package com.app.learning.language.langugelearningapp_backend.model;

import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private JwtUser createdBy;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "language_code")
    private SupportedLanguage language;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuizAnswer> answers = new ArrayList<>();
}
