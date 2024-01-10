package com.app.learning.language.langugelearningapp_backend.configuration;

import com.app.learning.language.langugelearningapp_backend.security.model.ApplicationUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
public class AuditorConfig implements AuditorAware<ApplicationUser> {


    @Override
    public Optional<ApplicationUser> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        return Optional.of((ApplicationUser) authentication.getPrincipal());
    }
}