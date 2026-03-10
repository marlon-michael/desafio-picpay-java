package com.desafio.pixpay.infra.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.desafio.pixpay.core.gateways.TransferProducerGateway;
import com.desafio.pixpay.core.usecases.input.TransferInput;

@Component
public class KafkaTransferProducer implements TransferProducerGateway {

    @Autowired
    private KafkaTemplate<String, Object> template;

    @Override
    public void send(TransferInput transfer) {
        template.send("transfer-request", transfer);
    }
    
}
