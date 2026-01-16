package com.desafio.pixpay.core.gateways;

public interface IdentificationValidator {
    boolean isCPFValid(String cpf);
    boolean isCNPJValid(String cnpj);
}   
