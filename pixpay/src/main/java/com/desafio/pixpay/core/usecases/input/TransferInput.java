package com.desafio.pixpay.core.usecases.input;

import java.util.UUID;


public class TransferInput {
    private Double value;
    private UUID payer;
    private UUID payee;

    public TransferInput(Double value, UUID payer, UUID payee){
        this.value = value;
        this.payer = payer;
        this.payee = payee;
    }

    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    public UUID getPayer() {
        return payer;
    }
    public void setPayer(UUID payer) {
        this.payer = payer;
    }
    public UUID getPayee() {
        return payee;
    }
    public void setPayee(UUID payee) {
        this.payee = payee;
    }

    
}
