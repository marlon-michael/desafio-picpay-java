package com.desafio.pixpay.adapters;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import com.desafio.pixpay.adapters.dtos.SaveAccountDTO;
import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.infra.persistence.repository.AccountRepository;
import com.desafio.pixpay.utils.AccountFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class AccountControllerAdapterTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private Cookie managerCookie;
    private Cookie businessCokie;
    private Cookie personalCookie;
    private Account managerAccount;
    private Account businessAccount;
    private Account personalAccount;

    Cookie performLogin(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(
            post("/authenticate")
            .with(httpBasic(username, password))
        )
        .andExpect(status().isOk())
        .andReturn();
        return result.getResponse().getCookie("jwt-token");
    }

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();

        setupCreateAccounts();
        setupLoginAccounts();
    }

    void setupCreateAccounts() {
        managerAccount = AccountFactory.createManager();
        businessAccount = AccountFactory.createBusiness();
        personalAccount = AccountFactory.createPersonal();
        
        if (accountRepository.findByIdentificationNumber(managerAccount.getIdentification().getIdentificationNumber()) == null){
            accountRepository.create(managerAccount);
        }
        if (accountRepository.findByIdentificationNumber(businessAccount.getIdentification().getIdentificationNumber()) == null){
            accountRepository.create(businessAccount);
        }
        if (accountRepository.findByIdentificationNumber(personalAccount.getIdentification().getIdentificationNumber()) == null){
            accountRepository.create(personalAccount);
        }
    }

    void setupLoginAccounts() throws Exception{
        managerCookie = performLogin(managerAccount.getIdentification().getIdentificationNumber(), managerAccount.getIdentification().getIdentificationNumber());
        if (managerCookie.getValue() == null) throw new InvalidCookieException(null);
        businessCokie = performLogin(businessAccount.getIdentification().getIdentificationNumber(), businessAccount.getIdentification().getIdentificationNumber());
        if (businessCokie.getValue() == null) throw new InvalidCookieException(null);
        personalCookie = performLogin(personalAccount.getIdentification().getIdentificationNumber(), personalAccount.getIdentification().getIdentificationNumber());
        if (personalCookie.getValue() == null) throw new InvalidCookieException(null);
    }

    @Test
    void shouldListAccounts() throws Exception{
        mockMvc.perform(
            get("/accounts")
            .cookie(managerCookie)
        )
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").exists())
        .andExpect(jsonPath("$[0].fullName").exists())
        .andExpect(jsonPath("$[0].email").exists())
        .andExpect(jsonPath("$[0].roles").exists())
        .andExpect(status().isOk());
    }

    @Test
    void shouldCreateAccount() throws Exception {
        SaveAccountDTO saveAccountDTO = new SaveAccountDTO(
            "CadastroDePessoaFisica",
            "54104171000",
            "Marlon Michael",
            "marlon@gmail.com",
            "Marlon@Michael1"
        );

        String accountJson = objectMapper.writeValueAsString(saveAccountDTO);

        mockMvc.perform(
            post("/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(accountJson)
        )
        .andExpect(content().string("Account Marlon Michael created successfully."))
        .andExpect(status().isCreated());
    }

    @Test
    void shouldThrowCPFInvalidException() throws Exception {
        SaveAccountDTO saveAccountDTO = new SaveAccountDTO(
            "CadastroDePessoaFisica",
            "12345678910",
            "John Doe",
            "john@example.com",
            "Password1!"
        );

        String accountJson = objectMapper.writeValueAsString(saveAccountDTO);

        mockMvc.perform(post("/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(accountJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Invalid Identification number."));
    }

}
