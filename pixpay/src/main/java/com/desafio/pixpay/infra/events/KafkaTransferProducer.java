package com.desafio.pixpay.infra.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.desafio.pixpay.core.gateways.TransferProducerGateway;
import com.desafio.pixpay.core.usecases.data.TransferData;

@Component
public class KafkaTransferProducer implements TransferProducerGateway {

    private final KafkaTemplate<String, Object> template;

    KafkaTransferProducer(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    @Override
    public void send(TransferData transferData) {
        template.send("transfer-request", transferData);
    }
    
}
