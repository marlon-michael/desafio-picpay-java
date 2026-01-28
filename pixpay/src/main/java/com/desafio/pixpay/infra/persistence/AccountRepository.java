package com.desafio.pixpay.infra.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.gateways.AccountGateway;

@Repository
public class AccountRepository implements AccountGateway {
    private final JpaAccountRepository jpaAccountRepository;
    private final AccountMapper accountMapper;

    public AccountRepository(JpaAccountRepository jpaAccountRepository, AccountMapper accountMapper) {
        this.jpaAccountRepository = jpaAccountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public void saveAccount(Account account) {
        jpaAccountRepository.save(accountMapper.fromDomainToEntity(account));
    }

    @Override
    public Account findAccountById(UUID id) {
        Optional<AccountEntity> optionalAccount = jpaAccountRepository.findById(id);
        if(optionalAccount.isEmpty()){
            throw new IllegalArgumentException("The account was not found using the provided UUID.");
        }
        
        AccountEntity account = optionalAccount.get();
        return accountMapper.fromEntityToDomain(account);
    }
    
}
