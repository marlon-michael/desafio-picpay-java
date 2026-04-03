package com.desafio.pixpay.adapters.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.pixpay.adapters.dtos.TransferDTO;
import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.FullName;
import com.desafio.pixpay.core.domain.email.Email;
import com.desafio.pixpay.core.domain.identification.CadastroDePessoaFisica;
import com.desafio.pixpay.core.domain.transfer.Transfer;
import com.desafio.pixpay.core.gateways.NotifyTransferGateway;
import com.desafio.pixpay.core.gateways.TransferAuthorizerGateway;
import com.desafio.pixpay.core.usecases.ProcessTransferUseCase;
import com.desafio.pixpay.core.usecases.data.TransferData;
import com.desafio.pixpay.infra.persistence.repository.AccountRepository;
import com.desafio.pixpay.infra.persistence.repository.TransferRepository;
import com.desafio.pixpay.utils.AccountFactory;
import com.desafio.pixpay.utils.AccountManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class TransferControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    ProcessTransferUseCase processTransferUseCase;

    @MockitoBean
    private TransferAuthorizerGateway transferAuthorizerGateway;

    @MockitoBean
    private NotifyTransferGateway notifyTransferGateway;

    private Account personalAccount = AccountFactory.createPersonal();
    private Account businessAccount = AccountFactory.createBusiness();

    private Cookie personalCookie;
    private Cookie businessCookie;

    private Cookie getPersonalCookie(){
        if (personalCookie != null) return personalCookie;
        personalCookie = accountManager
        .register(personalAccount)
        .authenticate(
            personalAccount.getIdentification().getIdentificationNumber(),
            AccountFactory.defaultPassword
        );
        return personalCookie;
    }

    private Cookie getBusinessCookie(){
        if (businessCookie != null) return businessCookie;
        businessCookie = accountManager
        .register(businessAccount)
        .authenticate(
            businessAccount.getIdentification().getIdentificationNumber(),
            AccountFactory.defaultPassword
        );
        return businessCookie;
    }
    
    @Test
    void ShouldRequestTransfer() throws Exception{
        getBusinessCookie();
        TransferDTO transfer = new TransferDTO(
            null,
            10.0, 
            personalAccount.getId(), 
            businessAccount.getId()
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
    void shouldProcessTransfer() throws Exception{
        Mockito.when(transferAuthorizerGateway.isAuthorized()).thenReturn(true);
        Mockito.when(notifyTransferGateway.send(any())).thenReturn(true);
        getPersonalCookie();
        getBusinessCookie();

        Double transferValue = 9.99;

        Account beforeRequestPayerAccount = accountRepository.findById(personalAccount.getId());
        assertNotNull(beforeRequestPayerAccount);

        Account beforeRequestPayeeAccount = accountRepository.findById(businessAccount.getId());
        assertNotNull(beforeRequestPayeeAccount);
        
        TransferData transferData = new TransferData(
            null,
            transferValue, 
            personalAccount.getId(), 
            businessAccount.getId()
        );

        Transfer transfer = processTransferUseCase.execute(transferData);
        entityManager.flush();
        entityManager.clear();
        
        Account afterRequestPayerAccount = accountRepository.findById(personalAccount.getId());
        assertNotNull(afterRequestPayerAccount);
        assert(beforeRequestPayerAccount.getBalanceInReal() - transferValue == afterRequestPayerAccount.getBalanceInReal());

        Account afterRequestPayeeAccount = accountRepository.findById(personalAccount.getId());
        assertNotNull(afterRequestPayeeAccount);
        assert(beforeRequestPayerAccount.getBalanceInReal() - transferValue == afterRequestPayeeAccount.getBalanceInReal());

        Transfer RequestedTransfer = transferRepository.findById(transfer.getId());
        assertNotNull(RequestedTransfer);
        assertEquals(transfer.getPayer().getId(), RequestedTransfer.getPayer().getId());
        assertEquals(transfer.getPayee().getId(), RequestedTransfer.getPayee().getId());
        assertEquals(transfer.getValue().getMoneyInReal(), RequestedTransfer.getValue().getMoneyInReal());
    }

    @Test
    void shouldRefuseTransferByMismatchingPayerAccount() throws Exception{
        getBusinessCookie();
        Account wrongAccountPayer = AccountFactory.createPersonal();
        wrongAccountPayer
            .setEmail(new Email().fromPersistence("wrong@email.com"))
            .setFullName(new FullName().fromPersistence("Wrong Person"))
            .setIdentification(new CadastroDePessoaFisica().fromPersistence("16683008000"));
        accountManager.register(wrongAccountPayer);
        TransferDTO transfer = new TransferDTO(
            null,
            10.0, 
            wrongAccountPayer.getId(), 
            businessAccount.getId()
        );

        String json = objectMapper.writeValueAsString(transfer);
        mockMvc.perform(
            post("/transfer")
            .cookie(getPersonalCookie())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        )
        .andExpect(content().string("Authenticated account is different from payer account."))
        .andExpect(status().is(422))
        ;
    }

    @Test
    void ShouldRefuseTransferByBusinessAccountCantTransfer() throws Exception{
        getPersonalCookie();
        TransferDTO transfer = new TransferDTO(
            null,
            10.0, 
            businessAccount.getId(),
            personalAccount.getId()
        );

        String json = objectMapper.writeValueAsString(transfer);
        mockMvc.perform(
            post("/transfer")
            .cookie(getBusinessCookie())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        )
        .andExpect(status().is(422))
        .andExpect(content().string("Business account type cannot transfer money to another account."))
        ;
    }
    
}