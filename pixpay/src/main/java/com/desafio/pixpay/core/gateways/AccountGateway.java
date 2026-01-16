package com.desafio.pixpay.core.gateways;

import com.desafio.pixpay.core.domain.account.Account;

public interface AccountGateway {
    void saveAccount(Account account);
}
