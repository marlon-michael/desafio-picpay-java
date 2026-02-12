package com.desafio.pixpay.adapters.controllers;

import java.util.List;
import com.desafio.pixpay.infra.configuration.AccountConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.ListAccountByManagerDTO;
import com.desafio.pixpay.core.usecases.ListAccountsUseCase;
import org.springframework.web.bind.annotation.GetMapping;


@EnableMethodSecurity
@RestController
@RequestMapping("accounts")
public class AccountController {

    @Autowired
    private ListAccountsUseCase listAccountsUseCase;

    AccountController(AccountConfig accountConfig) {
    }

    @Deprecated 
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    @GetMapping()
    public List<ListAccountByManagerDTO> getAccounts(Authentication auth) {
        return listAccountsUseCase
            .execute()
            .stream()
            .map(account -> ListAccountByManagerDTO.fromAccount(account))
            .toList();
    }
    
}
