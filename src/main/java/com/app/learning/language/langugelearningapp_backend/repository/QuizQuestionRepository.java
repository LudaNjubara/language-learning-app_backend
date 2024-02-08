package com.app.learning.language.langugelearningapp_backend.repository;

import com.app.learning.language.langugelearningapp_backend.model.QuizQuestion;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
}