package com.desafio.pixpay.infra.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAccountRepository extends JpaRepository<AccountEntity, UUID> {

    AccountEntity findByEmail(String email);

}
