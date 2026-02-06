package com.desafio.pixpay.core.usecases;


import java.util.UUID;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.exceptions.UuidAlreadyExistsException;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.TransferGateway;
import com.desafio.pixpay.core.usecases.input.TransferInput;

public class TransferMoneyUseCase {
    AccountGateway accountGateway;
    TransferGateway transferGateway;

    public TransferMoneyUseCase(AccountGateway accountGateway, TransferGateway transferGateway){
        this.accountGateway = accountGateway;
        this.transferGateway = transferGateway;
    }

    public boolean execute(TransferInput transferInput){
        Account payer = accountGateway.findAccountById(transferInput.getPayer());
        Account payee = accountGateway.findAccountById(transferInput.getPayee());
        Double value = Math.ceil(transferInput.getValue()*100) / 100.0;
        
        if (payee.getAccountType() == AccountTypeEnum.BUSINESS){
            throw new IllegalArgumentException("Account type business cannot transfer money to other accounts.");
        }
        
        if (payer.getBalanceInReal() < value){
            throw new IllegalArgumentException("The transfer amount cannot exceed the payer's balance.");
        }

        payer.getBalance().subtractValueInCurrency(value);
        payee.getBalance().addValueInCurrency(value);

        Money money = new Money();
        money.setMoneyInCurrency(value);

        Transfer transfer = new Transfer(money, payer, payee);

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

        accountGateway.updateAccountBalanceById(payer.getId(), payer.getBalance());
        accountGateway.updateAccountBalanceById(payee.getId(), payee.getBalance());
        return true;
    }
}
