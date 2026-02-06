package com.desafio.pixpay.adapters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.adapters.dtos.TransferDTO;
import com.desafio.pixpay.core.usecases.TransferMoneyUseCase;
import com.desafio.pixpay.core.usecases.input.TransferInput;

@RestController
@RequestMapping("transfer")
public class TransferController {

    @Autowired
    TransferMoneyUseCase transferMoneyUseCase;
    
    @PostMapping("transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferDTO transferDTO){
        TransferInput transferInput = new TransferInput(
            transferDTO.value(),
            transferDTO.payer(),
            transferDTO.payee()
        );
        transferMoneyUseCase.execute(transferInput);
        return ResponseEntity.ok().build();
    }

}
