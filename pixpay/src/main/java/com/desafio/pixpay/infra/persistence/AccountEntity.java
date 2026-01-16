package com.desafio.pixpay.infra.persistence;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "accounts")
@Entity
public class AccountEntity {
    @Id()
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
}
