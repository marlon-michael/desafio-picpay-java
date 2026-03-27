package com.desafio.pixpay.core.usecases;

import java.util.UUID;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.gateways.TransferGateway;
import com.desafio.pixpay.core.gateways.TransferProducerGateway;
import com.desafio.pixpay.core.usecases.data.TransferData;

public class RefundTransferUsecase {
    private final TransferGateway transferGateway;
    private final TransferProducerGateway transferProducerGateway;

    public RefundTransferUsecase(TransferGateway transferGateway, TransferProducerGateway transferProducerGateway) {
        this.transferGateway = transferGateway;
        this.transferProducerGateway = transferProducerGateway;
    }

    public Transfer execute(String auth, UUID id) {
        Account refundRecipient;
        Account refundIssuer;
        Money value;
        Transfer refundingTransfer = transferGateway.findById(id);

        if (refundingTransfer == null){
            throw new BusinessException("Transfer not found.");
        }

        refundRecipient = refundingTransfer.getPayer();
        refundIssuer = refundingTransfer.getPayee();
        value = refundingTransfer.getValue();

        if (!auth.equals(refundIssuer.getIdentification().getIdentificationNumber())) {
            throw new BusinessException("Authenticated account is different from payee account.");
        }

        if (refundIssuer.getBalanceInCents() < value.getMoneyInCents()) {
            throw new BusinessException("The transfer amount cannot exceed the payer's balance.");
        }

        refundingTransfer.setId(null);
        refundingTransfer.setPayer(refundIssuer);
        refundingTransfer.setPayee(refundRecipient);
        TransferData transfer = new TransferData(value.getMoneyInReal(), refundIssuer.getId(), refundRecipient.getId());

        transferProducerGateway.send(transfer);

        return refundingTransfer;
    }
}
