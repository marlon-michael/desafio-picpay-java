package com.desafio.pixpay.adapters.dtos;


import java.util.UUID;

import com.desafio.pixpay.core.domain.transfer.Transfer;

public record TransferDTO( 
    UUID id,
    Double value,
    UUID payer,
    UUID payee
){
    public static TransferDTO fromDomain(Transfer transfer){
        return new TransferDTO(
            transfer.getId(),
            transfer.getValue().getMoneyInCurrency(),
            transfer.getPayer().getId(),
            transfer.getPayee().getId()
        );
    }


}