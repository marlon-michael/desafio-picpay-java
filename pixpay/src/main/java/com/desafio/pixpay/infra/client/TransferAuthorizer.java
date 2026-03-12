package com.desafio.pixpay.infra.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.desafio.pixpay.adapters.client.TransferAuthorizerClient;
import com.desafio.pixpay.adapters.dtos.TransferAuthorizationDTO;
import com.desafio.pixpay.core.gateways.TransferAuthorizerGateway;

import feign.FeignException;


public class TransferAuthorizer implements TransferAuthorizerGateway {

    @Autowired
    private TransferAuthorizerClient transferAuthorizerClient;

    @Override
    public boolean isAuthorized() {
        ResponseEntity <TransferAuthorizationDTO> transferAuthorization;
        try {
            transferAuthorization = transferAuthorizerClient.isAuthorized();
            if (!transferAuthorization.hasBody()) return false;
            if (transferAuthorization.getBody().status().equals("success")) return true;
        } catch (FeignException.Forbidden exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        return false;
    }
    
}
