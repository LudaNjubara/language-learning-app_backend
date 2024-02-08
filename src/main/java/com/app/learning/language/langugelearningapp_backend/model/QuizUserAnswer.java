package com.app.learning.language.langugelearningapp_backend.model;

import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "quiz_user_answer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizUserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuizQuestion question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private JwtUser user;

    @Column(name = "user_answer")
    private String userAnswer;
}