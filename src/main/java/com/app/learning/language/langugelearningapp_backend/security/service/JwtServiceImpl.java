package com.app.learning.language.langugelearningapp_backend.security.service;

import com.app.learning.language.langugelearningapp_backend.security.model.Authority;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.app.learning.language.langugelearningapp_backend.security.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final UserRepository userRepository;
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
        JwtUser applicationUser = getUserDataFromJwt(token);
        saveAuthentication(applicationUser);

        return true;
    }

    @Override
    public String createJwt(JwtUser jwtUser) {
        Instant expiration = Instant.now().plusSeconds(accessTokenValiditySeconds);
        String authorities = jwtUser.getAuthorities()
                .stream()
                .map(Authority::name)
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

    public JwtUser getUserDataFromJwt(String jwtToken) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody();

        JwtUser jwtUser = userRepository
                .findByUsername(claims.getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        jwtUser.setQuizzesTaken(null);

        return jwtUser;
    }

    private void saveAuthentication(JwtUser applicationUser) {
        List<SimpleGrantedAuthority> authorities = applicationUser.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.name()))
                .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(applicationUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void logout(String token) {

    }

}