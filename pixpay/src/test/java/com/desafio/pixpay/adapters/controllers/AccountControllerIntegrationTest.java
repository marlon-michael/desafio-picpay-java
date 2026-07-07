package com.desafio.pixpay.adapters.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.pixpay.infra.TestContainers;
import com.desafio.pixpay.utils.AccountFactory;
import com.desafio.pixpay.utils.AccountManager;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestContainers.class)
@Transactional
@Rollback(true)
public class AccountControllerIntegrationTest {

    @Autowired
    private AccountManager accountManager;
    
    @Autowired
    private MockMvc mockMvc;

    private Cookie managerCookie;
    
    private Cookie getManagerCookie(){
        if (managerCookie != null) return managerCookie;
        managerCookie = accountManager
        .register(AccountFactory.createManager())
        .authenticate(
            AccountFactory.createManager().getIdentification().getIdentificationNumber(),
            AccountFactory.defaultPassword
        );
        return managerCookie;
    }

    @Test
    void shouldListAccounts() throws Exception{
        mockMvc.perform(
            get("/accounts/all")
            .cookie(getManagerCookie())
        )
        .andExpect(jsonPath("$.content").exists())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content[0].id").exists())
        .andExpect(jsonPath("$.content[0].fullName").exists())
        .andExpect(jsonPath("$.content[0].email").exists())
        .andExpect(jsonPath("$.content[0].roles").exists())
        .andExpect(status().isOk());
    }
}
