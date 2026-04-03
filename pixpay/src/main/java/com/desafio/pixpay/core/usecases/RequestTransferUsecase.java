package com.desafio.pixpay.core.usecases;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.exceptions.InvalidDataException;
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
            throw new InvalidDataException("Transfer value is missing.");
        }
        if (transferData.getPayer() == null){
            throw new InvalidDataException("Transfer payer is missing.");
        }
        if (transferData.getPayee() == null){
            throw new InvalidDataException("Transfer payee is missing.");
        }

        Account payer = accountGateway.findById(transferData.getPayer());
        
        if (payer == null){
            throw new BusinessException("Payer account not found.");
        }

        if (!authentication.equals(payer.getIdentification().getIdentificationNumber())) {
            throw new BusinessException("Authenticated account is different from payer account.");
        }

        if (payer.getAccountType() == AccountTypeEnum.BUSINESS){
            throw new BusinessException("Business account type cannot transfer money to another account.");
        }

        transferProducerGateway.send(transferData);
    }
}
