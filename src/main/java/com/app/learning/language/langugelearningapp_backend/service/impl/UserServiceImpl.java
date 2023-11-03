package com.app.learning.language.langugelearningapp_backend.service.impl;

import com.app.learning.language.langugelearningapp_backend.dto.UserDTO;
import com.app.learning.language.langugelearningapp_backend.model.SupportedLanguage;
import com.app.learning.language.langugelearningapp_backend.repository.SupportedLanguagesRepository;
import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;
import com.app.learning.language.langugelearningapp_backend.security.repository.UserRepository;
import com.app.learning.language.langugelearningapp_backend.service.SupportedLanguagesService;
import com.app.learning.language.langugelearningapp_backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SupportedLanguagesRepository supportedLanguagesRepository;

    public UserServiceImpl(UserRepository userRepository, SupportedLanguagesRepository supportedLanguagesRepository) {
        this.userRepository = userRepository;
        this.supportedLanguagesRepository = supportedLanguagesRepository;
    }

    @Override
    public UserDTO getUserData(String username) throws Exception {
        Optional<JwtUser> jwtUser = userRepository.findByUsername(username);

        if (jwtUser.isEmpty())  throw new Exception("User not found");

        JwtUser user = jwtUser.get();

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setSelectedLanguage(user.getSelectedLanguage());

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
