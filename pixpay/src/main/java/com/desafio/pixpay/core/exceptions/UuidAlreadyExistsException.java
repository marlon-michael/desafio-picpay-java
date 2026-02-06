package com.desafio.pixpay.core.exceptions;

public class UuidAlreadyExistsException extends RuntimeException{
    public UuidAlreadyExistsException(String message){
        super(message);
    }
}
