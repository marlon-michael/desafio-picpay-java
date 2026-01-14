package com.desafio.pixpay.core.domain;

import java.util.UUID;


public class Account {
    UUID id;
    String identificationNumber;
    String fullName;
    String email;
    String password;
    Long balanceInPipsOfReal;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getBalanceInPipsOfReal() {
        return balanceInPipsOfReal;
    }

    public void setBalanceInPipsOfReal(Long balanceInPipsOfReal) {
        this.balanceInPipsOfReal = balanceInPipsOfReal;
    }
}