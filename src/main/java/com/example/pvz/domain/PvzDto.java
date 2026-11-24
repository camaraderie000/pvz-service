package com.example.pvz.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PvzDto(UUID id, OffsetDateTime registrationDate, String city) {}