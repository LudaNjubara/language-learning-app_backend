package com.app.learning.language.langugelearningapp_backend.service;

import com.app.learning.language.langugelearningapp_backend.dto.UserDTO;

public interface UserService {
    UserDTO getUserData(String username);
    void updateSelectedLanguage(String username, String languageCode) throws Exception;
}
