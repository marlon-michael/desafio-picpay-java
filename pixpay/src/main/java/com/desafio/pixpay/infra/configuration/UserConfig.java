package com.desafio.pixpay.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.desafio.pixpay.core.gateways.UserGateway;
import com.desafio.pixpay.core.usecases.CreateUserUseCase;
import com.desafio.pixpay.infra.persistence.JpaUserRepository;
import com.desafio.pixpay.infra.persistence.UserRepository;

@Configuration
public class UserConfig {

    @Bean
    public UserGateway userGateway(JpaUserRepository jpaUserRepository){
        return new UserRepository(jpaUserRepository);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserGateway userGateway) {
        return new CreateUserUseCase(userGateway);
    }
}
