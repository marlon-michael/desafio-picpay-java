package com.desafio.pixpay.core.gateways;

public interface IdentificationValidatorGateway {
    boolean isCPFValid(String cpf);
    boolean isCNPJValid(String cnpj);
}   
