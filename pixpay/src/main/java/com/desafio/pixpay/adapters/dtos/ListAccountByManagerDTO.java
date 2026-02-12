package com.desafio.pixpay.adapters.dtos;

import java.util.List;

import com.desafio.pixpay.core.domain.account.Account;

public record ListAccountByManagerDTO(
        List<String> roles,
        String accountType,
        String identificationType,
        String identificationNumber,
        String fullName,
        String email,
        Double balanceInReal,
        String createdAt,
        String lastModifiedAt) {
    public static ListAccountByManagerDTO fromAccount(Account account) {
        return new ListAccountByManagerDTO(
                account.getRoles().stream().map(role -> role.getValue()).toList(),
                account.getAccountType().getValue(),
                account.getIdentificationType().getValue(),
                account.getIdentification().getIdentificationNumber(),
                account.getFullName().getFullName(),
                account.getEmail().getValue(),
                account.getBalanceInReal(),
                account.getCreatedAt().toString(),
                account.getLastModifiedAt().toString()
            );
    }
}
