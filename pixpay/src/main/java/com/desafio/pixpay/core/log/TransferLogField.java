package com.desafio.pixpay.core.log;

public enum TransferLogField implements LogField {
    TRANSFER_ID("transfer.id", false),
    TRANSFER_VALUE("transfer.value", false),
    TRANSFER_PAYER("transfer.payer", false),
    TRANSFER_PAYEE("transfer.payee", false);

    private final String path;
    private final boolean sensitive;

    TransferLogField(String path, boolean sensitive) {
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