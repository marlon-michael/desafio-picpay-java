package com.desafio.pixpay.infra.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.usecases.ProcessTransferUseCase;
import com.desafio.pixpay.core.usecases.input.TransferInput;

@Component
public class KafkaTransferConsumer {

    private ProcessTransferUseCase processTransferUseCase;

    public KafkaTransferConsumer(ProcessTransferUseCase transferMoneyUseCase){
        this.processTransferUseCase = transferMoneyUseCase;
    }
    
    @KafkaListener(id = "transfer-request", topics = "transfer-request", groupId = "pixpay-group")
    public void listen(TransferInput transfer){
        try {
            processTransferUseCase.execute(transfer);
        } catch (BusinessException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
