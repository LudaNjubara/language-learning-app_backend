package com.app.learning.language.langugelearningapp_backend.controller;

import com.app.learning.language.langugelearningapp_backend.dto.UserDTO;
import com.app.learning.language.langugelearningapp_backend.request.UserPutRequest;
import com.app.learning.language.langugelearningapp_backend.security.service.JwtService;
import com.app.learning.language.langugelearningapp_backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.logging.Logger;

import static com.app.learning.language.langugelearningapp_backend.security.jwt.JwtFilter.AUTHORIZATION_TOKEN_PREFIX;

@RestController
@RequestMapping("/api/user")
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public UserDTO getUserData(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
                String username = jwtService.getUserDataFromJwt(token.substring(AUTHORIZATION_TOKEN_PREFIX.length())).getUsername();
                return userService.getUserData(username);
            }
            return null;
        } catch (Exception e) {
            Logger.getLogger("Error: " + e);
            return null;
        }
    }

    @PutMapping(value = "/update-selected-language")
    public void updateLearningLanguage(
            @Valid @RequestBody UserPutRequest userPutRequest
    ) {
        try {
            userService.updateSelectedLanguage(userPutRequest.getUsername(), userPutRequest.getLanguageCode());
        } catch (Exception e) {
            Logger.getLogger("Error: " + e);
        }
    }
}
