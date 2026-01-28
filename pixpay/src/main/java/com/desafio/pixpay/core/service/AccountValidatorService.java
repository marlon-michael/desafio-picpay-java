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

    public boolean validateAccount(Account account) {
        boolean isEmailValid = isAccountEmailValid(account.getEmail());
        boolean isIdentificationValid = isAccountIdentificationValid(account.getIdentificationType(), account.getIdentificationNumber());
        if(isEmailValid && isIdentificationValid) {
            account.validate();
        }
        return isEmailValid && isIdentificationValid;
    }

    public boolean isAccountEmailValid(String email) {
        boolean isValid = emailValidator.isEmailValid(email);
        if(!isValid){
            throw new IllegalArgumentException("Invalid email format.");
        }
        return isValid;
    }

    public boolean isAccountIdentificationValid(IdentificationTypeEnum identificationType, String identificationCode) {
        boolean isValid = false;
        if (identificationType.getValue().equals("CadastroDePessoaFisica")) {
            isValid = identificationValidator.isCPFValid(identificationCode);
        } else if (identificationType.getValue().equals("CadastroNacionalDePessoaJuridica")) {
            isValid = identificationValidator.isCNPJValid(identificationCode);
        }
        if(!isValid){
            throw new IllegalArgumentException("Invalid Identification number.");
        }
        return isValid;
    }
}
