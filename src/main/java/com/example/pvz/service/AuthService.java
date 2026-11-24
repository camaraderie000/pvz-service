package com.example.pvz.service;

import com.example.pvz.api.ApiException;
import com.example.pvz.domain.UserDto;
import com.example.pvz.domain.UserEntity;
import com.example.pvz.repo.UserRepository;
import com.example.pvz.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public UserDto register(String email, String password, String role) {
        if (users.existsByEmail(email))
            throw new ApiException(HttpStatus.BAD_REQUEST, "email уже занят");
        var u = new UserEntity();
        u.setId(UUID.randomUUID());
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(password));
        u.setRole(role);
        users.save(u);
        return new UserDto(u.getId(), u.getEmail(), u.getRole());
    }

    public String login(String email, String password) {
        var u = users.findByEmail(email)
                .filter(x -> encoder.matches(password, x.getPasswordHash()))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "неверные учётные данные"));
        return jwt.generate(u.getId(), u.getRole());
    }

    public String dummyLogin(String role) {
        return jwt.generate(UUID.randomUUID(), role);
    }
}