package com.app.learning.language.langugelearningapp_backend.security.service;

import com.app.learning.language.langugelearningapp_backend.configuration.AuditorConfig;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.app.learning.language.langugelearningapp_backend.repository.SupportedLanguagesRepository;
import com.app.learning.language.langugelearningapp_backend.security.dto.LoginDTO;
import com.app.learning.language.langugelearningapp_backend.security.dto.RegisterDTO;
import com.app.learning.language.langugelearningapp_backend.security.model.Authority;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.app.learning.language.langugelearningapp_backend.security.repository.UserRepository;
import com.app.learning.language.langugelearningapp_backend.security.request.LoginRequest;
import com.app.learning.language.langugelearningapp_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final SupportedLanguagesRepository supportedLanguagesRepository;

    private final AuditorConfig auditorConfig;

    @Override
    @SneakyThrows
    public Optional<LoginDTO> login(LoginRequest req) {
        JwtUser user = userRepository.findByUsername(req.getUsername()).orElse(null);

        if (user == null || !isMatchingPassword(req.getPassword(), user.getPassword())) {
            return Optional.empty();
        }

        return Optional.of(
                new LoginDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getAuthorities().stream().map(Authority::name).toList(),
                        user.getSelectedLanguage(),
                        jwtService.createJwt(user)
                )
        );
    }

    @Override
    public Optional<RegisterDTO> register(LoginRequest req) {
        Optional<JwtUser> user = userRepository.findByUsername(req.getUsername());
        if (user.isPresent()) {
            return Optional.empty();
        }

        JwtUser newUser = new JwtUser();
        newUser.setUsername(req.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder().encode(req.getPassword()));

        SupportedLanguage defaultLanguage = supportedLanguagesRepository.findByLanguageCode("en").orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Default language not found")
        );
        newUser.setSelectedLanguage(defaultLanguage);

        newUser.getAuthorities().add(Authority.ROLE_USER);
        userRepository.save(newUser);

        return Optional.of(
                new RegisterDTO(
                        newUser.getId(),
                        newUser.getUsername(),
                        newUser.getAuthorities().stream().map(Authority::name).toList(),
                        newUser.getSelectedLanguage(),
                        jwtService.createJwt(newUser)
                )
        );
    }

    @Override
    public boolean authenticate(String token) {
        return jwtService.authenticate(token);
    }

    @Override
    public void logout(String token) {
        jwtService.logout(token);
    }

    private boolean isMatchingPassword(String rawPassword, String encryptedPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(rawPassword, encryptedPassword);
    }
}
