package com.desafio.pixpay.core.domain.identification;


public enum IdentificationTypeEnum {
    CADASTRO_DE_PESSOA_FISICA("CadastroDePessoaFisica"),
    CADASTRO_NACIONAL_DE_PESSOA_JURIDICA("CadastroNacionalDePessoaJuridica");

    private final String identificationType;

    IdentificationTypeEnum(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getValue() {
        return identificationType;
    }

    public static IdentificationTypeEnum fromValue(String value) {
        for (IdentificationTypeEnum identificationType : IdentificationTypeEnum.values()) {
            if (identificationType.getValue().equals(value)) {
                return identificationType;
            }
        }
        throw new IllegalArgumentException("Invalid identification type: " + value);
    }
}