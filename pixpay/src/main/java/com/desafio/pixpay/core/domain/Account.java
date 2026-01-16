package com.desafio.pixpay.core.domain;

import java.util.UUID;

import com.desafio.pixpay.core.domain.identification.IdentificationFactory;
import com.desafio.pixpay.core.domain.identification.IdentificationNumber;
import com.desafio.pixpay.core.domain.identification.IdentificationTypeEnum;
import com.desafio.pixpay.core.gateways.EmailValidatorGateway;


public class Account {
    UUID id;
    AccountTypeEnum accountType;
    IdentificationNumber identificationNumber;
    IdentificationTypeEnum identificationType;
    String fullName;
    String email;
    String password;
    Long balanceInPipsOfReal;

    public Account(){}

    public Account(AccountTypeEnum accountType, IdentificationTypeEnum identificationType, String identificationNumber, String fullName, String email, String password, EmailValidatorGateway emailValidator) {
        setId(UUID.randomUUID());
        setAccountType(accountType);
        setIdentificationType(identificationType);
        setIdentificationNumber(identificationType, identificationNumber);
        setFullName(fullName);
        setEmail(email, emailValidator);
        setPassword(password);
        setBalanceInPipsOfReal(0L);
    }

    public Account(UUID id, AccountTypeEnum accountType, IdentificationTypeEnum identificationType, String identificationNumber, String fullName, String email, String password, Long balanceInPipsOfReal) {
        this.id = id;
        this.accountType = accountType;
        this.identificationType = identificationType;
        this.identificationNumber = IdentificationFactory.createIdentification(identificationType, identificationNumber);
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.balanceInPipsOfReal = balanceInPipsOfReal;
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

    public boolean isFullNameValid(String fullName) {
        // The full name must contain only letters and spaces
        String invalidRegex = ".*[\\\\;\"'<>/|\\-\\-\\*\\(\\)\\[\\]{}].*";
        boolean isValid = !fullName.matches(invalidRegex);
        if (!isValid){
            throw new IllegalArgumentException("The full name cannot contain this special character: \\\\ / | * ( ) [ ] { } ; ' \\\" < >.");
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
    public void setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
    }

    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public String getIdentificationNumber() {
        return identificationNumber.getIdentificationCode();
    }

    public void setIdentificationNumber(IdentificationTypeEnum identificationType, String identificationNumber) {
        isStringValid(identificationNumber);
        this.identificationNumber = IdentificationFactory.createIdentification(identificationType, identificationNumber);
    }

    public IdentificationTypeEnum getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(IdentificationTypeEnum identificationType) {
        this.identificationType = identificationType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        isFullNameValid(fullName);
        if (fullName.length() < 1 || fullName.length() > 40) {
            throw new IllegalArgumentException("The full name must be between 1 and 40 characters long.");
        }
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email, EmailValidatorGateway emailValidator) {
        if (email.length() < 3 || email.length() > 254) {
            throw new IllegalArgumentException("The email must be between 3 and 254 characters long.");
        }
        if (!emailValidator.isEmailValid(email)){
            throw new IllegalArgumentException("The email format is invalid.");
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