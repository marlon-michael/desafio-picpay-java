package com.desafio.pixpay.infra.exceptions;

public interface ExceptionHandleable {
    Class<? extends Exception> getTargetExceptionClass();
    void handle(Exception e);
}
