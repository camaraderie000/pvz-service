package com.example.pvz.api;

import com.example.pvz.domain.Product;
import com.example.pvz.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductController {
    record AddRequest(@NotNull UUID pvzId,
                      @NotNull @Pattern(regexp = "электроника|одежда|обувь") String type) {}

    private final ProductService service;

    @PostMapping("/products") @ResponseStatus(HttpStatus.CREATED)
    Product add(@Valid @RequestBody AddRequest r) {
        return service.add(r.pvzId(), r.type());
    }

    @PostMapping("/pvz/{pvzId}/delete_last_product")
    void deleteLast(@PathVariable UUID pvzId) {
        service.deleteLast(pvzId);
    }
}