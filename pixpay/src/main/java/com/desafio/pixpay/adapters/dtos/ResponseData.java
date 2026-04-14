package com.desafio.pixpay.adapters.dtos;

import java.time.LocalDateTime;

public record ResponseData<T>(T content, String message, LocalDateTime timestamp) {
}
