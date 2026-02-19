package com.desafio.pixpay.core.gateways;

public interface IdentificationValidatorGateway {
    boolean isCpfValid(String cpf);
    boolean isCnpjValid(String cpf);
    boolean isAlfaNumCnpjValid(String cnpj);
}   
