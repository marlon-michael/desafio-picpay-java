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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Table(name = "transfers")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TransferEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    private Long value;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    private AccountEntity payer;

    @ManyToOne
    @JoinColumn(name = "payee_id")
    private AccountEntity payee;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedAt;

    @Transient
    private boolean isNew = true;

    public TransferEntity(){}

    public TransferEntity(Long value, AccountEntity payer, AccountEntity payee) {
        this.id = UUID.randomUUID();
        this.value = value;
        this.payer = payer;
        this.payee = payee;
        this.isNew = true;
    }

    public TransferEntity(UUID id, Long value, AccountEntity payer, AccountEntity payee) {
        this.id = id;
        this.value = value;
        this.payer = payer;
        this.payee = payee;
        this.isNew = false;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}