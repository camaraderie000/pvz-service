package com.example.pvz.service;

import com.example.pvz.api.ApiException;
import com.example.pvz.domain.Reception;
import com.example.pvz.repo.PvzRepository;
import com.example.pvz.repo.ReceptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceptionService {
    private final PvzRepository pvz;
    private final ReceptionRepository receptions;

    @Transactional
    public Reception open(UUID pvzId) {
        if (!pvz.existsById(pvzId))
            throw new ApiException(HttpStatus.NOT_FOUND, "ПВЗ не найден");
        return receptions.insertOpen(pvzId)
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT, "приёмка уже открыта"));
    }

    @Transactional
    public Reception close(UUID pvzId) {
        return receptions.close(pvzId)
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT, "нет открытой приёмки"));
    }
}