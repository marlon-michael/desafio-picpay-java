package com.desafio.pixpay.adapters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pixpay.core.domain.User;
import com.desafio.pixpay.core.usecases.CreateUserUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private CreateUserUseCase createUserUseCase;

    
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        User createdUser = createUserUseCase.execute(user);
        System.out.println(user.getId());
        System.out.println(user.getIdentificacao());
        System.out.println(user.getNomeCompleto());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        System.out.println(user.getSaldo());
        return ResponseEntity.status(201).body("User " + createdUser.getNomeCompleto() + " created successfully");
    }
}
