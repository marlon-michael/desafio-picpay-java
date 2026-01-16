package com.desafio.pixpay.core.domain;

public enum AccountTypeEnum {
    PERSONAL("PERSONAL"),
    BUSINESS("BUSINESS");

    private final String type;

    AccountTypeEnum(String type) {
        this.type = type;
    }

    public String getValue() {
        return type;
    }

    public static AccountTypeEnum fromValue(String value) {
        for (AccountTypeEnum accountType : AccountTypeEnum.values()) {
            if (accountType.getValue().equals(value)) {
                return accountType;
            }
        }
        throw new IllegalArgumentException("Invalid account type: " + value);
    }
}
