package com.desafio.pixpay.core.usecases;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.gateways.AccountGateway;

public class FindAccountByIdentificationNumberUseCase {
    private final AccountGateway accountGateway;

    public FindAccountByIdentificationNumberUseCase(AccountGateway accountGateway){
        this.accountGateway = accountGateway;
    }

    public Account execute(String identificationNumber){
        Account account = accountGateway.findByIdentificationNumber(identificationNumber);

        if (account == null){
            throw new BusinessException("Account not found.");
        }

        return account;
    }
    
}
