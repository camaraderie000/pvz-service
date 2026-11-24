package com.example.pvz.domain;

import java.util.UUID;

public record UserDto(UUID id, String email, String role) {}