package com.desafio.pixpay.core.gateways;

import com.desafio.pixpay.core.usecases.input.TransferInput;

public interface TransferProducerGateway {
    void send(TransferInput transfer);
}
