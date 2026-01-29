package com.desafio.pixpay.infra.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.desafio.pixpay.core.gateways.PasswordEncoderGateway;

public class PasswordEncoderImpl implements PasswordEncoderGateway{

    private PasswordEncoder passwordEncoder;

    public PasswordEncoderImpl(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    } 
}
