package com.app.learning.language.langugelearningapp_backend.repository;

import com.app.learning.language.langugelearningapp_backend.model.QuizUserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizUserAnswerRepository extends JpaRepository<QuizUserAnswer, Long> {
    Optional<QuizUserAnswer> findByQuizIdAndUserId(Long quizId, Long userId);
}
