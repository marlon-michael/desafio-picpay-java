package com.desafio.pixpay.core.gateways;

import com.desafio.pixpay.core.domain.Account;

public interface AccountGateway {
    void saveAccount(Account account);
}
