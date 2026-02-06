package com.desafio.pixpay.core.domain.transfer;

import java.util.UUID;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.money.Money;


public class Transfer {
    private UUID id;
    private Money value;
    private Account payer;
    private Account payee;

    public Transfer(){}

    public Transfer(Money value, Account payer, Account payee) {
        this.id = UUID.randomUUID();
        this.value = value;
        this.payer = payer;
        this.payee = payee;
    }

    public Transfer(UUID id, Money value, Account payer, Account payee) {
        this.id = id;
        this.value = value;
        this.payer = payer;
        this.payee = payee;
    }
    
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public Money getValue() {
        return value;
    }
    public void setValue(Money value) {
        this.value = value;
    }
    public Account getPayer() {
        return payer;
    }
    public void setPayer(Account payer) {
        this.payer = payer;
    }
    public Account getPayee() {
        return payee;
    }
    public void setPayee(Account payee) {
        this.payee = payee;
    }

    
}