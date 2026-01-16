package com.desafio.pixpay.core.domain.identification;


public class CadastroNacionalDePessoaJuridica extends IdentificationNumber {

    public CadastroNacionalDePessoaJuridica(String identificationCode) {
        super();
        if (identificationCode.length() != 14){
            throw new IllegalArgumentException("The identification number for Cadastro Nacional De Pessoa Juridica must be 14 characters long. Ex: 00.000.000/0000-00");
        }
        this.identificationCode = identificationCode;
    }
    
}
