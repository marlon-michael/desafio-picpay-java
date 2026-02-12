package com.desafio.pixpay.core.gateways;

import java.util.List;
import java.util.UUID;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.money.Money;

public interface AccountGateway {
    void create(Account account);
    List<Account> findAll();
    Account findById(UUID id);
    Account findByIdentificationNumber(String identificationNumber);
    int updateBalanceById(UUID id, Money money);
}
