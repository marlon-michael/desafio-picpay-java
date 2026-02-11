package com.desafio.pixpay.infra.persistence.mapper;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.FullName;
import com.desafio.pixpay.core.domain.account.Password;
import com.desafio.pixpay.core.domain.email.Email;
import com.desafio.pixpay.core.domain.identification.IdentificationFactory;
import com.desafio.pixpay.core.domain.identification.IdentificationTypeEnum;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.infra.persistence.entity.AccountEntity;

public class AccountMapper {

    public static AccountEntity fromDomainToEntity(Account account) {
        AccountEntity accountEntity = new AccountEntity(
            account.getId(),
            account.getRoles(),
            account.getAccountType().getValue(),
            account.getIdentificationType().getValue(),
            account.getIdentification().getIdentificationNumber(),
            account.getFullName().getFullName(),
            account.getEmail().getValue(),
            account.getPassword().getValue(),
            account.getBalanceInPipsOfReal()
        );
        return accountEntity;
    }

    public static Account fromEntityToDomain(AccountEntity accountEntity) {
        Account account = new Account(
            accountEntity.getId(),
            accountEntity.getRoles(),
            IdentificationFactory.createIdentification(
                IdentificationTypeEnum.fromValue(
                    accountEntity.getIdentificationType()
                ), 
                accountEntity.getIdentificationNumber()
            ),
            new FullName().fromPersistence(accountEntity.getFullName()),
            new Email().fromPersistence(accountEntity.getEmail()),
            new Password().fromPersistence(accountEntity.getPassword()),
            new Money(accountEntity.getBalanceInPipsOfReal())
        );
        return account;
    }
}
