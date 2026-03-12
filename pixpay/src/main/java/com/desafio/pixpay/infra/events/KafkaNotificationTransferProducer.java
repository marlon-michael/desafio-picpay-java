package com.desafio.pixpay.infra.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.desafio.pixpay.core.gateways.NotifyTransferGateway;
import com.desafio.pixpay.core.usecases.data.TransferData;

@Component
public class KafkaNotificationTransferProducer implements NotifyTransferGateway {
    
    @Autowired
    private KafkaTemplate<String, Object> template;

	@Override
	public boolean send(TransferData transferData) {
		try {
			template.send("transfer-notification", transferData).get();
			return true;
		} catch (Exception exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}

}
