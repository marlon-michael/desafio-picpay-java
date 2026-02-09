package com.desafio.pixpay.core.domain.identification;

public abstract class IdentificationNumber {
    String identificationNumber;

    protected IdentificationNumber() {}

    public IdentificationNumber(String identificationCode){
        this.identificationNumber = identificationCode;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }
}
