package com.desafio.pixpay.adapters.dtos;

import java.util.UUID;

import com.desafio.pixpay.core.domain.transfer.Transfer;

public record TransferOutputDTO(
    UUID idDaTransacao,
    String payer,
    String payee,
    Double value
) {
    public static TransferOutputDTO fromDomain(Transfer transfer){
        return new TransferOutputDTO(
            transfer.getId(),
            transfer.getPayer().getIdentification().getIdentificationNumber(),
            transfer.getPayee().getIdentification().getIdentificationNumber(),
            transfer.getValue().getMoneyInReal()
        );
    }    
}
