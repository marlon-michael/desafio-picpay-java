package com.desafio.pixpay.core.domain.account;

import com.desafio.pixpay.core.exceptions.BusinessException;

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
        throw new BusinessException("Invalid account type: " + value);
    }
}
