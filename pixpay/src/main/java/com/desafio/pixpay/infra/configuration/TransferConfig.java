package com.desafio.pixpay.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.LoggerGateway;
import com.desafio.pixpay.core.gateways.NotifyTransferGateway;
import com.desafio.pixpay.core.gateways.TransferAuthorizerGateway;
import com.desafio.pixpay.core.gateways.TransferGateway;
import com.desafio.pixpay.core.gateways.TransferProducerGateway;
import com.desafio.pixpay.core.usecases.RefundTransferUsecase;
import com.desafio.pixpay.core.usecases.RequestTransferUsecase;
import com.desafio.pixpay.core.usecases.ListTransfersByManagerUseCase;
import com.desafio.pixpay.core.usecases.ListTransfersByUserUseCase;
import com.desafio.pixpay.core.usecases.ProcessTransferUseCase;
import com.desafio.pixpay.infra.client.TransferAuthorizer;
import com.desafio.pixpay.infra.events.KafkaNotificationTransferProducer;
import com.desafio.pixpay.infra.log.Slf4jLogger;
import com.desafio.pixpay.infra.persistence.jpa.JpaTransferRepository;
import com.desafio.pixpay.infra.persistence.repository.TransferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;


@Configuration
public class TransferConfig {

    @Bean
    LoggerGateway loggerGateway(){
        return new Slf4jLogger();
    }

    @Bean
    TransferGateway transferGateway(JpaTransferRepository jpaTransferRepository, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper){
        return new TransferRepository(jpaTransferRepository, redisTemplate, objectMapper);
    }

    @Bean
    TransferAuthorizerGateway transferAuthorizerGateway(){
        return new TransferAuthorizer();
    }

    @Bean
    NotifyTransferGateway notifyTransferGateway(){
        return new KafkaNotificationTransferProducer();
    }

    @Bean
    RequestTransferUsecase requestTransferUsecase(TransferProducerGateway transferProducerGateway, AccountGateway accountGateway, LoggerGateway logger){
        return new RequestTransferUsecase(transferProducerGateway, accountGateway, logger);
    }

    @Bean
    @Transactional
    ProcessTransferUseCase processTransferUseCase(AccountGateway accountGateway, TransferGateway transferGateway, TransferAuthorizerGateway transferAuthorizerGateway, NotifyTransferGateway notifyTransferGateway, LoggerGateway logger){
        return new ProcessTransferUseCase(accountGateway, transferGateway, transferAuthorizerGateway, notifyTransferGateway, logger);
    }

    @Bean
    ListTransfersByManagerUseCase listTransfersByManager(TransferGateway transferGateway){
        return new ListTransfersByManagerUseCase(transferGateway);
    }

    @Bean
    ListTransfersByUserUseCase listTransfersByUserUseCase(TransferGateway transferGateway, AccountGateway accountGateway){
        return new ListTransfersByUserUseCase(transferGateway, accountGateway);
    }

    @Bean
    RefundTransferUsecase refundTransferUsecase(TransferGateway transferGateway, TransferProducerGateway transferProducerGateway, LoggerGateway logger){
        return new RefundTransferUsecase(transferGateway, transferProducerGateway, logger);
    }
}
