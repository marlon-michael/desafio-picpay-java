package com.desafio.pixpay.infra.persistence.entity;

import java.util.UUID;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public AccountEntity getPayer() {
        return payer;
    }

    public void setPayer(AccountEntity payer) {
        this.payer = payer;
    }

    public AccountEntity getPayee() {
        return payee;
    }

    public void setPayee(AccountEntity payee) {
        this.payee = payee;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}