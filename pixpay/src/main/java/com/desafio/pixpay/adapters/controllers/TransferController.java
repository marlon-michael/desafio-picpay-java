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
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.ListTransfersByManagerDTO;
import com.desafio.pixpay.adapters.dtos.TransferDTO;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.usecases.RefundTransferUsecase;
import com.desafio.pixpay.core.usecases.TransferMoneyUseCase;
import com.desafio.pixpay.core.usecases.input.ListTransfersByManager;
import com.desafio.pixpay.core.usecases.input.TransferInput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@EnableMethodSecurity
@RequestMapping("transfer")
public class TransferController {

    @Autowired
    TransferMoneyUseCase transferMoneyUseCase;

    @Autowired
    ListTransfersByManager listTransfersByManager;

    @Autowired
    RefundTransferUsecase refundTransferUsecase;
    
    @PostMapping
    public ResponseEntity<String> transferMoney(Authentication auth, @RequestBody TransferDTO transferDTO){
        TransferInput transferInput = new TransferInput(
            transferDTO.value(),
            transferDTO.payer(),
            transferDTO.payee()
        );
        Transfer transfer = transferMoneyUseCase.execute(auth.getName(), transferInput);
        TransferDTO responseTransferDTO = TransferDTO.fromDomain(transfer);
        return ResponseEntity.ok().body(responseTransferDTO.toString());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public List<ListTransfersByManagerDTO> listTransfersByManager() {
        return listTransfersByManager
            .execute()
            .stream()
            .map(transfer -> ListTransfersByManagerDTO.fromDomain(transfer))
            .toList();
    }

    @PostMapping("refund/{transferId}")
    public TransferDTO refund(Authentication auth, @PathVariable(required = true) UUID transferId) {
        Transfer transfer = refundTransferUsecase.execute(auth.getName(), transferId);
        TransferDTO transferDTO = TransferDTO.fromDomain(transfer);
        return transferDTO;
    }

}
