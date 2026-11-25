package com.example.pvz.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Product(UUID id, OffsetDateTime dateTime, String type, UUID receptionId) {}

