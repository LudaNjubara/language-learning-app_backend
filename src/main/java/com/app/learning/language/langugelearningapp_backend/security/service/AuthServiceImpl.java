package com.app.learning.language.langugelearningapp_backend.security.service;

import com.app.learning.language.langugelearningapp_backend.security.dto.LoginDTO;
import com.app.learning.language.langugelearningapp_backend.security.model.Authority;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.app.learning.language.langugelearningapp_backend.security.repository.AuthorityRepository;
import com.app.learning.language.langugelearningapp_backend.security.repository.UserRepository;
import com.app.learning.language.langugelearningapp_backend.security.request.LoginRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public AuthServiceImpl(JwtService jwtService, UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Optional<LoginDTO> login(LoginRequest req) {
        Optional<JwtUser> user = userRepository.findByUsername(req.getUsername());

        if (user.isEmpty() || !isMatchingPassword(req.getPassword(), user.get().getPassword())) {
            return Optional.empty();
        }

        return Optional.of(
                new LoginDTO(
                        user.get().getId(),
                        user.get().getUsername(),
                        jwtService.createJwt(user.get())
                )
        );
    }

    @Override
    public Optional<LoginDTO> register(LoginRequest req) {
        Optional<JwtUser> user = userRepository.findByUsername(req.getUsername());

        if (user.isPresent()) {
            return Optional.empty();
        }

        JwtUser newUser = new JwtUser();
        newUser.setUsername(req.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder().encode(req.getPassword()));

        Authority userAuthority = authorityRepository.findByName("ROLE_USER");

        if (userAuthority != null) {
            newUser.getAuthorities().add(userAuthority);
        } else {
            throw new RuntimeException("User authority not found");
        }

        userRepository.save(newUser);

        return Optional.of(
                new LoginDTO(
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
