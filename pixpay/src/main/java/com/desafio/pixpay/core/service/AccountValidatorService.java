package com.desafio.pixpay.core.service;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.identification.IdentificationTypeEnum;
import com.desafio.pixpay.core.gateways.EmailValidatorGateway;
import com.desafio.pixpay.core.gateways.IdentificationValidatorGateway;

public class AccountValidatorService {
    private final IdentificationValidatorGateway identificationValidator;
    private final EmailValidatorGateway emailValidator;

    public AccountValidatorService(IdentificationValidatorGateway identificationValidator, EmailValidatorGateway emailValidator) {
        this.identificationValidator = identificationValidator;
        this.emailValidator = emailValidator;
    }

    public boolean isAccountValid(Account account) {
        boolean isEmailValid = isAccountEmailValid(account.getEmail());
        boolean isIdentificationValid = isAccountIdentificationValid(account.getIdentificationType(), account.getIdentificationNumber());
        return isEmailValid && isIdentificationValid;
    }

    public boolean isAccountEmailValid(String email) {
        return emailValidator.isEmailValid(email);
    }

    public boolean isAccountIdentificationValid(IdentificationTypeEnum identificationType, String identificationCode) {
        if (identificationType.getValue().equals("CadastroDePessoaFisica")) {
            return identificationValidator.isCPFValid(identificationCode);
        } else if (identificationType.getValue().equals("CadastroNacionalDePessoaJuridica")) {
            return identificationValidator.isCNPJValid(identificationCode);
        }
        return false;
    }
}
