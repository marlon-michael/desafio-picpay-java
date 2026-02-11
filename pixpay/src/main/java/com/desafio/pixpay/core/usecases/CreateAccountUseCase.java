package com.desafio.pixpay.core.usecases;


import java.util.Set;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.FullName;
import com.desafio.pixpay.core.domain.account.Password;
import com.desafio.pixpay.core.domain.account.Role;
import com.desafio.pixpay.core.domain.email.Email;
import com.desafio.pixpay.core.domain.identification.IdentificationFactory;
import com.desafio.pixpay.core.domain.identification.IdentificationTypeEnum;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.EmailValidatorGateway;
import com.desafio.pixpay.core.gateways.PasswordEncoderGateway;
import com.desafio.pixpay.core.usecases.input.CreateAccountInput;


public class CreateAccountUseCase {
    private final AccountGateway accountGateway;
    private final PasswordEncoderGateway passwordEncoder;
    private final EmailValidatorGateway emailValidatorGateway;

    public CreateAccountUseCase(AccountGateway accountGateway, EmailValidatorGateway emailValidatorGateway, PasswordEncoderGateway passwordEncoder) {
        this.accountGateway = accountGateway;
        this.emailValidatorGateway = emailValidatorGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public Account execute(CreateAccountInput createAccountInput) {
        Account account = new Account(
            Set.of(Role.ROLE_USER),
            IdentificationFactory.createIdentification(
                IdentificationTypeEnum.fromValue(
                    createAccountInput.getIdentificationType()
                ), 
                createAccountInput.getIdentificationNumber()
            ),
            new FullName().setFullnameAndValidate(createAccountInput.getFullName()),
            new Email().setEmailAndValidate(createAccountInput.getEmail(), emailValidatorGateway),
            new Password().setPasswordAndValidate(createAccountInput.getPassword(), passwordEncoder),
            new Money(10000L)
        );

        if(accountGateway.findAccountByIdentificationNumber(account.getIdentification().getIdentificationNumber()) != null){
            throw new IllegalArgumentException("Account identification number already in use.");
        }

        accountGateway.createAccount(account);
        return account;
    }
}
