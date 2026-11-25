package com.example.pvz.service;

import com.example.pvz.api.ApiException;
import com.example.pvz.domain.Product;
import com.example.pvz.repo.ProductRepository;
import com.example.pvz.repo.ReceptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ReceptionRepository receptions;
    private final ProductRepository products;

    @Transactional
    public Product add(UUID pvzId, String type) {
        UUID rid = receptions.lockOpen(pvzId)
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT, "нет открытой приёмки"));
        return products.insert(rid, type);
    }

    @Transactional
    public void deleteLast(UUID pvzId) {
        UUID rid = receptions.lockOpen(pvzId)
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT, "нет открытой приёмки"));
        products.deleteLast(rid)
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT, "в приёмке нет товаров"));
    }
}