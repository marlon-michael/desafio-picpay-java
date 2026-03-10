package com.desafio.pixpay.adapters.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler; // Esta é a anotação
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.desafio.pixpay.core.exceptions.BusinessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}