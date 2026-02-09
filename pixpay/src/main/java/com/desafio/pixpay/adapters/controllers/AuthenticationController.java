package com.desafio.pixpay.adapters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.SaveAccountDTO;
import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.usecases.CreateAccountUseCase;
import com.desafio.pixpay.core.usecases.input.CreateAccountInput;
import com.desafio.pixpay.infra.security.AuthenticationService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CreateAccountUseCase createAccountUseCase;

    @PostMapping("authenticate")
    public ResponseEntity<String> login(Authentication auth, HttpServletResponse response) {
        Long durationInSeconds = 86400L;
        String token = authenticationService.authenticate(auth);
        ResponseCookie cookie = ResponseCookie.from("jwt-token", token)
            .secure(false) // Em produção, deve ser true
            .httpOnly(true)
            .path("/")
            .maxAge(durationInSeconds)
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().body("Authenticated.");
    }
    
    @PostMapping("signup")
    public ResponseEntity<String> createAccount(@RequestBody SaveAccountDTO createAccountDTO) {
        CreateAccountInput createAccountInput = new CreateAccountInput(
            createAccountDTO.accountType(),
            createAccountDTO.identificationType(),
            createAccountDTO.identificationNumber(),
            createAccountDTO.fullName(),
            createAccountDTO.email(),
            createAccountDTO.password()
        );
        Account createdAccount = createAccountUseCase.execute(createAccountInput);
        return ResponseEntity.status(201).body("Account " + createdAccount.getFullName() + " created successfully.");
    }
    

}
