package com.desafio.pixpay.core.usecases;


import java.util.UUID;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.exceptions.BusinessAccountCannotMakeTransferException;
import com.desafio.pixpay.core.exceptions.InsufficientBalanceException;
import com.desafio.pixpay.core.exceptions.TransferNotAuthorizedException;
import com.desafio.pixpay.core.exceptions.UuidAlreadyExistsException;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.NotifyTransferGateway;
import com.desafio.pixpay.core.gateways.TransferAuthorizerGateway;
import com.desafio.pixpay.core.gateways.TransferGateway;
import com.desafio.pixpay.core.usecases.data.TransferData;

public class ProcessTransferUseCase {
    AccountGateway accountGateway;
    TransferGateway transferGateway;
    TransferAuthorizerGateway transferAuthorizerGateway;
    NotifyTransferGateway notifyTransferGateway;

    public ProcessTransferUseCase(AccountGateway accountGateway, TransferGateway transferGateway, TransferAuthorizerGateway transferAuthorizerGateway, NotifyTransferGateway notifyTransferGateway){
        this.accountGateway = accountGateway;
        this.transferGateway = transferGateway;
        this.transferAuthorizerGateway = transferAuthorizerGateway;
        this.notifyTransferGateway = notifyTransferGateway;
    }

    public Transfer execute(TransferData transferData){
        Account payer = accountGateway.findById(transferData.getPayer());
        Account payee = accountGateway.findById(transferData.getPayee());
        Money value = Money.builder().setMoneyInReal(transferData.getValue());

        if (payer == null){
            throw new BusinessAccountCannotMakeTransferException("Payer account not found.");
        }

        if (payee == null){
            throw new BusinessAccountCannotMakeTransferException("Payee account not found.");
        }

        if (payer.getAccountType() == AccountTypeEnum.BUSINESS){
            throw new BusinessAccountCannotMakeTransferException("Business account type cannot transfer money to another account.");
        }
        
        if (payer.getBalanceInCents() < value.getMoneyInCents()){
            throw new InsufficientBalanceException("The transfer amount cannot exceed the payer's balance.");
        }

        if (!transferAuthorizerGateway.isAuthorized()){
            throw new TransferNotAuthorizedException("Transfer not authorized.");
        }

        payer.getBalance().subtractValueInReal(value);
        payee.getBalance().addValueInReal(value);


        Transfer transfer = new Transfer(value, payer, payee);

        boolean error = false;
        int times = 0;
        do{
            try {
                transferGateway.create(transfer);
                error = false;
            } catch (UuidAlreadyExistsException exception) {
                transfer.setId(UUID.randomUUID());
                error = true;
                times++;
                if (times > 5) throw exception;
            } catch(Exception exception){
                error = true;
                throw exception;
            }
        }while(error);

        accountGateway.updateBalanceById(payer.getId(), payer.getBalance());
        accountGateway.updateBalanceById(payee.getId(), payee.getBalance());

        transferData.setId(transfer.getId());
        transferData.setValue(value.getMoneyInReal());

        error = false;
        times = 0;
        do {
            try {
                notifyTransferGateway.send(transferData);
            } catch (Exception exception) {
                error = true;
                times++;
                if (times > 3) throw new RuntimeException(exception.getMessage());
            }
        } while (error);

        return transfer;
    }
}
