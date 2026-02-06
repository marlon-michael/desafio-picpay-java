package com.desafio.pixpay.infra.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class ExceptionHandler {

    private static Map<Class<?>, ExceptionHandleable> exceptionHandlers = new HashMap<>();

    static {
        ServiceLoader<ExceptionHandleable> loader = ServiceLoader.load(ExceptionHandleable.class);
        for (ExceptionHandleable exceptionHandleable : loader) {
            handleException(exceptionHandleable.getTargetExceptionClass(), exceptionHandleable);
        }
    }

    public static void runUntil(int times, Runnable throwable) {
        int index = 0;
        do {
            try {
            throwable.run();
        } catch (Exception exception) {
            index++;
            if (index < times) continue;
            ExceptionHandleable exceptionHandler = exceptionHandlers.get(exception.getClass());
            if (exceptionHandlers != null) System.out.println(exception.toString());
            exceptionHandler.handle(exception);
        }
        } while (index < times);
    }

    public static void run(Runnable throwable) {
        try {
            throwable.run();
        } catch (Exception exception) {
            ExceptionHandleable exceptionHandler = exceptionHandlers.get(exception.getClass());
            if (exceptionHandlers != null) System.out.println(exception.toString());
            exceptionHandler.handle(exception);
        }
    }

    public static void handleException(Class<?> exception, ExceptionHandleable exceptionHandler){
        if (exceptionHandlers.containsValue(exceptionHandler))
        exceptionHandlers.put(exception, exceptionHandler);
    }
    
}
