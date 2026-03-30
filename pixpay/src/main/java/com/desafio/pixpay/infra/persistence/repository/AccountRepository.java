package com.desafio.pixpay.infra.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.infra.persistence.entity.AccountEntity;
import com.desafio.pixpay.infra.persistence.jpa.JpaAccountRepository;
import com.desafio.pixpay.infra.persistence.mapper.AccountMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Repository
public class AccountRepository implements AccountGateway {

    private final RedisTemplate<String, String> redis;
    private final ObjectMapper objectMapper;
    private final JpaAccountRepository jpaAccountRepository;

    @Value("${app.pagination.default-size}")
    private Integer DEFAULT_PAGE_SIZE;

    public AccountRepository(JpaAccountRepository jpaAccountRepository, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.jpaAccountRepository = jpaAccountRepository;
        this.redis = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void create(Account account) {
        redis.delete("accounts:page:0");
        jpaAccountRepository.save(AccountMapper.fromDomainToEntity(account));
    }

    @Override
    public int updateBalanceById(UUID id, Money money) {
        redis.delete("accounts:page:0");
        redis.delete("account:"+id);
        Long balance = money.getMoneyInCents();
        return jpaAccountRepository.updateAccountBalanceById(id, balance);
    }

    @Override
    public List<Account> findAll(Integer size, Integer page) {
        List<AccountEntity> accountsFromPersistence;
        List<Account> accounts;
        
        if (size == DEFAULT_PAGE_SIZE && page == 0){
            String json = redis.opsForValue().get("accounts:page:0");
            if (json != null) {
                try {
                    accountsFromPersistence = objectMapper.readValue(json, new TypeReference<List<AccountEntity>>() {});
                    accounts = accountsFromPersistence
                        .stream()
                        .map(entity -> AccountMapper.fromEntityToDomain(entity))
                        .toList();
                    return accounts;
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }

        PageRequest pagination = PageRequest.of(page, size);
        accountsFromPersistence = jpaAccountRepository.findAll(pagination).toList();
        accounts = accountsFromPersistence
            .stream()
            .map(entity -> AccountMapper.fromEntityToDomain(entity))
            .toList();

        if (size == DEFAULT_PAGE_SIZE && page == 0){
            try {
                redis.opsForValue().set("accounts:page:0", objectMapper.writeValueAsString(accountsFromPersistence));
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }

        return accounts;
    }

    @Override
    public Account findById(UUID id) {
        AccountEntity account;
        String json = redis.opsForValue().get("account:"+id);

        if (json != null) {
            try {
                account = objectMapper.readValue(json, AccountEntity.class);
                return AccountMapper.fromEntityToDomain(account);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }

        Optional<AccountEntity> optionalAccount = jpaAccountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            return null;
        }
        account = optionalAccount.get();
        setCacheForAccount(account);

        return AccountMapper.fromEntityToDomain(account);
    }

    @Override
    public Account findByEmail(String email) {
        AccountEntity account;
        String json;
        String accountId = redis.opsForValue().get("account:"+email);

        if (accountId != null) {
            try {
                json = redis.opsForValue().get("account:"+accountId);
                account = objectMapper.readValue(json, AccountEntity.class);
                return AccountMapper.fromEntityToDomain(account);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }

        account = jpaAccountRepository.findByEmail(email);
        if (account == null) return null;
        setCacheForAccount(account);
        return AccountMapper.fromEntityToDomain(account);
    }

    @Override
    public Account findByIdentificationNumber(String identificationNumber) {
        AccountEntity account;
        String json = redis.opsForValue().get("account:"+identificationNumber);

        if (json != null) {
            try {
                account = objectMapper.readValue(json, AccountEntity.class);
                return AccountMapper.fromEntityToDomain(account);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }

        account = jpaAccountRepository.findByIdentificationNumber(identificationNumber);
        if (account == null) return null;

        setCacheForAccount(account);
        return AccountMapper.fromEntityToDomain(account);
    }

    private void setCacheForAccount(AccountEntity accountEntity){
        try {
            redis.opsForValue().set("account:"+accountEntity.getId(), objectMapper.writeValueAsString(accountEntity));
            redis.opsForValue().set("account:"+accountEntity.getIdentificationNumber(), accountEntity.getId().toString());
            redis.opsForValue().set("account:"+accountEntity.getEmail(), accountEntity.getId().toString());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

}
