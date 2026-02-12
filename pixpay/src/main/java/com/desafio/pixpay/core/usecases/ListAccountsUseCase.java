package com.desafio.pixpay.core.usecases;

import java.util.List;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.gateways.AccountGateway;

public class ListAccountsUseCase {

    private final AccountGateway accountGateway;

    public ListAccountsUseCase(AccountGateway accountGateway){
        this.accountGateway = accountGateway;
    }

    public List<Account> execute(){
        return accountGateway.findAll();
    }
}
