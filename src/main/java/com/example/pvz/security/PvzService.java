package com.example.pvz.security;

import com.example.pvz.domain.PvzDto;
import com.example.pvz.domain.PvzEntity;
import com.example.pvz.repo.PvzRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PvzService {
    private final PvzRepository repo;

    @Transactional
    public PvzDto create(String city) {
        PvzEntity e = new PvzEntity();
        e.setId(UUID.randomUUID());
        e.setCity(city);
        e.setRegistrationDate(OffsetDateTime.now());
        repo.save(e);
        return new PvzDto(e.getId(), e.getRegistrationDate(), e.getCity());
    }
}