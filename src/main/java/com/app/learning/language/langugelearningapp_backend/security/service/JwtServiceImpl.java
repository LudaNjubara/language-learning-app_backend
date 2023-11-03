package com.app.learning.language.langugelearningapp_backend.security.service;

import com.app.learning.language.langugelearningapp_backend.security.jwt.UserAuth;
import com.app.learning.language.langugelearningapp_backend.security.model.ApplicationUser;
import com.app.learning.language.langugelearningapp_backend.security.model.Authority;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);
    private static final String AUTHORITIES_KEY = "authorities";

    @Value("${security.authentication.jwt.access-token-validity-seconds}")
    private Long accessTokenValiditySeconds;

    @Value("${security.authentication.jwt.base64-secret}")
    private String secretKey;

    @Override
    public boolean authenticate(String token) {
        // If JWT is invalid, user can not be authenticated
        if (isJwtInvalid(token)) {
            return false;
        }

        // JWT is valid, store authentication in Spring security context
        ApplicationUser applicationUser = getUserDataFromJwt(token);
        saveAuthentication(applicationUser);

        return true;
    }

    @Override
    public String createJwt(JwtUser jwtUser) {
        Instant expiration = Instant.now().plusSeconds(accessTokenValiditySeconds);
        String authorities = jwtUser.getAuthorities()
                .stream()
                .map(Authority::getName)
                .collect(Collectors.joining(","));

        return Jwts
                .builder()
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setSubject(jwtUser.getUsername())
                .setExpiration(new Date(expiration.toEpochMilli()))
                .setIssuedAt(new Date())
                .claim(AUTHORITIES_KEY, authorities)
                .compact();
    }

    private boolean isJwtInvalid(String jwtToken) {
        try {
            jwtToken = jwtToken.trim();
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return false;
        } catch (SignatureException e) {
            log.debug("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.debug("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.debug("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e.getMessage());
        }
        return true;
    }

    public ApplicationUser getUserDataFromJwt(String jwtToken) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody();

        List<SimpleGrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(claims.getSubject());
        applicationUser.setAuthorities(authorities);

        return applicationUser;
    }

    private void saveAuthentication(ApplicationUser applicationUser) {
        Authentication authentication = new UserAuth(applicationUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void logout(String token) {

    }

}