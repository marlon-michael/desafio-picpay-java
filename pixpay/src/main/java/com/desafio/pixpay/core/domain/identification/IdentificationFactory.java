package com.desafio.pixpay.core.domain.identification;

import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.gateways.IdentificationValidatorGateway;

public class IdentificationFactory {
    private static IdentificationValidatorGateway identificationValidatorGateway;

    public static void initIdentificationFactory(IdentificationValidatorGateway identificationValidatorGateway){
        IdentificationFactory.identificationValidatorGateway = identificationValidatorGateway;
    }

    public static Identification createIdentification(IdentificationTypeEnum identificationType, String identificationNumber) {
        if (IdentificationFactory.identificationValidatorGateway == null) throw new RuntimeException("identificationValidatorGateway is null");
        if (identificationType == null) throw new BusinessException("Empty identification type");
        if (identificationNumber == null) throw new BusinessException("Empty identification number");
        if (identificationType.getValue().equals("CadastroDePessoaFisica")) {
            return new CadastroDePessoaFisica().setIdentificationNumberAndValidate(identificationNumber, IdentificationFactory.identificationValidatorGateway);
        }
        if (identificationType.getValue().equals("CadastroNacionalDePessoaJuridica")) {
            return new CadastroNacionalDePessoaJuridica().setIdentificationNumberAndValidate(identificationNumber, IdentificationFactory.identificationValidatorGateway);
        }
        throw new BusinessException("Unsupported identification type: " + identificationType);
    }
}
