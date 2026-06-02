package com.desafio.pixpay.adapters.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler; // Esta é a anotação
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.exceptions.DefaultException;
import com.desafio.pixpay.core.exceptions.InternalServerException;
import com.desafio.pixpay.core.exceptions.InvalidDataException;
import com.desafio.pixpay.core.exceptions.NotFoundException;
import com.desafio.pixpay.core.gateways.LoggerGateway;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private LoggerGateway logger;

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleInvalidDataException(InvalidDataException exception){
        logger.warn(exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception){
        logger.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException exception){
        logger.warn(exception.getMessage());
        return ResponseEntity.status(422).body(exception.getMessage());
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<String> handleInternalServerException(InternalServerException exception) {
        logger.error(exception.getMessage() + exception);
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }

    @ExceptionHandler(DefaultException.class)
    public ResponseEntity<String> handleDefaultException(DefaultException exception) {
        logger.error(exception.getMessage() + exception);
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }
}