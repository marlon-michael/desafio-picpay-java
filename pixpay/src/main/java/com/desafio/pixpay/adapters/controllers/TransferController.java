package com.desafio.pixpay.adapters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.TransferDTO;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.usecases.TransferMoneyUseCase;
import com.desafio.pixpay.core.usecases.input.TransferInput;

@RestController
@RequestMapping("transfer")
public class TransferController {

    @Autowired
    TransferMoneyUseCase transferMoneyUseCase;
    
    @PostMapping
    public ResponseEntity<String> transferMoney(Authentication auth, @RequestBody TransferDTO transferDTO){
        TransferInput transferInput = new TransferInput(
            transferDTO.value(),
            transferDTO.payer(),
            transferDTO.payee()
        );
        Transfer transfer = transferMoneyUseCase.execute(auth.getName(), transferInput);
        TransferDTO responseTransferDTO = new TransferDTO(
            transfer.getValue().getMoneyInCurrency(),
            transfer.getPayer().getId(),
            transfer.getPayee().getId()
        );
        return ResponseEntity.ok().body(responseTransferDTO.toString());
    }

}
