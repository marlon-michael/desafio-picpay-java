package com.desafio.pixpay.adapters.dtos;


import java.util.UUID;

import com.desafio.pixpay.core.usecases.data.TransferData;

public record TransferInputDTO( 
    UUID id,
    Double value,
    UUID payer,
    UUID payee
){
    public static TransferInputDTO fromDomain(TransferData transfer){
        return new TransferInputDTO(
            transfer.getId(),
            transfer.getValue(),
            transfer.getPayer(),
            transfer.getPayee()
        );
    }


}