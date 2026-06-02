package com.desafio.pixpay.core.gateways;

import java.util.Map;

import com.desafio.pixpay.core.log.LogField;

public interface LoggerGateway {
    public void info(String message);
    public void warn(String message);
    public void error(String message);
    public void info(String message, Map<LogField, Object> details);
    public void warn(String message, Map<LogField, Object> details);
    public void error(String message, Map<LogField, Object> details);
}
