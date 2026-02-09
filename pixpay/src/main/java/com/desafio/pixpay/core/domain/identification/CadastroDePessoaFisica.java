package com.desafio.pixpay.core.domain.identification;


public class CadastroDePessoaFisica extends IdentificationNumber {
    public CadastroDePessoaFisica(String identificationNumber) {
        super();
        if (identificationNumber.length() != 11){
            throw new IllegalArgumentException("The identification number for Cadastro De Pessoa Fisica must be 11 characters long. Ex: 000.000.000-00.");
        }
        if (!identificationNumber.matches("^[0-9]{11}$")) {
            throw new IllegalArgumentException("The identification number for Cadastro De Pessoa Fisica must contain only numbers.");
        }
        this.identificationNumber = identificationNumber;
    }
}
