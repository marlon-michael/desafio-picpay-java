package com.desafio.pixpay.infra.security;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.desafio.pixpay.infra.persistence.entity.AccountEntity;

public class UserAuthenticated implements UserDetails {

    private AccountEntity accountEntity;

    public UserAuthenticated(AccountEntity accountEntity){
        this.accountEntity = accountEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "read");
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
