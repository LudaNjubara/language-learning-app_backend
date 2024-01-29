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

    private String userAnswer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private JwtUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    @JsonIgnore
    private Quiz quiz;
}
