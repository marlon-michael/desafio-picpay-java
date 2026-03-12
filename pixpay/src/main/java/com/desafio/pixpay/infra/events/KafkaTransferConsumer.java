package com.desafio.pixpay.infra.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.usecases.ProcessTransferUseCase;
import com.desafio.pixpay.core.usecases.data.TransferData;

@Component
public class KafkaTransferConsumer {

    @Autowired
    private ProcessTransferUseCase processTransferUseCase;
    
    @KafkaListener(id = "transfer-request", topics = "transfer-request", groupId = "pixpay-group")
    public void listen(TransferData transferData){
        try {
            processTransferUseCase.execute(transferData);
        } catch (BusinessException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
