package com.desafio.pixpay.core.domain.account;

public enum Role {
    ROLE_USER("USER"),
    ROLE_MANAGER("MANAGER");

    private final String role;

    Role(String role){
        this.role = role;
    }

    public String getValue(){
        return this.role;
    }

    public static Role fromValue(String value){
        for (Role role : Role.values()) {
            if(role.getValue() == value) return role;
        }
        throw new IllegalArgumentException("Invalid role.");
    }
}
