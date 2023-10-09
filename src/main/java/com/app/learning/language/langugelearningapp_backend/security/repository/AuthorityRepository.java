package com.app.learning.language.langugelearningapp_backend.security.repository;

import com.app.learning.language.langugelearningapp_backend.security.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(String name);
}
