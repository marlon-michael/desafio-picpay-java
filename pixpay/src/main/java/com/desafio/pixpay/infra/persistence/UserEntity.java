package com.desafio.pixpay.infra.persistence;

import java.util.UUID;

import com.desafio.pixpay.core.domain.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "users")
@Entity
public class UserEntity {
    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String identificacao;
    String nomeCompleto;
    String email;
    String password;
    Long saldo;

    public static UserEntity fromDomainToEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setIdentificacao(user.getIdentificacao());
        userEntity.setNomeCompleto(user.getNomeCompleto());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setSaldo(user.getSaldo());
        return userEntity;
    }

    public static User fromEntityToDomain(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setIdentificacao(userEntity.getIdentificacao());
        user.setNomeCompleto(userEntity.getNomeCompleto());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        user.setSaldo(userEntity.getSaldo());
        return user;
    }
}
