package com.desafio.pixpay.adapters.dtos;

public record SaveAccountDTO(
    String accountType,
    String identificationType,
    String identificationNumber,
    String fullName,
    String email,
    String password
) {
}