package com.desafio.pixpay.core.gateways;

import java.util.List;
import java.util.UUID;

import com.desafio.pixpay.core.domain.transfer.Transfer;

public interface TransferGateway {
    Transfer create(Transfer transfer);
    Transfer findById(UUID id);
    List<Transfer> findAll();
}
