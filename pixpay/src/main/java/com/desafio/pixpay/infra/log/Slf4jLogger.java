package com.desafio.pixpay.infra.log;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import com.desafio.pixpay.core.gateways.LoggerGateway;
import com.desafio.pixpay.core.log.LogField;

import tools.jackson.databind.ObjectMapper;

public class Slf4jLogger implements LoggerGateway{

    private Logger logger = LoggerFactory.getLogger(Slf4jLogger.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void info(String message) {
        executeLog(message, null, logger::info);
    }

    @Override
    public void info(String message, Map<LogField, Object> details) {
        executeLog(message, details, logger::info);
    }

    @Override
    public void warn(String message) {
        executeLog(message, null, logger::warn);
    }

    @Override
    public void warn(String message, Map<LogField, Object> details) {
        executeLog(message, details, logger::warn);
    }

    @Override
    public void error(String message) {
        executeLog(message, null, logger::error);
    }

    @Override
    public void error(String message, Map<LogField, Object> details) {
        executeLog(message, details, logger::error);
    }

    private void executeLog(String message, Map<LogField, Object> details, Consumer<String> logOperation) {
        if (details == null || details.isEmpty()) {
            logOperation.accept(message);
            return;
        }

        try {
            details = hideSensitiveFields(details);
            Map<String, Object> logPayload = new LinkedHashMap<>();
            logPayload.put("message", message);
            details.forEach((key, value) -> {
                if (key != null && value != null) {
                    logPayload.put(key.getFieldName(), String.valueOf(value));
                }
            });
            logOperation.accept(stringfy(logPayload));
        } catch (Exception exception) {
            logFallback(exception, message, details, logOperation);
        } finally {
            MDC.clear();
        }
    }

    private void logFallback(Exception exception, String message, Map<LogField, Object> details, Consumer<String> logOperation) {
        logger.error(exception.getMessage());
        String payload = stringfy(details);
        logOperation.accept(message + " | payload:" + payload);
    }

    private Map<LogField, Object> hideSensitiveFields(Map<LogField, Object> logFieldMap){
        if (logFieldMap == null) return Map.of();
        
        Map<LogField, Object> maskedMap = new HashMap<>();
        
        logFieldMap.forEach((field, value) -> {
            if (field.isSensitive() && value != null) {
                maskedMap.put(field, maskField(value.toString()));
            } else {
                maskedMap.put(field, value);
            }
        });
        
        return maskedMap;
    }

    private String maskField(String value) {
        if (value == null || value.length() <= 4) {
            return "****";
        }

        int length = value.length();
        String middle = value.substring(2, length - 2);
        
        return "**" + middle + "**";
    }

    private String stringfy(Object object){
        if (object == null) return "";
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            return "[Failed to serialize object to JSON]";
        }
    }
    
}
