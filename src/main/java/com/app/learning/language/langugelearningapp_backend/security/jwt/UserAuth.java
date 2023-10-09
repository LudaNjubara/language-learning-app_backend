package com.app.learning.language.langugelearningapp_backend.security.jwt;

import com.app.learning.language.langugelearningapp_backend.security.model.ApplicationUser;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
public class UserAuth extends AbstractAuthenticationToken {
    @Serial
    private static final long serialVersionUID = 3651172851990132112L;

    private final ApplicationUser principal;

    public UserAuth(ApplicationUser principal) {
        super(principal.getAuthorities());
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "NO";
    }

    @Override
    public ApplicationUser getPrincipal() {
        return principal;
    }
}