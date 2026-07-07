package com.desafio.pixpay.adapters.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.TransferOutputDTO;
import com.desafio.pixpay.adapters.dtos.ResponseData;
import com.desafio.pixpay.adapters.dtos.TransferInputDTO;
import com.desafio.pixpay.core.usecases.ListTransfersByManagerUseCase;
import com.desafio.pixpay.core.usecases.ListTransfersByUserUseCase;
import com.desafio.pixpay.core.usecases.RefundTransferUsecase;
import com.desafio.pixpay.core.usecases.RequestTransferUsecase;
import com.desafio.pixpay.core.usecases.data.TransferData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
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
    
    final ListTransfersByManagerUseCase listTransfersByManagerUseCase;
    final ListTransfersByUserUseCase listTransfersByUserUseCase;
    final RequestTransferUsecase requestTransferUseCase;
    final RefundTransferUsecase refundTransferUsecase;

    TransferController(ListTransfersByManagerUseCase listTransfersByManagerUseCase, ListTransfersByUserUseCase listTransfersByUserUseCase, RequestTransferUsecase requestTransferUseCase, RefundTransferUsecase refundTransferUsecase) {
        this.listTransfersByManagerUseCase = listTransfersByManagerUseCase;
        this.listTransfersByUserUseCase = listTransfersByUserUseCase;
        this.requestTransferUseCase = requestTransferUseCase;
        this.refundTransferUsecase = refundTransferUsecase;
    }


    @GetMapping("all")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    @Operation(summary = "Lista all transfers", description = "List all transfers to be seen by a manager")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List returned successfully", 
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(allOf = ResponseData.class),
                schemaProperties = @SchemaProperty(
                    name = "content", 
                    array = @ArraySchema(schema = @Schema(implementation = TransferOutputDTO.class))
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
        @ApiResponse(responseCode = "403", description = "User doesn't have access to this method", content = @Content)

    })
    public ResponseEntity<EntityModel<?>> listTransfersByManager(
        @RequestParam(name = "size", defaultValue = "25") Integer pageSize, 
        @RequestParam(name = "page", defaultValue = "0") Integer pageNumber
    ) {
        List<TransferOutputDTO> transfers = listTransfersByManagerUseCase
            .execute(pageSize, pageNumber)
            .stream()
            .map(transfer -> TransferOutputDTO.fromDomain(transfer))
            .toList();
        ResponseData<?> responseData = new ResponseData<>(transfers, null, LocalDateTime.now());
        EntityModel<ResponseData<?>> model = EntityModel.of(responseData,
            linkTo(methodOn(AccountController.class).listAccountsByManager(null, null)).withRel("list accounts")
        );
        return ResponseEntity.ok().body(model);
    }

    @GetMapping
    @Operation(summary = "List transfers from current account", description = "List all transfers details from current account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List returned successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(allOf = ResponseData.class),
                schemaProperties = @SchemaProperty(
                    name = "content", 
                    array = @ArraySchema(schema = @Schema(implementation = TransferOutputDTO.class))
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
    })
    public ResponseEntity<EntityModel<?>> listTransfersFromAccount(
        Authentication auth,
        @RequestParam(name = "size", defaultValue = "25") Integer pageSize, 
        @RequestParam(name = "page", defaultValue = "0") Integer pageNumber
    ) {
        String identificationNumber = auth.getName();
        List<TransferOutputDTO> transfers = listTransfersByUserUseCase
            .execute(identificationNumber, pageSize, pageNumber)
            .stream()
            .map(transfer -> TransferOutputDTO.fromDomain(transfer))
            .toList();
        ResponseData<?> responseData = new ResponseData<>(transfers, "Authenticated", LocalDateTime.now());
        EntityModel<ResponseData<?>> model = EntityModel.of(responseData, 
            linkTo(methodOn(AccountController.class).getAccountDatails(null)).withRel("account details"),
            linkTo(methodOn(TransferController.class).transferMoney(null, null)).withRel("request transfer"),
            linkTo(methodOn(TransferController.class).refund(null, null)).withRel("refund transfer made to you")
        );
        return ResponseEntity.ok().body(model);
    }
    
    @PostMapping
    @Operation(summary = "Transfer money", description = "Request money transfer from payer to payee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Request registered successfully", 
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(allOf = ResponseData.class),
                schemaProperties = @SchemaProperty(
                    name = "content", 
                    schema = @Schema(implementation = TransferInputDTO.class)
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid data / Data field missing", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
        @ApiResponse(responseCode = "422", description = "Unprocessable  / Business error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<EntityModel<?>> transferMoney(Authentication auth, @RequestBody TransferInputDTO transferDTO){
        TransferData transferData = new TransferData(
            transferDTO.value(),
            transferDTO.payer(),
            transferDTO.payee()
        );
        TransferData transfer = requestTransferUseCase.execute(auth.getName(), transferData);
        transferDTO = TransferInputDTO.fromDomain(transfer);
        ResponseData<?> responseData = new ResponseData<>(transferDTO, null, LocalDateTime.now());
        EntityModel<ResponseData<?>> model = EntityModel.of(responseData, 
            linkTo(methodOn(AccountController.class).getAccountDatails(null)).withRel("account details"),
            linkTo(methodOn(TransferController.class).listTransfersFromAccount(null, null,null)).withRel("account transfers"),
            linkTo(methodOn(TransferController.class).refund(null, null)).withRel("refund transfer made to you")
        );
        return ResponseEntity.accepted().body(model);
    }

    @PostMapping("refund/{transferId}")
    @Operation(summary = "Refund transfer", description = "Request transfer refund")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Request registered successfully", 
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(allOf = ResponseData.class),
                schemaProperties = @SchemaProperty(
                    name = "content", 
                    schema = @Schema(implementation = TransferInputDTO.class)
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid data / Data field missing", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
        @ApiResponse(responseCode = "404", description = "Transfer not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "422", description = "Unprocessable  / Business error", content = @Content(schema = @Schema(implementation = String.class))),
    })
    public ResponseEntity<EntityModel<?>> refund(Authentication auth, @PathVariable(required = true) UUID transferId) {
        TransferData transfer = refundTransferUsecase.execute(auth.getName(), transferId);
        TransferInputDTO transferDTO = TransferInputDTO.fromDomain(transfer);
        ResponseData<?> responseData = new ResponseData<>(transferDTO, null, LocalDateTime.now());
        EntityModel<ResponseData<?>> model = EntityModel.of(responseData, 
            linkTo(methodOn(AccountController.class).getAccountDatails(null)).withRel("account details"),
            linkTo(methodOn(TransferController.class).listTransfersFromAccount(null, null,null)).withRel("account transfers"),
            linkTo(methodOn(TransferController.class).transferMoney(null, null)).withRel("make a transfer")
        );
        return ResponseEntity.accepted().body(model);
    }

}
