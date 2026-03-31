package com.desafio.pixpay.core.domain.identification;

import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.exceptions.InvalidDataException;
import com.desafio.pixpay.core.gateways.IdentificationValidatorGateway;

public class CadastroNacionalDePessoaJuridica implements Identification {
    private String identificationNumber;

	@Override
	public Identification builder(){
		return new CadastroNacionalDePessoaJuridica();
	}

	@Override
	public IdentificationTypeEnum getIdentificationType() {
		return IdentificationTypeEnum.CADASTRO_NACIONAL_DE_PESSOA_JURIDICA;
	}

	@Override
    public Identification setIdentificationNumberAndValidate(String identificationNumber, IdentificationValidatorGateway identificationValidatorGateway) {
        if (identificationValidatorGateway == null) throw new InvalidDataException("IdentificationValidatorGateway should not be null.");
		if (identificationNumber == null) throw new InvalidDataException("Empty Identification number.");
		if (identificationNumber.length() != 14) throw new InvalidDataException("The identification number for Cadastro Nacional De Pessoa Juridica must be 14 characters long. Ex: 00.000.000/0000-00");
		if (!identificationValidatorGateway.isCnpjValid(identificationNumber)) throw new InvalidDataException("Invalid Identification number.");
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
