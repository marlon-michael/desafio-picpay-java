package com.desafio.pixpay.core.gateways;

import com.desafio.pixpay.core.usecases.data.TransferData;

public interface NotifyTransferGateway {
    boolean send(TransferData transfer);
}
