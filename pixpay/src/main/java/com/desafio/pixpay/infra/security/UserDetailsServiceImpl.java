package com.desafio.pixpay.infra.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.desafio.pixpay.infra.persistence.AccountEntity;
import com.desafio.pixpay.infra.persistence.AccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private AccountRepository accountRepository;

    public UserDetailsServiceImpl(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity accountEntity = accountRepository.findAccountByEmail(username);
        if (accountEntity == null) throw new UsernameNotFoundException("Username not found: "+username);
        return new UserAuthenticated(accountEntity);
    }
    
}
