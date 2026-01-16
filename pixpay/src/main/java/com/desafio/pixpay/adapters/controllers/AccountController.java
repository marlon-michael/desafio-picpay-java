package com.desafio.pixpay.adapters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.SaveAccountDTO;
import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.usecases.CreateAccountUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/accounts")
public class AccountController {
    
    @Autowired
    private CreateAccountUseCase createAccountUseCase;

    
    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody SaveAccountDTO saveAccountDTO) {
        Account createdAccount = createAccountUseCase.execute(saveAccountDTO);
        return ResponseEntity.status(201).body("Account " + createdAccount.getFullName() + " created successfully");
    }
}
