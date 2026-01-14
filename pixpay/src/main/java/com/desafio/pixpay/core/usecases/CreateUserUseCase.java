package com.desafio.pixpay.core.usecases;


import com.desafio.pixpay.core.domain.User;
import com.desafio.pixpay.core.gateways.UserGateway;

public class CreateUserUseCase {
    private UserGateway userGateway;

    public CreateUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(User user){
        userGateway.saveUser(user);
        return user;
    }
}
