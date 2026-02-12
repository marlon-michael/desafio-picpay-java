package com.desafio.pixpay.core.domain.identification;

import com.desafio.pixpay.core.domain.account.AccountTypeEnum;
import com.desafio.pixpay.core.gateways.IdentificationValidatorGateway;

public interface Identification {
    public Identification builder();
    public String getIdentificationNumber();
    public IdentificationTypeEnum getIdentificationType();
    public AccountTypeEnum getIdentificationAccountType();
    public Identification fromPersistence(String identificationNumber);
    public Identification setIdentificationNumberAndValidate(String identificationNumber, IdentificationValidatorGateway identificationValidatiorGateway);
}
