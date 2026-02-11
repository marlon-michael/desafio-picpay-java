package com.desafio.pixpay.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.desafio.pixpay.core.domain.identification.IdentificationFactory;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.EmailValidatorGateway;
import com.desafio.pixpay.core.gateways.IdentificationValidatorGateway;
import com.desafio.pixpay.core.gateways.PasswordEncoderGateway;
import com.desafio.pixpay.core.usecases.CreateAccountUseCase;
import com.desafio.pixpay.infra.validation.JMailValidator;
import com.desafio.pixpay.infra.validation.JakartaIdentificationValidator;
import com.desafio.pixpay.infra.persistence.jpa.JpaAccountRepository;
import com.desafio.pixpay.infra.persistence.mapper.AccountMapper;
import com.desafio.pixpay.infra.persistence.repository.AccountRepository;

@Configuration
public class AccountConfig {

    @Bean
    AccountMapper accountMapper(){
        return new AccountMapper();
    }

    @Bean
    AccountGateway accountGateway(JpaAccountRepository jpaAccountRepository){
        return new AccountRepository(jpaAccountRepository);
    }
    
    @Bean
    CreateAccountUseCase createAccountUseCase(AccountGateway  accountGateway, EmailValidatorGateway emailValidatorGateway, PasswordEncoderGateway passwordEncoderGateway){
        return new CreateAccountUseCase(accountGateway, emailValidatorGateway, passwordEncoderGateway);
    }
    
    @Bean
    EmailValidatorGateway emailValidatorGateway(){
        return new JMailValidator();
    }
    
    @Bean
    IdentificationValidatorGateway identificationValidatorGateway(){
        return new JakartaIdentificationValidator();
    }

    @Bean
    boolean identificationFactory(IdentificationValidatorGateway identificationValidatorGateway){
        IdentificationFactory.initIdentificationFactory(identificationValidatorGateway);
        return true;
    }
}
