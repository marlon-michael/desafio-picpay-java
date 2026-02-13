package com.desafio.pixpay.core.usecases;

import java.util.UUID;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.exceptions.UuidAlreadyExistsException;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.TransferGateway;

public class RefundTransferUsecase {
    private final TransferGateway transferGateway;
    private final AccountGateway accountGateway;

    public RefundTransferUsecase(AccountGateway accountGateway, TransferGateway transferGateway) {
        this.accountGateway = accountGateway;
        this.transferGateway = transferGateway;
    }

    public Transfer execute(String auth, UUID id) {
        Transfer refundingTransfer = transferGateway.findById(id);
        Account oldPayer = refundingTransfer.getPayer();
        Account oldPayee = refundingTransfer.getPayee();
        Money value = refundingTransfer.getValue();

        if (!auth.equals(oldPayee.getIdentification().getIdentificationNumber())) {
            throw new IllegalArgumentException("Authenticated account is different from payee account.");
        }

        if (oldPayee.getBalanceInPips() < value.getMoneyInPips()) {
            throw new IllegalArgumentException("The transfer amount cannot exceed the payer's balance.");
        }

        oldPayee.getBalance().subtractValueInCurrency(value);
        oldPayer.getBalance().addValueInCurrency(value);

        Transfer transfer = new Transfer(value, oldPayee, oldPayer);

        boolean error = false;
        int tries = 0;
        do {
            try {
                transferGateway.create(transfer);
            } catch (UuidAlreadyExistsException exception) {
                transfer.setId(UUID.randomUUID());
                error = true;
                tries++;
                if (tries > 5) throw exception;
            }
        } while (error);

        accountGateway.updateBalanceById(oldPayee.getId(), oldPayee.getBalance());
        accountGateway.updateBalanceById(oldPayer.getId(), oldPayer.getBalance());

        return transfer;
    }
}
