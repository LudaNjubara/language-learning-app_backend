package com.app.learning.language.langugelearningapp_backend.configuration;

import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorConfig implements AuditorAware<JwtUser> {


    @Override
    public Optional<JwtUser> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        return Optional.of((JwtUser) authentication.getPrincipal());
    }
}