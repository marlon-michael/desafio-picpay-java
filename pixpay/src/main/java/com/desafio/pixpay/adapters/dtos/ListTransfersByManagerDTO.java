package com.desafio.pixpay.adapters.dtos;

import java.util.UUID;

import com.desafio.pixpay.core.domain.transfer.Transfer;

public record ListTransfersByManagerDTO(
    UUID idDaTransacao,
    String payer,
    String payee,
    Double value
) {
    public static ListTransfersByManagerDTO fromDomain(Transfer transfer){
        return new ListTransfersByManagerDTO(
            transfer.getId(),
            transfer.getPayer().getIdentification().getIdentificationNumber(),
            transfer.getPayee().getIdentification().getIdentificationNumber(),
            transfer.getValue().getMoneyInCurrency()
        );
    }    
}
