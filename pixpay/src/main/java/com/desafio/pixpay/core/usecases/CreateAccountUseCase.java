package com.desafio.pixpay.core.usecases;


import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.domain.identification.IdentificationTypeEnum;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.PasswordEncoderGateway;
import com.desafio.pixpay.core.service.AccountValidatorService;
import com.desafio.pixpay.core.usecases.input.CreateAccountInput;


public class CreateAccountUseCase {
    private final AccountGateway accountGateway;
    private final AccountValidatorService accountValidatorService;
    private final PasswordEncoderGateway passwordEncoder;

    public CreateAccountUseCase(AccountGateway accountGateway, AccountValidatorService accountValidatorService, PasswordEncoderGateway passwordEncoder) {
        this.accountGateway = accountGateway;
        this.accountValidatorService = accountValidatorService;
        this.passwordEncoder = passwordEncoder;
    }

    public Account execute(CreateAccountInput createAccountInput) {
        Account account = new Account(
            AccountTypeEnum.fromValue(createAccountInput.getAccountType()),
            IdentificationTypeEnum.fromValue(createAccountInput.getIdentificationType()),
            createAccountInput.getIdentificationNumber(),
            createAccountInput.getFullName(),
            createAccountInput.getEmail(),
            createAccountInput.getPassword(),
            passwordEncoder
        );
        accountValidatorService.validateAccount(account);
        if(!account.isValidated()) {
            throw new IllegalArgumentException("Account coudn't be validated, check all fields and try again.");
        }

        accountGateway.createAccount(account);
        return account;
    }
}
