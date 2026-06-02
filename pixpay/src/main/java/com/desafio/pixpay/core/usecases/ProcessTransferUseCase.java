package com.desafio.pixpay.core.usecases;


import java.util.Map;
import java.util.UUID;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.exceptions.UuidAlreadyExistsException;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.LoggerGateway;
import com.desafio.pixpay.core.gateways.NotifyTransferGateway;
import com.desafio.pixpay.core.gateways.TransferAuthorizerGateway;
import com.desafio.pixpay.core.gateways.TransferGateway;
import com.desafio.pixpay.core.log.TransferLogField;
import com.desafio.pixpay.core.usecases.data.TransferData;

public class ProcessTransferUseCase {
    private AccountGateway accountGateway;
    private TransferGateway transferGateway;
    private TransferAuthorizerGateway transferAuthorizerGateway;
    private NotifyTransferGateway notifyTransferGateway;
    private LoggerGateway logger;

    public ProcessTransferUseCase(AccountGateway accountGateway, TransferGateway transferGateway, TransferAuthorizerGateway transferAuthorizerGateway, NotifyTransferGateway notifyTransferGateway, LoggerGateway logger) {
        this.accountGateway = accountGateway;
        this.transferGateway = transferGateway;
        this.transferAuthorizerGateway = transferAuthorizerGateway;
        this.notifyTransferGateway = notifyTransferGateway;
        this.logger = logger;
    }

    public Transfer execute(TransferData transferData){

        logger.info(
            "Starting transfer processing",
            Map.of(
                TransferLogField.TRANSFER_PAYER, transferData.getPayer(),
                TransferLogField.TRANSFER_PAYEE, transferData.getPayee(),
                TransferLogField.TRANSFER_VALUE, transferData.getValue()
            )
        );

        Account payer = accountGateway.findById(transferData.getPayer());
        Account payee = accountGateway.findById(transferData.getPayee());
        Money value = Money.builder().setMoneyInReal(transferData.getValue());
        
        if (payer == null){
            throw new BusinessException("Payer account not found.");
        }
        
        if (payee == null){
            throw new BusinessException("Payee account not found.");
        }

        if (value.getMoneyInCents() <= 0){
            throw new BusinessException("The transfer amount cannot be negative or zero.");
        }

        if (payer.getAccountType() == AccountTypeEnum.BUSINESS){
            throw new BusinessException("Business account type cannot transfer money to another account.");
        }
        
        if (payer.getBalanceInCents() < value.getMoneyInCents()){
            throw new BusinessException("The transfer amount cannot exceed the payer's balance.");
        }

        if (!transferAuthorizerGateway.isAuthorized()){
            throw new BusinessException("Transfer not authorized.");
        }

        payer.getBalance().subtractValueInReal(value);
        payee.getBalance().addValueInReal(value);


        Transfer transfer = new Transfer(value, payer, payee);

        saveTransfer(transfer);

        accountGateway.updateBalanceById(payer.getId(), payer.getBalance());
        accountGateway.updateBalanceById(payee.getId(), payee.getBalance());

        transferData.setId(transfer.getId());
        transferData.setValue(value.getMoneyInReal());

        sendTransferNotification(transferData);

        logger.info(
            "Completing transfer processing",
            Map.of(
                TransferLogField.TRANSFER_ID, transferData.getId(),
                TransferLogField.TRANSFER_PAYER, transferData.getPayer(),
                TransferLogField.TRANSFER_PAYEE, transferData.getPayee(),
                TransferLogField.TRANSFER_VALUE, transferData.getValue()
            )
        );

        return transfer;
    }

    private void saveTransfer(Transfer transfer) {
        int errorCount = 0;
        while(true){
            try {
                transferGateway.create(transfer);
                break;
            } catch (UuidAlreadyExistsException exception) {
                if (errorCount > 5) throw exception;
                transfer.setId(UUID.randomUUID());
                errorCount++;
            } catch(Exception exception){
                if (errorCount > 5) throw exception;
                errorCount++;
            }
        }
    }

    private void sendTransferNotification(TransferData transferData){
        int errorCount = 0;
        while (true) {
            try {
                notifyTransferGateway.send(transferData);
                break;
            } catch (Exception exception) {
                if (errorCount > 3) {
                    logger.error(
                        exception.getMessage()+
                        exception.getCause().toString()+
                        exception.getStackTrace().toString(), 
                        null
                    );
                    break;
                }
            }
        }
    }
}
