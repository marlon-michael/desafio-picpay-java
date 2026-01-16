package com.desafio.pixpay.infra.persistence;

import java.util.UUID;

import com.desafio.pixpay.core.domain.Account;
import com.desafio.pixpay.core.domain.AccountTypeEnum;
import com.desafio.pixpay.core.domain.identification.IdentificationTypeEnum;

import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "accounts")
@Entity
public class AccountEntity {
    @Id()
    //@GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String accountType;
    String identificationType;
    String identificationNumber;
    String fullName;
    String email;
    String password;
    Long balanceInPipsOfReal;

    public AccountEntity(){}

    public AccountEntity(UUID id, String accountType, String identificationType, String identificationNumber, String fullName, String email, String password, Long balanceInPipsOfReal) {
        this.id = id;
        this.accountType = accountType;
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.balanceInPipsOfReal = balanceInPipsOfReal;
    }

    public static AccountEntity fromDomainToEntity(Account account) {
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

    public static Account fromEntityTo1Domain(AccountEntity accountEntity) {
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
