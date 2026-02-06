package com.desafio.pixpay.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.TransferGateway;
import com.desafio.pixpay.core.usecases.TransferMoneyUseCase;
import com.desafio.pixpay.infra.persistence.jpa.JpaTransferRepository;
import com.desafio.pixpay.infra.persistence.repository.TransferRepository;

import jakarta.transaction.Transactional;


@Configuration
public class TransferConfig {

    @Bean
    TransferGateway transferGateway(JpaTransferRepository jpaTransferRepository){
        return new TransferRepository(jpaTransferRepository);
    }

    @Bean
    @Transactional
    TransferMoneyUseCase transferMoneyUseCase(AccountGateway accountGateway, TransferGateway transferGateway){
        return new TransferMoneyUseCase(accountGateway, transferGateway);
    }
}
