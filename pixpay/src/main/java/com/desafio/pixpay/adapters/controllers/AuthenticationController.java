package com.desafio.pixpay.adapters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.AuthenticationDTO;
import com.desafio.pixpay.adapters.dtos.SaveAccountDTO;
import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.usecases.CreateAccountUseCase;
import com.desafio.pixpay.core.usecases.data.CreateAccountInput;
import com.desafio.pixpay.infra.security.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Tag(name = "Auth", description = "Authentication and signup endpoint")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CreateAccountUseCase createAccountUseCase;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("authenticate")
    @Operation(summary = "Perform login", description = "Perform login by body with authenticationDTO")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Authenticated successfully", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized: wrong login or password", content = @Content)
    })
    public ResponseEntity<String> login(@RequestBody AuthenticationDTO auth, HttpServletResponse response) {

        Long durationInSeconds = 86400L;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.username(), auth.password()));
        String token = authenticationService.authenticate(authentication);
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
    
    @Operation(summary = "Perform signup", description = "Perform the creation of account and user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sign up performed successfully", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data / Data field missing / Data already exists", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
    })
    @PostMapping("signup")
    public ResponseEntity<String> createAccount(@RequestBody SaveAccountDTO createAccountDTO) {
        CreateAccountInput createAccountInput = new CreateAccountInput(
            createAccountDTO.identificationType(),
            createAccountDTO.identificationNumber(),
            createAccountDTO.fullName(),
            createAccountDTO.email(),
            createAccountDTO.password()
        );
        Account createdAccount = createAccountUseCase.execute(createAccountInput);
        return ResponseEntity.status(201).body("Account " + createdAccount.getFullName().getFullName() + " created successfully.");
    }
    

}
