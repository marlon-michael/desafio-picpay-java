package com.desafio.pixpay.adapters.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.pixpay.adapters.dtos.TransferDTO;
import com.desafio.pixpay.utils.AccountFactory;
import com.desafio.pixpay.utils.AccountManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class TransferControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountManager accountManager;

    private Cookie personalCookie;

    private Cookie getPersonalCookie(){
        if (personalCookie != null) return personalCookie;
        personalCookie = accountManager
        .register(AccountFactory.createPersonal())
        .authenticate(
            AccountFactory.createPersonal().getIdentification().getIdentificationNumber(),
            AccountFactory.defaultPassword
        );
        return personalCookie;
    }

    @Test
    void ShouldRequestTransfer() throws Exception{
        TransferDTO transfer = new TransferDTO(
            null,
            10.0, 
            AccountFactory.createPersonal().getId(), 
            AccountFactory.createBusiness().getId()
        );

        String json = objectMapper.writeValueAsString(transfer);
        mockMvc.perform(
            post("/transfer")
            .cookie(getPersonalCookie())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        )
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.id").isEmpty())
        .andExpect(jsonPath("$.payer").exists())
        .andExpect(jsonPath("$.payee").exists())
        .andExpect(jsonPath("$.value").exists());
    }

    @Test
    void shouldHaveProcessTransfer(){
        // ?
    }

    @Test
    void shouldBlockTransfer(){
        // ?
    }
    
}