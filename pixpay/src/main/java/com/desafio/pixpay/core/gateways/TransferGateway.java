package com.desafio.pixpay.core.gateways;

import com.desafio.pixpay.core.domain.transfer.Transfer;

public interface TransferGateway {
    Transfer create(Transfer transfer);
}
