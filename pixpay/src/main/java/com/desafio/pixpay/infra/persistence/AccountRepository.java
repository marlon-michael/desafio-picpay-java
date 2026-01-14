package com.desafio.pixpay.infra.persistence;

import org.springframework.stereotype.Repository;

import com.desafio.pixpay.core.domain.Account;
import com.desafio.pixpay.core.gateways.AccountGateway;

@Repository
public class AccountRepository implements AccountGateway {
    private final JpaAccountRepository jpaAccountRepository;

    public AccountRepository(JpaAccountRepository jpaAccountRepository) {
        this.jpaAccountRepository = jpaAccountRepository;
    }

    @Override
    public void saveAccount(Account account) {
        jpaAccountRepository.save(AccountEntity.fromDomainToEntity(account));
    }
    
}
