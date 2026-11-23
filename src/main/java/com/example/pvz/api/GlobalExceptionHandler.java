package com.example.pvz.api;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    record Err(String message) {}

    @ExceptionHandler(ApiException.class)
    ResponseEntity<Err> api(ApiException e) {
        return ResponseEntity.status(e.status()).body(new Err(e.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class,
            ConstraintViolationException.class, HandlerMethodValidationException.class,
            MethodArgumentTypeMismatchException.class})
    ResponseEntity<Err> badRequest(Exception e) {
        return ResponseEntity.badRequest().body(new Err("некорректный запрос"));
    }
}