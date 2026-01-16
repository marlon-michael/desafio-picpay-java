package com.desafio.pixpay.core.domain.identification;

public class IdentificationFactory {
    public static IdentificationNumber createIdentification(IdentificationTypeEnum identificationType, String identificationCode) {
        if (identificationType.getValue().equals("CadastroDePessoaFisica")) {
            return new CadastroDePessoaFisica(identificationCode);
        }
        else if (identificationType.getValue().equals("CadastroNacionalDePessoaJuridica")) {
            return new CadastroNacionalDePessoaJuridica(identificationCode);
        }
        throw new IllegalArgumentException("Unsupported identification type: " + identificationType);
    }
}
