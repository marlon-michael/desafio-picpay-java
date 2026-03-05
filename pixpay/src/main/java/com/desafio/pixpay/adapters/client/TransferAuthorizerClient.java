package com.desafio.pixpay.adapters.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import com.desafio.pixpay.adapters.dtos.TransferAuthorizationDTO;


@FeignClient(name = "transfer-authorizer", url = "${comunication.external-api.authorizer-url}")
public interface TransferAuthorizerClient {

    @GetMapping
    public ResponseEntity<TransferAuthorizationDTO> isAuthorized();
    
}
