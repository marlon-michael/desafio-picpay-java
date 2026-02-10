package com.desafio.pixpay.core.domain.identification;

import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.gateways.IdentificationValidatorGateway;

public class CadastroNacionalDePessoaJuridica implements Identification {
    private String identificationNumber;

	@Override
	public IdentificationTypeEnum getIdentificationType() {
		return IdentificationTypeEnum.CADASTRO_NACIONAL_DE_PESSOA_JURIDICA;
	}

	@Override
    public Identification setIdentificationNumberAndValidate(String identificationNumber, IdentificationValidatorGateway identificationValidatorGateway) {
        if (identificationValidatorGateway == null) throw new RuntimeException("IdentificationValidatorGateway should not be null.");
		if (!identificationValidatorGateway.isCNPJValid(identificationNumber)) throw new IllegalArgumentException("Invalid Identification number.");
		if (identificationNumber.length() != 14) throw new IllegalArgumentException("The identification number for Cadastro Nacional De Pessoa Juridica must be 14 characters long. Ex: 00.000.000/0000-00");
        this.identificationNumber = identificationNumber;
        
        return this;
    }

	@Override
	public String getIdentificationNumber() {
		return this.identificationNumber;
	}

	@Override
	public Identification fromPersistence(String identificationNumber) {
		this.identificationNumber = identificationNumber;
		return this;
	}

	@Override
	public AccountTypeEnum getIdentificationAccountType() {
		return AccountTypeEnum.BUSINESS;
	}
    
}
