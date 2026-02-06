package com.desafio.pixpay.infra.persistence.mapper;

import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.infra.persistence.entity.TransferEntity;

public class TransferMapper {
    public static Transfer fromEntityToDomain(TransferEntity entity){
        return new Transfer(
            entity.getId(), 
            new Money(entity.getValue()), 
            AccountMapper.fromEntityToDomain(entity.getPayer()), 
            AccountMapper.fromEntityToDomain(entity.getPayee())
        );
    }

    public static TransferEntity fromDomainToEntity(Transfer domain){
        return new TransferEntity(
            domain.getId(), 
            domain.getValue().getMoneyInPips(), 
            AccountMapper.fromDomainToEntity(domain.getPayer()), 
            AccountMapper.fromDomainToEntity(domain.getPayee())
        );
    }
}
