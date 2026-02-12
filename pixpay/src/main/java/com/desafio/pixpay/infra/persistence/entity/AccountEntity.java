package com.desafio.pixpay.infra.persistence.entity;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.desafio.pixpay.core.domain.account.Role;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
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

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "account_roles", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "role")
    private Set<Role> roles;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedAt;

    @Transient
    private boolean isNew;

    public AccountEntity(){}

    public AccountEntity(UUID id, Set<Role> roles, String accountType, String identificationType, String identificationNumber, String fullName, String email, String password, Long balanceInPipsOfReal) {
        this.id = id;
        this.roles = roles;
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
