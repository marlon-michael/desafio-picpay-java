package com.desafio.pixpay.adapters.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.infra.persistence.entity.AccountEntity;
import com.desafio.pixpay.infra.persistence.repository.AccountRepository;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Deprecated 
    @GetMapping()
    public List<AccountEntity> getAccounts() {
        return accountRepository.findAll();
    }
    
}
