package com.desafio.pixpay.core.log;

public enum AccountLogField implements LogField {
    ACCOUNT_ID("account.id", false),
    ACCOUNT_ROLES("account.roles", false),
    ACCOUNT_IDENTIFICATION_NUMBER("account.identificationNumber", true),
    ACCOUNT_IDENTIFICATION_TYPE("account.identificationType", false),
    ACCOUNT_BALANCE("account.balance", true);

    private final String path;
    private final boolean sensitive;

    AccountLogField(String path, boolean sensitive) {
        this.path = path;
        this.sensitive = sensitive;
    }

    @Override
    public String getFieldName() {
        return path;
    }

    @Override
    public boolean isSensitive() {
        return sensitive;
    }
}