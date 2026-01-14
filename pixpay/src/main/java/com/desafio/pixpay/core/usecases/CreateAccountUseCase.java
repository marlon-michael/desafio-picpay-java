package com.desafio.pixpay.core.usecases;


import com.desafio.pixpay.core.domain.Account;
import com.desafio.pixpay.core.gateways.AccountGateway;

public class CreateAccountUseCase {
    private AccountGateway accountGateway;

    public CreateAccountUseCase(AccountGateway accountGateway) {
        this.accountGateway = accountGateway;
    }

    public Account execute(Account account){
        accountGateway.saveAccount(account);
        return account;
    }
}
