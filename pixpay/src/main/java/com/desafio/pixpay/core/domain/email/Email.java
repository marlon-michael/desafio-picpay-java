package com.desafio.pixpay.core.domain.email;

import com.desafio.pixpay.core.gateways.EmailValidatorGateway;

public class Email {
    private String email;

    public Email setEmailAndValidate(String email, EmailValidatorGateway emailValidatorGateway) {
        if(emailValidatorGateway == null) throw new RuntimeException("EmailValidatorGateway should not be null.");
        if(!emailValidatorGateway.isEmailValid(email)) throw new IllegalArgumentException("Invalid email format.");
        if (email.length() < 3 || email.length() > 254) throw new IllegalArgumentException("The email must be between 3 and 254 characters long.");
        this.email = email;
        return this;
    }

    public Email fromPersistence(String email){
        this.email = email;
        return this;
    }

    public String getValue(){
        return this.email;
    }
}
