package com.desafio.pixpay.infra.persistence;

import org.springframework.stereotype.Repository;

import com.desafio.pixpay.core.domain.User;
import com.desafio.pixpay.core.gateways.UserGateway;

@Repository
public class UserRepository implements UserGateway {
    private final JpaUserRepository jpaUserRepository;

    public UserRepository(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void saveUser(User user) {
        jpaUserRepository.save(UserEntity.fromDomainToEntity(user));
    }
    
}
