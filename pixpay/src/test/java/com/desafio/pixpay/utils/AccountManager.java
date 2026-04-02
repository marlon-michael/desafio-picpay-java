package com.desafio.pixpay.utils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.desafio.pixpay.adapters.dtos.AuthenticationDTO;
import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.infra.persistence.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;

@Component
public class AccountManager {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    public AccountManager register(Account account) {
        if (accountRepository.findByIdentificationNumber(account.getIdentification().getIdentificationNumber()) == null){
            accountRepository.create(account);
        }
        return this;
    }

    public Cookie authenticate(String username, String password) {
        String login;
        MvcResult result;
        try {
            login = objectMapper.writeValueAsString(new AuthenticationDTO(username, password));
            result = mockMvc.perform(
                post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(login)
            )
            .andExpect(status().isOk())
            .andReturn();
            return result.getResponse().getCookie("jwt-token");
        } catch (Exception e) {
            throw new RuntimeException("Couldn't perform login with provided account");
        }
    }
}
