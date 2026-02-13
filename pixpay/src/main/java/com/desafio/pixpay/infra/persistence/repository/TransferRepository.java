package com.desafio.pixpay.infra.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.exceptions.UuidAlreadyExistsException;
import com.desafio.pixpay.core.gateways.TransferGateway;
import com.desafio.pixpay.infra.persistence.entity.TransferEntity;
import com.desafio.pixpay.infra.persistence.jpa.JpaTransferRepository;
import com.desafio.pixpay.infra.persistence.mapper.TransferMapper;

@Repository
public class TransferRepository implements TransferGateway {

    JpaTransferRepository jpaTransferRepository;

    public TransferRepository(JpaTransferRepository jpaTransferRepository) {
        this.jpaTransferRepository = jpaTransferRepository;
    }

    @Override
    public Transfer create(Transfer transfer) {
        try {
            jpaTransferRepository.save(TransferMapper.fromDomainToEntity(transfer));
        } catch (DataIntegrityViolationException exception) {
            throw new UuidAlreadyExistsException("UUID already exists in database.");
        }
        return transfer;
    }

    @Override
    public List<Transfer> findAll() {
        return jpaTransferRepository
            .findAll()
            .stream()
            .map(entity -> TransferMapper.fromEntityToDomain(entity))
            .toList();
    }

    @Override
    public Transfer findById(UUID id) {
        Optional<TransferEntity> transfer = jpaTransferRepository.findById(id);
        if (transfer.isEmpty()) throw new IllegalArgumentException("Transfer not found with privided id");
        return TransferMapper.fromEntityToDomain(transfer.get());
    }
}
