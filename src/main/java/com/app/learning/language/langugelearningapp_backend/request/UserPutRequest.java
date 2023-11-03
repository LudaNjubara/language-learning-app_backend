package com.app.learning.language.langugelearningapp_backend.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserPutRequest {
    @NotNull(message = "Username cannot be null")
    private String username;

    private String languageCode;
}
