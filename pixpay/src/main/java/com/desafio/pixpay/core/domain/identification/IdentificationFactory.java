package com.desafio.pixpay.core.domain.identification;

import com.desafio.pixpay.core.gateways.IdentificationValidatorGateway;

public class IdentificationFactory {
    private static IdentificationValidatorGateway identificationValidatorGateway;

    public static void initIdentificationFactory(IdentificationValidatorGateway identificationValidatorGateway){
        IdentificationFactory.identificationValidatorGateway = identificationValidatorGateway;
    }

    public static Identification createIdentification(IdentificationTypeEnum identificationType, String identificationCode) {
        if (IdentificationFactory.identificationValidatorGateway == null) throw new RuntimeException("identificationValidatorGateway is null");
        if (identificationType.getValue().equals("CadastroDePessoaFisica")) {
            return new CadastroDePessoaFisica().setIdentificationNumberAndValidate(identificationCode, IdentificationFactory.identificationValidatorGateway);
        }
        if (identificationType.getValue().equals("CadastroNacionalDePessoaJuridica")) {
            return new CadastroNacionalDePessoaJuridica().setIdentificationNumberAndValidate(identificationCode, IdentificationFactory.identificationValidatorGateway);
        }
        throw new IllegalArgumentException("Unsupported identification type: " + identificationType);
    }
}
