package com.desafio.pixpay.core.domain.account;

import com.desafio.pixpay.core.gateways.PasswordEncoderGateway;

public class Password {
    private String password;

    public Password setPasswordAndValidate(String password, PasswordEncoderGateway passwordEncoder){
        String validPasswordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$.,&%?_+]).+$";
        String invalidRegex = ".*[\\s\\\\;\"'<>/|\\-\\-\\*\\(\\)\\[\\]{}].*";
        boolean isPasswordValid = password.matches(validPasswordRegex);
        boolean isValid = !password.matches(invalidRegex);
        if (password.length() < 8 || password.length() > 18) {
            throw new IllegalArgumentException("The password must be between 8 and 18 characters long.");
        }
        if (!isPasswordValid){
            throw new IllegalArgumentException("The password must contain one uppercase letter, one lowercase letter, one number and at least one of this special character: , . ! @ # $ & % ? _ +.");
        }
        if (!isValid) {
            throw new IllegalArgumentException("The fields cannot contain this special character: \\ / | * ( ) [ ] { } ; ' \" < > or spaces.");
        }
        this.password = passwordEncoder.encode(password);
        return this;
    }

    public Password fromPersistence(String password){
        this.password = password;
        return this;
    }

    public String getValue(){
        return this.password;
    }
}
