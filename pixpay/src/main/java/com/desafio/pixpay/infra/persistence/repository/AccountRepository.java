package com.desafio.pixpay.infra.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.infra.persistence.entity.AccountEntity;
import com.desafio.pixpay.infra.persistence.jpa.JpaAccountRepository;
import com.desafio.pixpay.infra.persistence.mapper.AccountMapper;

@Repository
public class AccountRepository implements AccountGateway {
    private final JpaAccountRepository jpaAccountRepository;

    public AccountRepository(JpaAccountRepository jpaAccountRepository) {
        this.jpaAccountRepository = jpaAccountRepository;
    }

    @Override
    public void createAccount(Account account) {
        jpaAccountRepository.save(AccountMapper.fromDomainToEntity(account));
    }

    @Override
    public int updateAccountBalanceById(UUID id, Money money){
        Long balance = money.getMoneyInPips();
        return jpaAccountRepository.updateAccountBalanceById(id, balance);
    }


    @Override
    public Account findAccountById(UUID id) {
        Optional<AccountEntity> optionalAccount = jpaAccountRepository.findById(id);
        if(optionalAccount.isEmpty()){
            throw new IllegalArgumentException("The account was not found with provided UUID.");
        }
        
        AccountEntity account = optionalAccount.get();
        return AccountMapper.fromEntityToDomain(account);
    }

    public AccountEntity findAccountByEmail(String email){
        return jpaAccountRepository.findByEmail(email);
    }

    public List<AccountEntity> findAll() {
        return jpaAccountRepository.findAll();
    }

    @Override
    public Account findAccountByIdentificationNumber(String identificationNumber) {
        Optional<AccountEntity> optional = Optional.ofNullable(jpaAccountRepository.findByIdentificationNumber(identificationNumber));
        if (optional.isEmpty()) return null;
        return AccountMapper.fromEntityToDomain(optional.get());
    }
    
}
