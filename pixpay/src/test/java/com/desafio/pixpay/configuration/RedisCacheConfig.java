package com.desafio.pixpay.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Profile("test")
public class RedisCacheConfig {

    @Bean
    @Primary
    RedisConnectionFactory redisConnectionFactory() {
        RedisConnectionFactory factory = Mockito.mock(RedisConnectionFactory.class);
        RedisConnection connection = Mockito.mock(RedisConnection.class);
        Mockito.when(connection.keyCommands()).thenReturn(Mockito.mock(RedisKeyCommands.class));
        Mockito.when(connection.stringCommands()).thenReturn(Mockito.mock(RedisStringCommands.class));
        Mockito.when(connection.isPipelined()).thenReturn(false);
        Mockito.when(connection.isQueueing()).thenReturn(false);
        Mockito.when(factory.getConnection()).thenReturn(connection);
        return factory;
    }

    @Bean
    @Primary
    RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    @Primary
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
