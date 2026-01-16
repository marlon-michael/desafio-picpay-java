package com.desafio.pixpay.infra.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.constraints.br.CPF;

import com.desafio.pixpay.core.gateways.IdentificationValidator;

import org.hibernate.validator.constraints.br.CNPJ;

public class JakartaIdentificationValidator implements IdentificationValidator {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public boolean isCPFValid(String cpf) {
        // Valida o valor contra a regra da anotação @CPF e retorna se não há erros
        return validator.validateValue(DataTransferObject.class, "cpf", cpf).isEmpty();
    }

    @Override
    public boolean isCNPJValid(String cnpj) {
        // Valida o valor contra a regra da anotação @CNPJ
        return validator.validateValue(DataTransferObject.class, "cnpj", cnpj).isEmpty();
    }

    // Classe auxiliar apenas para referenciar as regras brasileiras
    private static class DataTransferObject {
        @CPF String cpf;
        @CNPJ String cnpj;
    }
}
