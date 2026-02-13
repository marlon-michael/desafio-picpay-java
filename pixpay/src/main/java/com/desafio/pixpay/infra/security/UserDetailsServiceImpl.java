package com.desafio.pixpay.infra.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.desafio.pixpay.infra.persistence.entity.AccountEntity;
import com.desafio.pixpay.infra.persistence.mapper.AccountMapper;
import com.desafio.pixpay.infra.persistence.repository.AccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private AccountRepository accountRepository;

    public UserDetailsServiceImpl(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identification) throws UsernameNotFoundException {
        AccountEntity accountEntity = AccountMapper.fromDomainToEntity(accountRepository.findByIdentificationNumber(identification));
        if (accountEntity == null) throw new UsernameNotFoundException("Identifiation number not found: "+identification);
        return new UserAuthenticated(accountEntity);
    }
    
}
