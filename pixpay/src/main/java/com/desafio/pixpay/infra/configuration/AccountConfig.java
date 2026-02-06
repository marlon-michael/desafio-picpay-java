package com.desafio.pixpay.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.EmailValidatorGateway;
import com.desafio.pixpay.core.gateways.IdentificationValidatorGateway;
import com.desafio.pixpay.core.gateways.PasswordEncoderGateway;
import com.desafio.pixpay.core.service.AccountValidatorService;
import com.desafio.pixpay.core.usecases.CreateAccountUseCase;
import com.desafio.pixpay.infra.security.PasswordEncoderImpl;
import com.desafio.pixpay.infra.validation.JMailValidator;
import com.desafio.pixpay.infra.validation.JakartaIdentificationValidator;
import com.desafio.pixpay.infra.persistence.jpa.JpaAccountRepository;
import com.desafio.pixpay.infra.persistence.mapper.AccountMapper;
import com.desafio.pixpay.infra.persistence.repository.AccountRepository;

@Configuration
public class AccountConfig {

    @Bean
    PasswordEncoderGateway passwordEncoderGateway(PasswordEncoder passwordEncoder) {
        return new PasswordEncoderImpl(passwordEncoder);
    }

    @Bean
    AccountMapper accountMapper(AccountValidatorService accountValidatorService){
        return new AccountMapper(accountValidatorService);
    }

    @Bean
    AccountGateway accountGateway(JpaAccountRepository jpaAccountRepository){
        return new AccountRepository(jpaAccountRepository);
    }
    
    @Bean
    CreateAccountUseCase createAccountUseCase(AccountGateway  accountGateway, AccountValidatorService accountValidatorService, PasswordEncoderGateway passwordEncoderGateway){
        return new CreateAccountUseCase(accountGateway, accountValidatorService, passwordEncoderGateway);
    }

    @Bean
    AccountValidatorService accountValidatorService(EmailValidatorGateway emailValidatorGateway, IdentificationValidatorGateway identificationValidatorGateway){
        return new AccountValidatorService(identificationValidatorGateway, emailValidatorGateway);
    }

    @Bean
    EmailValidatorGateway emailValidatorGateway(){
        return new JMailValidator();
    }

    @Bean
    IdentificationValidatorGateway identificationValidator(){
        return new JakartaIdentificationValidator();
    }
}
