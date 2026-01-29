package com.desafio.pixpay.adapters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.SaveAccountDTO;
import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.usecases.CreateAccountUseCase;
import com.desafio.pixpay.infra.security.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CreateAccountUseCase createAccountUseCase;

    @PostMapping("authenticate")
    public String login(Authentication auth) {
        return authenticationService.authenticate(auth);
    }
    
    @PostMapping("signup")
    public ResponseEntity<String> createAccount(@RequestBody SaveAccountDTO saveAccountDTO) {
        Account createdAccount = createAccountUseCase.execute(saveAccountDTO);
        return ResponseEntity.status(201).body("Account " + createdAccount.getFullName() + " created successfully");
    }
    

}
