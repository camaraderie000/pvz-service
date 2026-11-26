package com.example.pvz.api;

import com.example.pvz.domain.Reception;
import com.example.pvz.service.ReceptionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReceptionController {
    record OpenRequest(@NotNull UUID pvzId) {}

    private final ReceptionService service;

    @PostMapping("/receptions") @ResponseStatus(HttpStatus.CREATED)
    Reception open(@Valid @RequestBody OpenRequest r) {
        return service.open(r.pvzId());
    }

    @PostMapping("/pvz/{pvzId}/close_last_reception")
    Reception close(@PathVariable UUID pvzId) {
        return service.close(pvzId);
    }
}