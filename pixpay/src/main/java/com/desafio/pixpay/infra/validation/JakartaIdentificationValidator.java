package com.desafio.pixpay.infra.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.constraints.br.CPF;

import com.desafio.pixpay.core.gateways.IdentificationValidatorGateway;

import org.hibernate.validator.constraints.br.CNPJ;

public class JakartaIdentificationValidator implements IdentificationValidatorGateway {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public boolean isCpfValid(String cpf) {
        return validator.validateValue(DataTransferObject.class, "cpf", cpf).isEmpty();
    }

    @Override
    public boolean isCnpjValid(String cnpj) {
        return validator.validateValue(DataTransferObject.class, "cnpj", cnpj).isEmpty();
    }

    @Override
    public boolean isAlfaNumCnpjValid(String cnpj) {
        if (!isAlfaNumCnpjValiad(cnpj)) return false;
        return true;
    }

    private boolean isAlfaNumCnpjValiad(String cnpj) {
        int[] PESOS_1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] PESOS_2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        if (cnpj == null || !cnpj.matches("^[A-Z0-9]{12}[0-9]{2}$")) return false;

        int dv1 = calculateDigit(cnpj.substring(0, 12), PESOS_1);
        int dv2 = calculateDigit(cnpj.substring(0, 12) + dv1, PESOS_2);

        return cnpj.endsWith(String.valueOf(dv1) + dv2);
    }

    private int calculateDigit(String base, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            // Converte char para valor decimal (ASCII - 48) conforme regra RFB
            int valor = base.charAt(i) - 48; 
            soma += valor * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    // Classe auxiliar apenas para referenciar as regras brasileiras
    private static class DataTransferObject {
        @CPF String cpf;
        @CNPJ String cnpj;
    }
}
