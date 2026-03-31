package com.desafio.pixpay.core.usecases;

import java.util.UUID;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.gateways.AccountGateway;

public class findAccountByIdUseCase {
    private final AccountGateway accountGateway;

    public findAccountByIdUseCase(AccountGateway accountGateway){
        this.accountGateway = accountGateway;
    }

    public Account execute(UUID id){
        Account account = accountGateway.findById(id);

        if (account == null){
            throw new BusinessException("Account not found.");
        }

        return account;
    }
    
}
