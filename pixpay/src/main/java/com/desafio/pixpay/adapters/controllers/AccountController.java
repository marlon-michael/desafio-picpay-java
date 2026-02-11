package com.desafio.pixpay.adapters.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.infra.persistence.entity.AccountEntity;
import com.desafio.pixpay.infra.persistence.repository.AccountRepository;

import org.springframework.web.bind.annotation.GetMapping;


@EnableMethodSecurity
@RestController
@RequestMapping("accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Deprecated 
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    @GetMapping()
    public List<AccountEntity> getAccounts(Authentication auth) {
        return accountRepository.findAll();
    }
    
}
