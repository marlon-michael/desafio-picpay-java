package com.desafio.pixpay.core.usecases;

import java.util.List;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.TransferGateway;

public class ListTransfersByUserUseCase {

    private final TransferGateway transferGateway;
    private final AccountGateway accountGateway;

    public ListTransfersByUserUseCase(TransferGateway transferGateway, AccountGateway accountGateway) {
        this.transferGateway = transferGateway;
        this.accountGateway = accountGateway;
    }

    public List<Transfer> execute(String auth, Integer pageSize, Integer pageNumber){
        Account account = accountGateway.findByIdentificationNumber(auth);
        return transferGateway.findAllByAccountId(account.getId(), pageSize, pageNumber);
    }
}
