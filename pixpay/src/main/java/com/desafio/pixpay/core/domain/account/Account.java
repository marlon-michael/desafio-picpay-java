package com.desafio.pixpay.core.domain.account;

import java.util.UUID;

import com.desafio.pixpay.core.domain.common.Email;
import com.desafio.pixpay.core.domain.identification.Identification;
import com.desafio.pixpay.core.domain.identification.IdentificationTypeEnum;
import com.desafio.pixpay.core.domain.money.Money;


public class Account {
    UUID id;
    Identification identification;
    FullName fullName;
    Email email;
    Password password;
    Money balance = new Money();

    public Account(){}

    public Account(Identification identification, FullName fullName, Email email, Password password, Money balance) {
        setId(UUID.randomUUID());
        setIdentification(identification);
        setFullName(fullName);
        setEmail(email);
        setPassword(password);
        setBalance(balance);
    }

    public Account(UUID id, Identification identification, FullName fullName, Email email, Password password, Money balance) {
        this.id = id;
        this.identification = identification;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public Account setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public Account setIdentification(Identification identification){
        if (this.identification != null && identification.getIdentificationAccountType() != this.getAccountType()) {
            throw new IllegalArgumentException(
                "Account identification type cannot change: actual type: "+this.getAccountType()+", new type: "+identification.getIdentificationAccountType()
            );
        }
        this.identification = identification;
        return this;
    }

    public Identification getIdentification(){
        return this.identification;
    }

    public AccountTypeEnum getAccountType() {
        return this.identification.getIdentificationAccountType();
    }

    public IdentificationTypeEnum getIdentificationType() {
        return this.identification.getIdentificationType();
    }

    public Account setFullName(FullName fullName) {
        this.fullName = fullName;
        return this;
    }

    public FullName getFullName() {
        return fullName;
    }

    public Account setEmail(Email email) {
        this.email = email;
        return this;
    }

    public Email getEmail() {
        return email;
    }

    public Account setPassword(Password password) {
        this.password = password;
        return this;
    }

    public Password getPassword() {
        return password;
    }

    public Account setBalance(Money balance){
        this.balance = balance;
        return this;
    }

    public Account setBalanceInPipsOfReal(Long balanceInPips){
        this.balance.setMoneyInPips(balanceInPips);
        return this;
    }
    
    public Account setBalanceInReal(Double balanceInReal) {
        this.balance.setMoneyInCurrency(balanceInReal);
        return this;
    }

    public Money getBalance(){
        return this.balance;
    }

    public Long getBalanceInPipsOfReal() {
        return balance.getMoneyInPips();
    }
    
    public Double getBalanceInReal() {
        return balance.getMoneyInCurrency();
    }
}
