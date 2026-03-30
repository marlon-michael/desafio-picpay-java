package com.desafio.pixpay.core.usecases;

import java.util.List;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.gateways.AccountGateway;

public class ListAccountsByManagerUseCase {

    private final AccountGateway accountGateway;

    public ListAccountsByManagerUseCase(AccountGateway accountGateway){
        this.accountGateway = accountGateway;
    }

    public List<Account> execute(Integer pageSize, Integer pageNumber){
        return accountGateway.findAll(pageSize, pageNumber);
    }
}
