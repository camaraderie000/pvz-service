package com.example.pvz.api;

import com.example.pvz.domain.PvzDto;
import com.example.pvz.domain.PvzView;
import com.example.pvz.repo.PvzQueryRepository;
import com.example.pvz.security.PvzService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class PvzController {
    record CreateRequest(@NotNull @Pattern(regexp = "Москва|Санкт-Петербург|Казань") String city) {}

    private final PvzService service;
    private final PvzQueryRepository query;

    @PostMapping("/pvz") @ResponseStatus(HttpStatus.CREATED)
    PvzDto create(@Valid @RequestBody CreateRequest r) {
        return service.create(r.city());
    }

    @GetMapping("/pvz")
    List<PvzView> list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(30) int limit) {
        return query.list(startDate, endDate, page, limit);
    }

}