package com.desafio.pixpay.adapters.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.ListTransfersByManagerDTO;
import com.desafio.pixpay.adapters.dtos.TransferDTO;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.usecases.ListTransfersByManagerUseCase;
import com.desafio.pixpay.core.usecases.RefundTransferUsecase;
import com.desafio.pixpay.core.usecases.RequestTransferUsecase;
import com.desafio.pixpay.core.usecases.data.TransferData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@EnableMethodSecurity
@RequestMapping("transfer")
@Tag(name = "Transfer", description = "Transfers endpoint")
public class TransferController {

    @Autowired
    RequestTransferUsecase requestTransferUseCase;

    @Autowired
    ListTransfersByManagerUseCase listTransfersByManager;

    @Autowired
    RefundTransferUsecase refundTransferUsecase;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    @Operation(summary = "Lista all transfers", description = "List all transfers to be seen by a manager")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfers returned successfully", 
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(arraySchema = @Schema(implementation = ListTransfersByManagerDTO.class))
        )),
        @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
        @ApiResponse(responseCode = "403", description = "User doesn't have access to this method", content = @Content)

    })
    public ResponseEntity<List<ListTransfersByManagerDTO>> listTransfersByManager(
        @RequestParam(name = "size", defaultValue = "25") Integer pageSize, 
        @RequestParam(name = "page", defaultValue = "0") Integer pageNumber
    ) {
        return ResponseEntity.ok().body(
            listTransfersByManager
            .execute(pageSize, pageNumber)
            .stream()
            .map(transfer -> ListTransfersByManagerDTO.fromDomain(transfer))
            .toList()
        );
    }
    
    @PostMapping
    @Operation(summary = "Transfer money", description = "Request money transfer from payer to payee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "203", description = "Request registered successfully", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TransferData.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data / Data field missing", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
        @ApiResponse(responseCode = "422", description = "Unprocessable  / Business error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<TransferData> transferMoney(Authentication auth, @RequestBody TransferDTO transferDTO){
        TransferData transferInput = new TransferData(
            transferDTO.value(),
            transferDTO.payer(),
            transferDTO.payee()
        );
        requestTransferUseCase.execute(auth.getName(), transferInput);
        return ResponseEntity.accepted().body(transferInput);
    }

    @PostMapping("refund/{transferId}")
    @Operation(summary = "Refund transfer", description = "Request transfer refund")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "203", description = "Request registered successfully", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TransferDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data / Data field missing", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
        @ApiResponse(responseCode = "404", description = "Transfer not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "422", description = "Unprocessable  / Business error", content = @Content(schema = @Schema(implementation = String.class))),
    })
    public ResponseEntity<TransferDTO> refund(Authentication auth, @PathVariable(required = true) UUID transferId) {
        Transfer transfer = refundTransferUsecase.execute(auth.getName(), transferId);
        TransferDTO transferDTO = TransferDTO.fromDomain(transfer);
        return ResponseEntity.accepted().body(transferDTO);
    }

}
