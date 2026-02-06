package com.desafio.pixpay.infra.persistence.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.pixpay.infra.persistence.entity.AccountEntity;


public interface JpaAccountRepository extends JpaRepository<AccountEntity, UUID> {

    AccountEntity findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE AccountEntity a SET a.balanceInPipsOfReal = :balance WHERE a.id = :id")
    int updateAccountBalanceById(@Param("id") UUID id, @Param("balance") Long balance);

}
