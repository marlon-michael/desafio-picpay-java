package com.desafio.pixpay.infra.persistence.jpa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.desafio.pixpay.infra.persistence.entity.TransferEntity;

public interface JpaTransferRepository extends JpaRepository<TransferEntity, UUID>{
    public Page<TransferEntity> findAllByPayerId(UUID id, Pageable pageable);
}
