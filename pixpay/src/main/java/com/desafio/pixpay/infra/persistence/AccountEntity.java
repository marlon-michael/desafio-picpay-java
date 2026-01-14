package com.desafio.pixpay.infra.persistence;

import java.util.UUID;

import com.desafio.pixpay.core.domain.Account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "accounts")
@Entity
public class AccountEntity {
    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String identificationNumber;
    String fullName;
    String email;
    String password;
    Long balanceInPipsOfReal;

    public static AccountEntity fromDomainToEntity(Account account) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(account.getId());
        accountEntity.setIdentificationNumber(account.getIdentificationNumber());
        accountEntity.setFullName(account.getFullName());
        accountEntity.setEmail(account.getEmail());
        accountEntity.setPassword(account.getPassword());
        accountEntity.setBalanceInPipsOfReal(account.getBalanceInPipsOfReal());
        return accountEntity;
    }

    public static Account fromEntityToDomain(AccountEntity accountEntity) {
        Account account = new Account();
        account.setId(accountEntity.getId());
        account.setIdentificationNumber(accountEntity.getIdentificationNumber());
        account.setFullName(accountEntity.getFullName());
        account.setEmail(accountEntity.getEmail());
        account.setPassword(accountEntity.getPassword());
        account.setBalanceInPipsOfReal(accountEntity.getBalanceInPipsOfReal());
        return account;
    }
}
