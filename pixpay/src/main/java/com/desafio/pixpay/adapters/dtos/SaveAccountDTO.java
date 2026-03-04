package com.desafio.pixpay.adapters.dtos;

public record SaveAccountDTO(
    String identificationType,
    String identificationNumber,
    String fullName,
    String email,
    String password
) {
}