package com.desafio.pixpay.core.domain.identification;

import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.gateways.IdentificationValidatorGateway;

public class CadastroDePessoaFisica implements Identification {
    private String identificationNumber;

    @Override
	public Identification builder() {
		return new CadastroDePessoaFisica();
	}

    @Override
    public IdentificationTypeEnum getIdentificationType() {
        return IdentificationTypeEnum.CADASTRO_DE_PESSOA_FISICA;
    }

    @Override
    public Identification setIdentificationNumberAndValidate(String identificationNumber, IdentificationValidatorGateway identificationValidatorGateway){
        if(identificationValidatorGateway == null) throw new RuntimeException("IdentificationValidatorGateway should not be null.");
        if (!identificationValidatorGateway.isCPFValid(identificationNumber)) throw new IllegalArgumentException("Invalid Identification number.");
        if (identificationNumber.length() != 11) throw new IllegalArgumentException("The identification number for Cadastro De Pessoa Fisica must be 11 characters long. Ex: 000.000.000-00.");
        if (!identificationNumber.matches("^[0-9]{11}$")) throw new IllegalArgumentException("The identification number for Cadastro De Pessoa Fisica must contain only numbers.");
        this.identificationNumber = identificationNumber;
        return this;
    }

    @Override
    public String getIdentificationNumber() {
        return this.identificationNumber;
    }

    @Override
    public Identification fromPersistence(String identificationNumber){
        this.identificationNumber = identificationNumber;
        return this;
    }

	@Override
	public AccountTypeEnum getIdentificationAccountType() {
        return AccountTypeEnum.PERSONAL;

	}
}
