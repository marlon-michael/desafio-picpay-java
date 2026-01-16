package com.desafio.pixpay.adapters;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.desafio.pixpay.adapters.dtos.SaveAccountDTO;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@ActiveProfiles("test") // Usa o application-test.properties
@Transactional
// @Rollback(false)
public class AccountControllerAdapterTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateAccount() throws Exception {
        SaveAccountDTO saveAccountDTO = new SaveAccountDTO(
            "PERSONAL",
            "CadastroDePessoaFisica",
            "07007826997",
            "Marlon Michael",
            "marlonmich7@gmail.com",
            "Qypu4013?"
        );

        String accountJson = objectMapper.writeValueAsString(saveAccountDTO);

        mockMvc.perform(post("/accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(accountJson))
            .andExpect(status().isCreated())
            .andExpect(content().string("Account Marlon Michael created successfully"));
    }

    @Test
    void shouldThrowCPFInvalidException() throws Exception {
        SaveAccountDTO saveAccountDTO = new SaveAccountDTO(
            "PERSONAL",
            "CadastroDePessoaFisica",
            "12345678910",
            "John Doe",
            "john@example.com",
            "Password1!"
        );

        String accountJson = objectMapper.writeValueAsString(saveAccountDTO);

        mockMvc.perform(post("/accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(accountJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Invalid Identification number."));
    }

}
