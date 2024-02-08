package com.app.learning.language.langugelearningapp_backend.service.impl;

import com.app.learning.language.langugelearningapp_backend.dto.UserDTO;
import com.app.learning.language.langugelearningapp_backend.model.QuizUserAnswer;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.app.learning.language.langugelearningapp_backend.repository.QuizUserAnswerRepository;
import com.app.learning.language.langugelearningapp_backend.repository.SupportedLanguagesRepository;
import com.app.learning.language.langugelearningapp_backend.response.QuizResponse;
import com.app.learning.language.langugelearningapp_backend.security.model.Authority;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.app.learning.language.langugelearningapp_backend.security.repository.UserRepository;
import com.app.learning.language.langugelearningapp_backend.service.SupportedLanguagesService;
import com.app.learning.language.langugelearningapp_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final QuizUserAnswerRepository quizUserAnswerRepository;
    private final SupportedLanguagesRepository supportedLanguagesRepository;

    @Override
    @SneakyThrows
    public UserDTO getUserData(String username) {
        Optional<JwtUser> jwtUser = userRepository.findByUsername(username);

        if (jwtUser.isEmpty()) throw new Exception("User not found");

        JwtUser user = jwtUser.get();

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setSelectedLanguage(user.getSelectedLanguage());
        userDTO.setAuthorities(user.getAuthorities().stream().map(Authority::name).toList());

        return userDTO;
    }

    @Override
    public void updateSelectedLanguage(String username, String languageCode) throws Exception {
        Optional<JwtUser> jwtUser = userRepository.findByUsername(username);

        if (jwtUser.isEmpty())  throw new Exception("User not found");

        JwtUser user = jwtUser.get();

        Optional<SupportedLanguage> language = supportedLanguagesRepository.findByLanguageCode(languageCode);

        if (language.isEmpty()) throw new Exception("Language not found");

        user.setSelectedLanguage(language.get());
        userRepository.save(user);
    }
}
