package com.example.pvz.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Reception(UUID id, OffsetDateTime dateTime, UUID pvzId, String status) {}
