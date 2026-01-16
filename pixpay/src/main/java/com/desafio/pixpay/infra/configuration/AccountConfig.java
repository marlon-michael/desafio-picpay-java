package com.desafio.pixpay.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.EmailValidatorGateway;
import com.desafio.pixpay.core.gateways.IdentificationValidator;
import com.desafio.pixpay.core.usecases.CreateAccountUseCase;
import com.desafio.pixpay.infra.persistence.JpaAccountRepository;
import com.desafio.pixpay.infra.validation.JMailValidator;
import com.desafio.pixpay.infra.validation.JakartaIdentificationValidator;
import com.desafio.pixpay.infra.persistence.AccountRepository;

@Configuration
public class AccountConfig {

    @Bean
    public AccountGateway accountGateway(JpaAccountRepository jpaAccountRepository){
        return new AccountRepository(jpaAccountRepository);
    }

    @Bean
    public EmailValidatorGateway emailValidatorGateway(){
        return new JMailValidator();
    }

    @Bean
    public IdentificationValidator identificationValidator(){
        return new JakartaIdentificationValidator();
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase(AccountGateway accountGateway, EmailValidatorGateway emailValidatorGateway){
        return new CreateAccountUseCase(accountGateway, emailValidatorGateway);
    }
}
