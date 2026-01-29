package com.desafio.pixpay.infra.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private JwtService jwtService;

    public AuthenticationService(JwtService jwtService){
        this.jwtService = jwtService;
    }

    public String authenticate(Authentication auth){
        return jwtService.generateToken(auth);
    }
}
