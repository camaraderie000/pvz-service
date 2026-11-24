package com.example.pvz.api;

import com.example.pvz.domain.PvzDto;
import com.example.pvz.security.PvzService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class PvzController {
    record CreateRequest(@NotNull @Pattern(regexp = "Москва|Санкт-Петербург|Казань") String city) {}

    private final PvzService service;

    @PostMapping("/pvz") @ResponseStatus(HttpStatus.CREATED)
    PvzDto create(@Valid @RequestBody CreateRequest r) {
        return service.create(r.city());
    }
}