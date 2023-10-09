package com.app.learning.language.langugelearningapp_backend.security.service;

import com.app.learning.language.langugelearningapp_backend.security.model.JwtUser;

public interface JwtService {

    boolean authenticate(String token);

    String createJwt(JwtUser jwtUser);

    void logout(String token);
}
