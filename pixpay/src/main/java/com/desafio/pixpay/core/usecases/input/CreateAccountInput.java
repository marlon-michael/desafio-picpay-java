package com.desafio.pixpay.core.usecases.input;

public class CreateAccountInput {
    private String accountType;
    private String identificationType;
    private String identificationNumber;
    private String fullName;
    private String email;
    private String password;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CreateAccountInput(
        String accountType,
        String identificationType,
        String identificationNumber,
        String fullName,
        String email,
        String password
    ) {
        this.accountType = accountType;
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    
}
