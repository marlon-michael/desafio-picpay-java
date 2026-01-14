package com.desafio.pixpay.core.domain;

import java.util.UUID;


public class Account {
    UUID id;
    String identificationNumber;
    String fullName;
    String email;
    String password;
    Long balanceInPipsOfReal;

    public Account(UUID id, String identificationNumber, String fullName, String email, String password, Long balanceInPipsOfReal) {
        setId(id);
        setIdentificationNumber(identificationNumber);
        setFullName(fullName);
        setEmail(email);
        setPassword(password);
        setBalanceInPipsOfReal(balanceInPipsOfReal);
    }

    public boolean isEmailValid(String email) {
        return false;
    }

    public boolean isPasswordValid(String password) {
        //String validPasswordRegexWithNoInvalidChars = "^(?!.*[\\s\\\\;\"'<>/|\\*\\(\\)\\[\\]{}]).*(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$.&%?_+]).+$";
        // password must contain one uppercase letter, one lowercase letter, one number and at least one special character: , . ! @ # $ & % ? _ +
        String validPasswordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$.,&%?_+]).+$";
        boolean isValid = password.matches(validPasswordRegex);
        if (!isValid){
            throw new IllegalArgumentException("The password must contain one uppercase letter, one lowercase letter, one number and at least one of this special character: , . ! @ # $ & % ? _ +");
        }
        return isValid;
    }

    public boolean isStringValid(String string) {
        // Regex detalhado:
        // \\\\  -> Corresponde a uma contrabarra literal (\)
        // \\s    -> Corresponde espaços, tabulações e quebras de linha
        // \"    -> Corresponde a aspas duplas (")
        // /, ;, ', <, >, |, - , * , ( , ) , [ , ] , { , } -> Corresponde aos respectivos caracteres literais
        String invalidRegex = ".*[\\s\\\\;\"'<>/|\\-\\-\\*\\(\\)\\[\\]{}].*";
        boolean isValid = !string.matches(invalidRegex);
        if (!isValid) {
            throw new IllegalArgumentException("The fields cannot contain this special character: \\ / | * ( ) [ ] { } ; ' \" < > or spaces");
        }
        return isValid;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        isStringValid(identificationNumber);
        if (identificationNumber.length() < 9 || identificationNumber.length() > 14) {
            throw new IllegalArgumentException("The identification number must be between 9 and 14 characters long. Ex: 000.000.000-00 or 00.000.000/0000-00");
        }
        this.identificationNumber = identificationNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        isStringValid(fullName);
        if (fullName.length() < 1 || fullName.length() > 40) {
            throw new IllegalArgumentException("The full name must be between 1 and 40 characters long.");
        }
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email.length() < 3 || email.length() > 254) {
            throw new IllegalArgumentException("The email must be between 3 and 254 characters long.");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        isStringValid(password);
        isPasswordValid(password);
        if (password.length() < 8 || password.length() > 18) {
            throw new IllegalArgumentException("The password must be between 8 and 18 characters long.");
        }
        this.password = password;
    }

    public Long getBalanceInPipsOfReal() {
        return balanceInPipsOfReal;
    }

    public void setBalanceInPipsOfReal(Long balanceInPipsOfReal) {
        this.balanceInPipsOfReal = balanceInPipsOfReal;
    }
}