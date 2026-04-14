package com.desafio.pixpay.adapters.dtos;

import java.util.UUID;

import com.desafio.pixpay.core.domain.transfer.Transfer;

public record TransfersDTO(
    UUID idDaTransacao,
    String payer,
    String payee,
    Double value
) {
    public static TransfersDTO fromDomain(Transfer transfer){
        return new TransfersDTO(
            transfer.getId(),
            transfer.getPayer().getIdentification().getIdentificationNumber(),
            transfer.getPayee().getIdentification().getIdentificationNumber(),
            transfer.getValue().getMoneyInReal()
        );
    }    
}
