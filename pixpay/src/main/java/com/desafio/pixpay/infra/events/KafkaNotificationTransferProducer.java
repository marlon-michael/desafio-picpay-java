package com.desafio.pixpay.infra.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.desafio.pixpay.core.gateways.NotifyTransferGateway;
import com.desafio.pixpay.core.usecases.data.TransferData;

@Component
public class KafkaNotificationTransferProducer implements NotifyTransferGateway {
    
    private final KafkaTemplate<String, Object> template;

	public KafkaNotificationTransferProducer(KafkaTemplate<String, Object> template) {
		this.template = template;
	}

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
