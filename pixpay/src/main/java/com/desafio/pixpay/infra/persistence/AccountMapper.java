package com.desafio.pixpay.infra.persistence;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.domain.identification.IdentificationTypeEnum;
import com.desafio.pixpay.core.service.AccountValidatorService;

public class AccountMapper {
    private static AccountValidatorService accountValidatorService;

    public AccountMapper(AccountValidatorService accountValidatorService) {
        AccountMapper.accountValidatorService = accountValidatorService;
    }

    public AccountEntity fromDomainToEntity(Account account) {
        if (!accountValidatorService.validateAccount(account)){
            throw  new IllegalArgumentException("Invalid account");
        }
        AccountEntity accountEntity = new AccountEntity(
            account.getId(),
            account.getAccountType().getValue(),
            account.getIdentificationType().getValue(),
            account.getIdentificationNumber(),
            account.getFullName(),
            account.getEmail(),
            account.getPassword(),
            account.getBalanceInPipsOfReal()
        );
        return accountEntity;
    }

    public Account fromEntityToDomain(AccountEntity accountEntity) {
        Account account = new Account(
            accountEntity.getId(),
            AccountTypeEnum.fromValue(accountEntity.getAccountType()),
            IdentificationTypeEnum.fromValue(accountEntity.getIdentificationType()),
            accountEntity.getIdentificationNumber(),
            accountEntity.getFullName(),
            accountEntity.getEmail(),
            accountEntity.getPassword(),
            accountEntity.getBalanceInPipsOfReal()
        );
        return account;
    }
}
