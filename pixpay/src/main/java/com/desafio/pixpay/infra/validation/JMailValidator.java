package com.desafio.pixpay.infra.validation;

import com.desafio.pixpay.core.gateways.EmailValidatorGateway;
import com.sanctionco.jmail.JMail;

public class JMailValidator implements EmailValidatorGateway {

    @Override
    public boolean isEmailValid(String email) {
        return JMail.strictValidator().isValid(email);
    }
    
}
