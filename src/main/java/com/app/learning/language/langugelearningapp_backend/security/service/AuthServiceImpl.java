package com.app.learning.language.langugelearningapp_backend.security.service;

import com.app.learning.language.langugelearningapp_backend.dto.UserDTO;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.app.learning.language.langugelearningapp_backend.repository.SupportedLanguagesRepository;
import com.app.learning.language.langugelearningapp_backend.security.dto.LoginDTO;
import com.app.learning.language.langugelearningapp_backend.security.dto.RegisterDTO;
import com.app.learning.language.langugelearningapp_backend.security.model.Authority;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.app.learning.language.langugelearningapp_backend.security.repository.AuthorityRepository;
import com.app.learning.language.langugelearningapp_backend.security.repository.UserRepository;
import com.app.learning.language.langugelearningapp_backend.security.request.LoginRequest;
import com.app.learning.language.langugelearningapp_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final SupportedLanguagesRepository supportedLanguagesRepository;

    public AuthServiceImpl(JwtService jwtService, UserService userService, UserRepository userRepository, AuthorityRepository authorityRepository, SupportedLanguagesRepository supportedLanguagesRepository) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.supportedLanguagesRepository = supportedLanguagesRepository;
    }

    @Override
    @SneakyThrows
    public Optional<LoginDTO> login(LoginRequest req) {
        Optional<JwtUser> user = userRepository.findByUsername(req.getUsername());

        if (user.isEmpty() || !isMatchingPassword(req.getPassword(), user.get().getPassword())) {
            return Optional.empty();
        }

        UserDTO userDTO = userService.getUserData(req.getUsername());

        return Optional.of(
                new LoginDTO(
                        user.get().getId(),
                        user.get().getUsername(),
                        userDTO.getAuthorities(),
                        userDTO.getSelectedLanguage(),
                        userDTO.getTakenQuizzes(),
                        jwtService.createJwt(user.get())
                )
        );
    }

    @Override
    public Optional<RegisterDTO> register(LoginRequest req) {
        Optional<JwtUser> user = userRepository.findByUsername(req.getUsername());
        SupportedLanguage defaultLanguage = supportedLanguagesRepository.findByLanguageCode("en").orElseThrow(
                () -> new RuntimeException("Default language not found")
        );

        if (user.isPresent()) {
            return Optional.empty();
        }

        JwtUser newUser = new JwtUser();
        newUser.setUsername(req.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder().encode(req.getPassword()));
        newUser.setSelectedLanguage(defaultLanguage);

        Authority userAuthority = authorityRepository.findByName("ROLE_USER");

        if (userAuthority != null) {
            newUser.getAuthorities().add(userAuthority);
        } else {
            throw new RuntimeException("User authority not found");
        }

        userRepository.save(newUser);

        return Optional.of(
                new RegisterDTO(
                        newUser.getId(),
                        newUser.getUsername(),
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
