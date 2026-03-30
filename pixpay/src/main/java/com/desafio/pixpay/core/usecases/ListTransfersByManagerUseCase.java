package com.desafio.pixpay.core.usecases;

import java.util.List;

import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.gateways.TransferGateway;

public class ListTransfersByManagerUseCase {
    private final TransferGateway transferGateway;

    public ListTransfersByManagerUseCase(TransferGateway transferGateway){
        this.transferGateway = transferGateway;
    }

    public List<Transfer> execute(Integer pageSize, Integer pageNumber) {
        return transferGateway.findAll(pageSize, pageNumber);
    }
}
