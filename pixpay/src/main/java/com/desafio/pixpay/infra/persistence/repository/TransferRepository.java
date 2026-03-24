package com.desafio.pixpay.infra.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.exceptions.UuidAlreadyExistsException;
import com.desafio.pixpay.core.gateways.TransferGateway;
import com.desafio.pixpay.infra.persistence.entity.TransferEntity;
import com.desafio.pixpay.infra.persistence.jpa.JpaTransferRepository;
import com.desafio.pixpay.infra.persistence.mapper.TransferMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class TransferRepository implements TransferGateway {

    private final RedisTemplate<String, String> redis;
    private final ObjectMapper objectMapper;
    private final JpaTransferRepository jpaTransferRepository;

    public TransferRepository(JpaTransferRepository jpaTransferRepository, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.jpaTransferRepository = jpaTransferRepository;
        this.redis = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Transfer create(Transfer transfer) {
        redis.delete("transfers");

        try {
            jpaTransferRepository.save(TransferMapper.fromDomainToEntity(transfer));
        } catch (DataIntegrityViolationException exception) {
            throw new UuidAlreadyExistsException("UUID already exists in database.");
        }

        return transfer;
    }

    @Override
    public List<Transfer> findAll() {
        List<TransferEntity> transfers;
        String json = redis.opsForValue().get("transfers");

        if (json != null) {
            try {
                transfers = objectMapper.readValue(json, new TypeReference<List<TransferEntity>>() {});
                return transfers
                    .stream()
                    .map(entity -> TransferMapper.fromEntityToDomain(entity))
                    .toList(); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        transfers = jpaTransferRepository.findAll();

        try {
            redis.opsForValue().set("transfers", objectMapper.writeValueAsString(transfers));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transfers
            .stream()
            .map(entity -> TransferMapper.fromEntityToDomain(entity))
            .toList();
    }

    @Override
    public Transfer findById(UUID id) {
        TransferEntity transfer;
        Optional<TransferEntity> transferOptional = jpaTransferRepository.findById(id);
        String json = redis.opsForValue().get("transfer:"+id);

        if (json != null) {
            try {
                transfer = objectMapper.readValue(json, TransferEntity.class);
                return TransferMapper.fromEntityToDomain(transfer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (transferOptional.isEmpty()) throw new BusinessException("Transfer not found with privided id");
        transfer = transferOptional.get();

        try {
            redis.opsForValue().set("transfer:"+transfer.getId(), objectMapper.writeValueAsString(transfer));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        
        return TransferMapper.fromEntityToDomain(transfer);
    }
}
