package com.desafio.pixpay.core.domain;

import java.util.UUID;
import lombok.Data;

@Data
public class User {
    UUID id;
    String identificacao;
    String nomeCompleto;
    String email;
    String password;
    Long saldo;
}
