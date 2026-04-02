package com.desafio.pixpay.utils;

import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.desafio.pixpay.core.domain.account.Account;
import com.desafio.pixpay.core.domain.account.FullName;
import com.desafio.pixpay.core.domain.account.Password;
import com.desafio.pixpay.core.domain.account.Role;
import com.desafio.pixpay.core.domain.email.Email;
import com.desafio.pixpay.core.domain.identification.CadastroDePessoaFisica;
import com.desafio.pixpay.core.domain.identification.CadastroNacionalDePessoaJuridica;
import com.desafio.pixpay.core.domain.money.Money;
import com.desafio.pixpay.core.gateways.PasswordEncoderGateway;
import com.desafio.pixpay.infra.security.PasswordEncoderImpl;

public class AccountFactory {
    
    private static PasswordEncoderGateway passwordEncoderGateway = new PasswordEncoderImpl(new BCryptPasswordEncoder());

    public static String defaultPassword = "Abcd1234?";

    private static Account managerAccount = new Account(
        UUID.randomUUID(),
        Set.of(Role.ROLE_MANAGER, Role.ROLE_USER),
        new CadastroNacionalDePessoaJuridica().builder().fromPersistence("CAEH9506000177"),
        new FullName().fromPersistence("Manager Manager"),
        new Email().fromPersistence("manager@manager.com"), 
        new Password().fromPersistence(passwordEncoderGateway.encode(defaultPassword)),
        new Money()
    );

    private static Account businessAccount = new Account(
        UUID.randomUUID(),
        Set.of(Role.ROLE_USER),
        new CadastroNacionalDePessoaJuridica().builder().fromPersistence("34547820000110"),
        new FullName().fromPersistence("Business Business"),
        new Email().fromPersistence("business@business.com"), 
        new Password().fromPersistence(passwordEncoderGateway.encode(defaultPassword)),
        new Money(100000L)
    );

    private static Account personalAccount = new Account(
        UUID.randomUUID(),
        Set.of(Role.ROLE_USER),
        new CadastroDePessoaFisica().builder().fromPersistence("94900666050"),
        new FullName().fromPersistence("Personal Personal"),
        new Email().fromPersistence("personal@personal.com"), 
        new Password().fromPersistence(passwordEncoderGateway.encode(defaultPassword)),
        new Money(10000L)
    );

    public static Account createManager(){
        return managerAccount;
    }

    public static Account createBusiness(){
        return businessAccount;
    }

    public static Account createPersonal(){
        return personalAccount;
    }
}
