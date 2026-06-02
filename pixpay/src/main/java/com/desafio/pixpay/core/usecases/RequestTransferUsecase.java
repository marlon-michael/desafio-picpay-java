package com.desafio.pixpay.core.usecases;

import java.util.Map;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.exceptions.InvalidDataException;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.LoggerGateway;
import com.desafio.pixpay.core.gateways.TransferProducerGateway;
import com.desafio.pixpay.core.log.TransferLogField;
import com.desafio.pixpay.core.usecases.data.TransferData;

public class RequestTransferUsecase {

    private TransferProducerGateway transferProducerGateway;
    private AccountGateway accountGateway;
    private LoggerGateway logger;

    public RequestTransferUsecase(TransferProducerGateway transferProducerGateway, AccountGateway accountGateway, LoggerGateway logger){
        this.transferProducerGateway = transferProducerGateway;
        this.accountGateway = accountGateway;
        this.logger = logger;
    }

    public TransferData execute(String authentication, TransferData transferData){

        logger.info("Starting transfer request verification");
        
        if (transferData.getValue() == null){
            throw new InvalidDataException("Transfer value is missing.");
        }
        if (transferData.getPayer() == null){
            throw new InvalidDataException("Transfer payer is missing.");
        }
        if (transferData.getPayee() == null){
            throw new InvalidDataException("Transfer payee is missing.");
        }

        logger.info(
            "Starting transfer request",
            Map.of(
                TransferLogField.TRANSFER_PAYER, transferData.getPayer(),
                TransferLogField.TRANSFER_PAYEE, transferData.getPayee(),
                TransferLogField.TRANSFER_VALUE, transferData.getValue()
            )
        );

        Account payer = accountGateway.findById(transferData.getPayer());
        
        if (payer == null){
            throw new BusinessException("Payer account not found.");
        }

        if (transferData.getValue() <= 0){
            throw new BusinessException("The transfer amount cannot be negative or zero.");
        }

        if (!authentication.equals(payer.getIdentification().getIdentificationNumber())) {
            throw new BusinessException("Authenticated account is different from payer account.");
        }

        if (payer.getAccountType() == AccountTypeEnum.BUSINESS){
            throw new BusinessException("Business account type cannot transfer money to another account.");
        }

        transferProducerGateway.send(transferData);

        logger.info(
            "Completing transfer request",
            Map.of(
                TransferLogField.TRANSFER_PAYER, transferData.getPayer(),
                TransferLogField.TRANSFER_PAYEE, transferData.getPayee(),
                TransferLogField.TRANSFER_VALUE, transferData.getValue()
            )
        );

        return transferData;
    }
}
