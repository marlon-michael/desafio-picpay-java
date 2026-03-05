package com.desafio.pixpay.infra.authorizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.desafio.pixpay.adapters.client.TransferAuthorizerClient;
import com.desafio.pixpay.adapters.dtos.TransferAuthorizationDTO;
import com.desafio.pixpay.core.gateways.TransferAuthorizerGateway;


public class TransferAuthorizer implements TransferAuthorizerGateway {

    @Autowired
    private TransferAuthorizerClient transferAuthorizerClient;

    @Override
    public boolean isAuthorized() {
        ResponseEntity <TransferAuthorizationDTO> transferAuthorization;
        try {
            transferAuthorization = transferAuthorizerClient.isAuthorized();
            System.out.println(transferAuthorization.getBody().toString());
            if (!transferAuthorization.hasBody()) return false;
            if (transferAuthorization.getBody().status().equals("success")) return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }
    
}
