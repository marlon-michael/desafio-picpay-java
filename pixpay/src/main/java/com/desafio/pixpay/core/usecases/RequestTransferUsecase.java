package com.desafio.pixpay.core.usecases;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.exceptions.BusinessAccountCannotMakeTransferException;
import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.TransferProducerGateway;
import com.desafio.pixpay.core.usecases.data.TransferData;

public class RequestTransferUsecase {

    private TransferProducerGateway transferProducerGateway;
    private AccountGateway accountGateway;

    public RequestTransferUsecase(TransferProducerGateway transferProducerGateway, AccountGateway accountGateway){
        this.transferProducerGateway = transferProducerGateway;
        this.accountGateway = accountGateway;
    }

    public void execute(String authentication, TransferData transferData){

        if (transferData.getValue() == null){
            throw new BusinessAccountCannotMakeTransferException("Transfer value is missing.");
        }
        if (transferData.getPayer() == null){
            throw new BusinessAccountCannotMakeTransferException("Transfer payer is missing.");
        }
        if (transferData.getPayee() == null){
            throw new BusinessAccountCannotMakeTransferException("Transfer payee is missing.");
        }

        Account payer = accountGateway.findById(transferData.getPayer());

        if (payer == null){
            throw new BusinessAccountCannotMakeTransferException("Payer account not found.");
        }

        if (!authentication.equals(payer.getIdentification().getIdentificationNumber())) {
            throw new BusinessException("Authenticated account is different from payer account.");
        }

        transferProducerGateway.send(transferData);
    }
}
