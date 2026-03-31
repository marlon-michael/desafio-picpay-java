package com.desafio.pixpay.core.usecases;


import java.util.Set;
import java.util.UUID;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.FullName;
import com.desafio.pixpay.core.domain.account.Password;
import com.desafio.pixpay.core.domain.account.Role;
import com.desafio.pixpay.core.domain.email.Email;
import com.desafio.pixpay.core.domain.identification.IdentificationFactory;
import com.desafio.pixpay.core.domain.identification.IdentificationTypeEnum;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.exceptions.BusinessException;
import com.desafio.pixpay.core.exceptions.InternalServerException;
import com.desafio.pixpay.core.exceptions.UuidAlreadyExistsException;
import com.desafio.pixpay.core.gateways.AccountGateway;
import com.desafio.pixpay.core.gateways.EmailValidatorGateway;
import com.desafio.pixpay.core.gateways.PasswordEncoderGateway;
import com.desafio.pixpay.core.usecases.data.CreateAccountInput;


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
            UUID.randomUUID(),
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

        if(accountGateway.findByIdentificationNumber(account.getIdentification().getIdentificationNumber()) != null){
            throw new BusinessException("Account identification number already in use.");
        }

        boolean error = false;
        int times = 0;
        do{
            try {
                accountGateway.create(account);
                error = false;
            } catch (UuidAlreadyExistsException exception) {
                account.setId(UUID.randomUUID());
                error = true;
                times++;
                if (times > 5) throw new InternalServerException("Internar error");
            } catch(Exception exception){
                error = true;
                times++;
                if (times > 5) throw new InternalServerException("Internar error");
            }
        }while(error);

        return account;
    }
}
