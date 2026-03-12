package com.desafio.pixpay.core.usecases.data;

import java.util.UUID;


public class TransferData {
    private UUID id;
    private Double value;
    private UUID payer;
    private UUID payee;

    public TransferData(){};

    public TransferData(Double value, UUID payer, UUID payee){
        this.value = value;
        this.payer = payer;
        this.payee = payee;
    }

    public TransferData(UUID id, Double value, UUID payer, UUID payee){
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
