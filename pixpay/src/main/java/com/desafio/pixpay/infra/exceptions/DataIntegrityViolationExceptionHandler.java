package com.desafio.pixpay.infra.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

import com.desafio.pixpay.core.exceptions.UuidAlreadyExistsException;

public class DataIntegrityViolationExceptionHandler implements ExceptionHandleable{

    @Override
    public Class<? extends Exception> getTargetExceptionClass() {
        return DataIntegrityViolationException.class;
    }

    @Override
    public void handle(Exception e) {
        throw new UuidAlreadyExistsException("UUID already exists in database.");
    }
    
}
