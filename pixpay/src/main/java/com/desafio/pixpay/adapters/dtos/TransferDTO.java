package com.desafio.pixpay.adapters.dtos;


import java.util.UUID;

public record TransferDTO( 
    Double value,
    UUID payer,
    UUID payee
){}