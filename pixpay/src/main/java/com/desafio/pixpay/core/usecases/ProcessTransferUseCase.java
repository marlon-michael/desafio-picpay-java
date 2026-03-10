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
import com.desafio.pixpay.core.gateways.TransferAuthorizerGateway;
import com.desafio.pixpay.core.gateways.TransferGateway;
import com.desafio.pixpay.core.usecases.input.TransferInput;

public class ProcessTransferUseCase {
    AccountGateway accountGateway;
    TransferGateway transferGateway;
    TransferAuthorizerGateway transferAuthorizerGateway;

    public ProcessTransferUseCase(AccountGateway accountGateway, TransferGateway transferGateway, TransferAuthorizerGateway transferAuthorizerGateway){
        this.accountGateway = accountGateway;
        this.transferGateway = transferGateway;
        this.transferAuthorizerGateway = transferAuthorizerGateway;
    }

    public Transfer execute(TransferInput transferInput){
        Account payer = accountGateway.findById(transferInput.getPayer());
        Account payee = accountGateway.findById(transferInput.getPayee());
        Money value = Money.builder().setMoneyInCurrency(transferInput.getValue());

        if (payer.getAccountType() == AccountTypeEnum.BUSINESS){
            throw new BusinessAccountCannotMakeTransferException("Business account type cannot transfer money to another account.");
        }
        
        if (payer.getBalanceInPips() < value.getMoneyInCents()){
            throw new InsufficientBalanceException("The transfer amount cannot exceed the payer's balance.");
        }

        if (!transferAuthorizerGateway.isAuthorized()){
            throw new TransferNotAuthorizedException("Transfer not authorized.");
        }

        payer.getBalance().subtractValueInCurrency(value);
        payee.getBalance().addValueInCurrency(value);


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
            }
        }while(error);

        accountGateway.updateBalanceById(payer.getId(), payer.getBalance());
        accountGateway.updateBalanceById(payee.getId(), payee.getBalance());
        return transfer;
    }
}
