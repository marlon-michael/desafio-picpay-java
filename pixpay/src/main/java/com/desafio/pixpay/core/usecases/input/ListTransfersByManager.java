package com.desafio.pixpay.core.usecases.input;

import java.util.List;

import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.gateways.TransferGateway;

public class ListTransfersByManager {
    private final TransferGateway transferGateway;

    public ListTransfersByManager(TransferGateway transferGateway){
        this.transferGateway = transferGateway;
    }

    public List<Transfer> execute(){
        return transferGateway.findAll();
    }
}
