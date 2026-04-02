package com.desafio.pixpay.adapters.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.pixpay.adapters.dtos.SaveAccountDTO;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateBusinessAccount() throws Exception{
        SaveAccountDTO AccountDTO = new SaveAccountDTO(
            "CadastroNacionalDePessoaJuridica",
            "34547820000110",
            "Business Account",
            "business@business.com",
            "Abcd1234?"
        );

        String accountJson = objectMapper.writeValueAsString(AccountDTO);

        mockMvc.perform(
            post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(accountJson)
        )
        .andExpect(content().string("Account Business Account created successfully."))
        .andExpect(status().isCreated());
    }

    @Test
    void shouldCreateAccount() throws Exception {
        SaveAccountDTO AccountDTO = new SaveAccountDTO(
            "CadastroDePessoaFisica",
            "94900666050",
            "Personal Account",
            "personal@personal.com",
            "Abcd1234?"
        );

        String accountJson = objectMapper.writeValueAsString(AccountDTO);

        mockMvc.perform(
            post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(accountJson)
        )
        .andExpect(content().string("Account Personal Account created successfully."))
        .andExpect(status().isCreated());
    }

    @Test
    void shouldThrowInvalidIdentificationTypeException() throws Exception {
        SaveAccountDTO AccountDTO = new SaveAccountDTO(
            "Passaporte",
            "12345678910",
            "John Doe",
            "john@example.com",
            "Abcd1234?"
        );

        String accountJson = objectMapper.writeValueAsString(AccountDTO);

        mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(accountJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Invalid identification type: Passaporte"));
    }

    @Test
    void shouldThrowInvalidIdentificationNumberException() throws Exception {
        SaveAccountDTO AccountDTO = new SaveAccountDTO(
            "CadastroDePessoaFisica",
            "12345678910",
            "John Doe",
            "john@example.com",
            "Abcd1234?"
        );

        String accountJson = objectMapper.writeValueAsString(AccountDTO);

        mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(accountJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Invalid Identification number."));
    }
    
    @Test
    void shouldThroInvalidEmailException() throws Exception {
        SaveAccountDTO AccountDTO = new SaveAccountDTO(
            "CadastroDePessoaFisica",
            "88886155042",
            "John Doe",
            "example.com",
            "Abcd1234?"
        );

        String accountJson = objectMapper.writeValueAsString(AccountDTO);
        
        mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(accountJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid email format."));
    }

    @Test
    void shouldThroInvalidPasswordException() throws Exception {
        SaveAccountDTO AccountDTO = new SaveAccountDTO(
            "CadastroDePessoaFisica",
            "88886155042",
            "John Doe",
            "john@example.com",
            "Abcd1234"
        );

        String accountJson = objectMapper.writeValueAsString(AccountDTO);

        mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(accountJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("The password must contain one uppercase letter, one lowercase letter, one number and at least one of this special character: , . ! @ # $ & % ? _ +."));
    }

}
