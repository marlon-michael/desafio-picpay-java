package com.desafio.pixpay.core.usecases;


import com.desafio.pixpay.core.domain.Account;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.EmailValidatorGateway;

public class CreateAccountUseCase {
    private final AccountGateway accountGateway;
    private final EmailValidatorGateway emailValidator;

    public CreateAccountUseCase(AccountGateway accountGateway, EmailValidatorGateway emailValidator) {
        this.accountGateway = accountGateway;
        this.emailValidator = emailValidator;
    }

    public Account execute(Account accountRequest) {
        Account account = new Account(
            accountRequest.getId(),
            accountRequest.getIdentificationNumber(),
            accountRequest.getFullName(),
            accountRequest.getEmail(),
            accountRequest.getPassword(),
            accountRequest.getBalanceInPipsOfReal(),
            emailValidator
        );
        accountGateway.saveAccount(account);
        return account;
    }
}
