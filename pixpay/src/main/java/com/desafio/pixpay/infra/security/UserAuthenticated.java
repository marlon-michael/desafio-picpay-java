package com.desafio.pixpay.infra.security;

import java.util.Collection;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.desafio.pixpay.infra.persistence.entity.AccountEntity;

public class UserAuthenticated implements UserDetails {

    private AccountEntity accountEntity;

    public UserAuthenticated(AccountEntity accountEntity){
        this.accountEntity = accountEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.accountEntity
            .getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getValue()))
            .toList();
    }

    @Override
    public @Nullable String getPassword() {
        return accountEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return accountEntity.getEmail();
    }
    
}
