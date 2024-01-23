package com.app.learning.language.langugelearningapp_backend.security.model;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ApplicationUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 8533039291044343363L;

    private String username;
    private String password;
    private List<SimpleGrantedAuthority> authorities;
}