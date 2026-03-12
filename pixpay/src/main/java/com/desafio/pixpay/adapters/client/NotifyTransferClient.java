package com.desafio.pixpay.adapters.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.desafio.pixpay.adapters.dtos.TransferDTO;

@FeignClient(name = "notify-transfer", url = "${comunication.external-api.notify-transfer-url}")
public interface NotifyTransferClient {

    @PostMapping
    public ResponseEntity<?> send(@RequestBody TransferDTO transferDTO);

}
