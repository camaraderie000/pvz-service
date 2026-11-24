package com.example.pvz.api;

import com.example.pvz.domain.UserDto;
import com.example.pvz.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private static final String ROLE = "employee|moderator";
    record RegisterRequest(@NotBlank @Email String email, @NotBlank String password,
                           @NotNull @Pattern(regexp = ROLE) String role) {}
    record LoginRequest(@NotBlank String email, @NotBlank String password) {}
    record DummyRequest(@NotNull @Pattern(regexp = ROLE) String role) {}
    record TokenResponse(String token) {}

    private final AuthService auth;

    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    UserDto register(@Valid @RequestBody RegisterRequest r) {
        return auth.register(r.email(), r.password(), r.role());
    }

    @PostMapping("/login")
    TokenResponse login(@Valid @RequestBody LoginRequest r) {
        return new TokenResponse(auth.login(r.email(), r.password()));
    }

    @PostMapping("/dummyLogin")
    TokenResponse dummy(@Valid @RequestBody DummyRequest r) {
        return new TokenResponse(auth.dummyLogin(r.role()));
    }
}