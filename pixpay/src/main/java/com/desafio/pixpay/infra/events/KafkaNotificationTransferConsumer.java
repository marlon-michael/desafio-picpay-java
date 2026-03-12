package com.desafio.pixpay.infra.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;

import com.desafio.pixpay.core.usecases.data.TransferData;
import com.desafio.pixpay.infra.client.NotifyTransfer;

import feign.FeignException;


@Component
public class KafkaNotificationTransferConsumer {

    @Autowired
    private NotifyTransfer notifyTransfer;

    @RetryableTopic(
        attempts = "3",
        include = FeignException.class,
        backOff = @BackOff(delay = 3000, multiplier = 2.0),
        dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(id = "transfer-notification", topics = "transfer-notification", groupId = "pixpay-group")
    public void listen(TransferData transferData){
        notifyTransfer.send(transferData);
    }
}
