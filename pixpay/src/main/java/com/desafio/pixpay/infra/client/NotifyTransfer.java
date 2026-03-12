package com.desafio.pixpay.infra.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.desafio.pixpay.adapters.client.NotifyTransferClient;
import com.desafio.pixpay.adapters.dtos.TransferDTO;
import com.desafio.pixpay.core.gateways.NotifyTransferGateway;
import com.desafio.pixpay.core.usecases.data.TransferData;

@Component
public class NotifyTransfer implements NotifyTransferGateway {

    @Autowired
    private NotifyTransferClient notifyTransferClient;

    @Override
    public boolean send(TransferData transferData) {
        TransferDTO transferDTO = new TransferDTO(
            transferData.getId(),
            transferData.getValue(),
            transferData.getPayer(),
            transferData.getPayee()
        );
        try {
            ResponseEntity<?> response = notifyTransferClient.send(transferDTO);
            if (response.getStatusCode().is2xxSuccessful()) return true;
        } catch (Exception exception) {
            throw exception;
        }
        return false;
    }
    
}
