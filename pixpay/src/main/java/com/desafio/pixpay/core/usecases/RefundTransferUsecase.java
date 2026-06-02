package com.desafio.pixpay.core.usecases;

import java.util.Map;
import java.util.UUID;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.exceptions.NotFoundException;
import com.desafio.pixpay.core.gateways.LoggerGateway;
import com.desafio.pixpay.core.gateways.TransferGateway;
import com.desafio.pixpay.core.gateways.TransferProducerGateway;
import com.desafio.pixpay.core.log.TransferLogField;
import com.desafio.pixpay.core.usecases.data.TransferData;

public class RefundTransferUsecase {
    private final TransferGateway transferGateway;
    private final TransferProducerGateway transferProducerGateway;
    private final LoggerGateway logger;

    public RefundTransferUsecase(TransferGateway transferGateway, TransferProducerGateway transferProducerGateway, LoggerGateway logger) {
        this.transferGateway = transferGateway;
        this.transferProducerGateway = transferProducerGateway;
        this.logger = logger;
    }

    public TransferData execute(String auth, UUID id) {
        
        Account refundRecipient;
        Account refundIssuer;
        Money value;
        Transfer refundingTransfer = transferGateway.findById(id);

        logger.info("Starting transfer refund request verification", Map.of(TransferLogField.TRANSFER_ID, id));
        
        if (refundingTransfer == null){
            throw new NotFoundException("Transfer not found.");
        }

        logger.info(
            "Starting transfer refund request",
            Map.of(
                TransferLogField.TRANSFER_ID, refundingTransfer.getId(),
                TransferLogField.TRANSFER_PAYER, refundingTransfer.getPayer().getId(),
                TransferLogField.TRANSFER_PAYEE, refundingTransfer.getPayee().getId(),
                TransferLogField.TRANSFER_VALUE, refundingTransfer.getValue().getMoneyInReal()
            )
        );

        refundRecipient = refundingTransfer.getPayer();
        refundIssuer = refundingTransfer.getPayee();
        value = refundingTransfer.getValue();

        if (!auth.equals(refundIssuer.getIdentification().getIdentificationNumber())) {
            throw new BusinessException("Authenticated account is different from refund issuer account.");
        }

        if (refundIssuer.getBalanceInCents() < value.getMoneyInCents()) {
            throw new BusinessException("The transfer amount cannot exceed the refund issuer's balance.");
        }

        refundingTransfer.setId(null);
        refundingTransfer.setPayer(refundIssuer);
        refundingTransfer.setPayee(refundRecipient);
        TransferData transfer = new TransferData(value.getMoneyInReal(), refundIssuer.getId(), refundRecipient.getId());

        transferProducerGateway.send(transfer);

        logger.info(
            "Completing transfer refund request",
            Map.of(
                TransferLogField.TRANSFER_ID, refundingTransfer.getId(),
                TransferLogField.TRANSFER_PAYER, refundingTransfer.getPayer().getId(),
                TransferLogField.TRANSFER_PAYEE, refundingTransfer.getPayee().getId(),
                TransferLogField.TRANSFER_VALUE, refundingTransfer.getValue().getMoneyInReal()
            )
        );

        return transfer;
    }
}
