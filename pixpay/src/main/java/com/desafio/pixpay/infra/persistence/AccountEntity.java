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

    public AccountEntity(UUID id, String identificationNumber, String fullName, String email, String password, Long balanceInPipsOfReal) {
        this.id = id;
        this.identificationNumber = identificationNumber;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.balanceInPipsOfReal = balanceInPipsOfReal;

    }

    public static AccountEntity fromDomainToEntity(Account account) {
        AccountEntity accountEntity = new AccountEntity(
            account.getId(),
            account.getIdentificationNumber(),
            account.getFullName(),
            account.getEmail(),
            account.getPassword(),
            account.getBalanceInPipsOfReal()
        );
        return accountEntity;
    }

    public static Account fromEntityTo1Domain(AccountEntity accountEntity) {
        Account account = new Account(
            accountEntity.getId(),
            accountEntity.getIdentificationNumber(),
            accountEntity.getFullName(),
            accountEntity.getEmail(),
            accountEntity.getPassword(),
            accountEntity.getBalanceInPipsOfReal()
        );
        return account;
    }
}
