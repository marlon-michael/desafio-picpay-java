package com.desafio.pixpay.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.EmailValidatorGateway;
import com.desafio.pixpay.core.gateways.IdentificationValidatorGateway;
import com.desafio.pixpay.core.service.AccountValidatorService;
import com.desafio.pixpay.core.usecases.CreateAccountUseCase;
import com.desafio.pixpay.infra.persistence.JpaAccountRepository;
import com.desafio.pixpay.infra.validation.JMailValidator;
import com.desafio.pixpay.infra.validation.JakartaIdentificationValidator;
import com.desafio.pixpay.infra.persistence.AccountMapper;
import com.desafio.pixpay.infra.persistence.AccountRepository;

@Configuration
public class AccountConfig {

    @Bean
    public AccountMapper accountMapper(AccountValidatorService accountValidatorService){
        return new AccountMapper(accountValidatorService);
    }

    @Bean
    public AccountGateway accountGateway(JpaAccountRepository jpaAccountRepository, AccountMapper accountMapper){
        return new AccountRepository(jpaAccountRepository, accountMapper);
    }

    @Bean AccountValidatorService accountValidatorService(EmailValidatorGateway emailValidatorGateway, IdentificationValidatorGateway identificationValidatorGateway){
        return new AccountValidatorService(identificationValidatorGateway, emailValidatorGateway);
    }   

    @Bean
    public EmailValidatorGateway emailValidatorGateway(){
        return new JMailValidator();
    }

    @Bean
    public IdentificationValidatorGateway identificationValidator(){
        return new JakartaIdentificationValidator();
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase(AccountGateway  accountGateway, AccountValidatorService accountValidatorService){
        return new CreateAccountUseCase(accountGateway, accountValidatorService);
    }
}
