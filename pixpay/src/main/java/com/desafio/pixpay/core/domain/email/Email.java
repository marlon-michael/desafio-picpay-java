package com.desafio.pixpay.core.domain.email;

import com.desafio.pixpay.core.exceptions.InvalidDataException;
import com.desafio.pixpay.core.gateways.EmailValidatorGateway;

public class Email {
    private String email;

    public Email setEmailAndValidate(String email, EmailValidatorGateway emailValidatorGateway) {
        if(emailValidatorGateway == null) throw new RuntimeException("EmailValidatorGateway should not be null.");
        if(email == null) throw new InvalidDataException("Empty email.");
        if(!emailValidatorGateway.isEmailValid(email)) throw new InvalidDataException("Invalid email format.");
        if (email.length() < 3 || email.length() > 254) throw new InvalidDataException("The email must be between 3 and 254 characters long.");
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
