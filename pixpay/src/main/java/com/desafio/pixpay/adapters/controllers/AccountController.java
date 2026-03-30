package com.desafio.pixpay.adapters.controllers;

import java.util.List;
import com.desafio.pixpay.infra.configuration.AccountConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.ListAccountByManagerDTO;
import com.desafio.pixpay.core.usecases.ListAccountsByManagerUseCase;
import org.springframework.web.bind.annotation.GetMapping;


@EnableMethodSecurity
@RestController
@RequestMapping("accounts")
@Tag(name = "Accounts", description = "Account management endpoint")
public class AccountController {

    @Autowired
    private ListAccountsByManagerUseCase listAccountsUseCase;

    AccountController(AccountConfig accountConfig) {
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    @Operation(summary = "list all Accounts", description = "List of accounts to be seen by a manager")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of accounts returned successfully", 
            content = @Content(
                mediaType = "application/json", 
                array = @ArraySchema(schema = @Schema(implementation = ListAccountByManagerDTO.class))
            )
        ),
        @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
        @ApiResponse(responseCode = "403", description = "User doesn't have access to this method", content = @Content)
    })
    public ResponseEntity<List<ListAccountByManagerDTO>> listAccountsByManager(
        @RequestParam(name = "size", defaultValue = "25") Integer pageSize,
        @RequestParam(name = "page", defaultValue = "0") Integer pageNumber
    ) {
        return ResponseEntity.ok().body(listAccountsUseCase
            .execute(pageSize, pageNumber)
            .stream()
            .map(account -> ListAccountByManagerDTO.fromAccount(account))
            .toList()
        );
    }
    
}
