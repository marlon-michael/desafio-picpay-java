package com.desafio.pixpay.core.usecases;


import com.desafio.pixpay.adapters.dtos.SaveAccountDTO;
import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.domain.identification.IdentificationTypeEnum;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.service.AccountValidatorService;

public class CreateAccountUseCase {
    private final AccountGateway accountGateway;
    private final AccountValidatorService accountValidatorService;

    public CreateAccountUseCase(AccountGateway accountGateway, AccountValidatorService accountValidatorService) {
        this.accountGateway = accountGateway;
        this.accountValidatorService = accountValidatorService;
    }

    public Account execute(SaveAccountDTO saveAccountDTO) {
        Account account = new Account(
            AccountTypeEnum.fromValue(saveAccountDTO.accountType()),
            IdentificationTypeEnum.fromValue(saveAccountDTO.identificationType()),
            saveAccountDTO.identificationNumber(),
            saveAccountDTO.fullName(),
            saveAccountDTO.email(),
            saveAccountDTO.password()
        );
        accountValidatorService.isAccountValid(account);
        accountGateway.saveAccount(account);
        return account;
    }
}
