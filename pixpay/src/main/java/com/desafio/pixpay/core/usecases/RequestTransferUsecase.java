package com.desafio.pixpay.core.usecases;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.TransferProducerGateway;
import com.desafio.pixpay.core.usecases.input.TransferInput;

public class RequestTransferUsecase {

    private TransferProducerGateway transferProducerGateway;
    private AccountGateway accountGateway;

    public RequestTransferUsecase(TransferProducerGateway transferProducerGateway, AccountGateway accountGateway){
        this.transferProducerGateway = transferProducerGateway;
        this.accountGateway = accountGateway;
    }

    public void execute(String authentication, TransferInput transferInput){
        Account payer = accountGateway.findById(transferInput.getPayer());

        if (!authentication.equals(payer.getIdentification().getIdentificationNumber())) {
            throw new BusinessException("Authenticated account is different from payer account.");
        }

        transferProducerGateway.send(transferInput);
    }
}
