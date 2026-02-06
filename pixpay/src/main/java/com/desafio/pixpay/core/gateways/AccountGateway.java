package com.desafio.pixpay.core.gateways;

import java.util.UUID;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.money.Money;

public interface AccountGateway {
    void saveAccount(Account account);
    Account findAccountById(UUID id);
    int updateAccountBalanceById(UUID id, Money money);
}
