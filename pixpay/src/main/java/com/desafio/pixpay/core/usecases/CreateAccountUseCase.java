package com.desafio.pixpay.core.usecases;


import com.desafio.pixpay.adapters.dtos.SaveAccountDTO;
import com.desafio.pixpay.core.domain.Account;
import com.desafio.pixpay.core.domain.AccountTypeEnum;
import com.desafio.pixpay.core.domain.identification.IdentificationTypeEnum;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.EmailValidatorGateway;

public class CreateAccountUseCase {
    private final AccountGateway accountGateway;
    private final EmailValidatorGateway emailValidator;

    public CreateAccountUseCase(AccountGateway accountGateway, EmailValidatorGateway emailValidator) {
        this.accountGateway = accountGateway;
        this.emailValidator = emailValidator;
    }

    public Account execute(SaveAccountDTO saveAccountDTO) {
        Account account = new Account(
            AccountTypeEnum.fromValue(saveAccountDTO.accountType()),
            IdentificationTypeEnum.fromValue(saveAccountDTO.identificationType()),
            saveAccountDTO.identificationNumber(),
            saveAccountDTO.fullName(),
            saveAccountDTO.email(),
            saveAccountDTO.password(),
            emailValidator
        );
        accountGateway.saveAccount(account);
        return account;
    }
}
