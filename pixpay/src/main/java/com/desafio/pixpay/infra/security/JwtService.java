package com.desafio.pixpay.infra.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private JwtEncoder encoder;

    public JwtService(JwtEncoder encoder){
        this.encoder = encoder;
    }

    public String generateToken(Authentication auth){
        Instant now = Instant.now();
        Long durationInSeconds = 86400L;

        String scope = auth
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet
            .builder()
            .issuer("spring-security-jwt")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(durationInSeconds))
            .subject(auth.getName())
            .claim("scope", scope)
            .build();

        return encoder
            .encode(JwtEncoderParameters.from(claims))
            .getTokenValue();
    }
}
