package com.desafio.pixpay.infra.persistence.entity;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Table(name = "accounts")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity implements Persistable<UUID> {

    @Id()
    private UUID id;
    private String accountType;
    private String identificationType;
    private String identificationNumber;
    private String fullName;
    private String email;
    private String password;
    private Long balanceInPipsOfReal;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant lastModifiedAt;

    @Transient
    private boolean isNew;

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

    @Override
    public boolean isNew() {
        return this.isNew;
    }
}
