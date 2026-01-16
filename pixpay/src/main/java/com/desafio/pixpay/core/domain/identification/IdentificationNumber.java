package com.desafio.pixpay.core.domain.identification;

public abstract class IdentificationNumber {
    String identificationCode;

    protected IdentificationNumber() {}

    public IdentificationNumber(String identificationCode){
        this.identificationCode = identificationCode;
    }

    public String getIdentificationCode() {
        return identificationCode;
    }
}
