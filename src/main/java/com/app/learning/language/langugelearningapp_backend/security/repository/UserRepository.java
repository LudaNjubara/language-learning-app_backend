package com.app.learning.language.langugelearningapp_backend.security.repository;

import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<JwtUser, Long> {

    Optional<JwtUser> findByUsername(String username);

}