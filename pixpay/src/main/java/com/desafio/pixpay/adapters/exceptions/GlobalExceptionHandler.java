package com.desafio.pixpay.adapters.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler; // Esta é a anotação
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.exceptions.DefaultException;
import com.desafio.pixpay.core.exceptions.InternalServerException;
import com.desafio.pixpay.core.exceptions.InvalidDataException;
import com.desafio.pixpay.core.exceptions.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleInvalidDataException(InvalidDataException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException excepetion){
        return ResponseEntity.status(422).body(excepetion.getMessage());
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<String> handleInternalServerException(InternalServerException exception) {
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }

    @ExceptionHandler(DefaultException.class)
    public ResponseEntity<String> handleDefaultException(DefaultException exception) {
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }
}