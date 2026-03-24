package com.desafio.pixpay.infra.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;


@Configuration
public class RedisConfig {

    @Value("${app.redis.sentinel.master}")
    private String MASTER;

    @Value("${app.redis.sentinel.nodes}")
    private String NODES;

    @Value("${app.redis.sentinel.timeout-seconds}")
    private String TIMEOUT_SECONDS;

    @Value("${app.redis.sentinel.ttl-minutes}")
    private String TTL_MINUTES;

    @Bean
    @Primary
    RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
    
    @Bean
    RedisConnectionFactory redisConnectionFactory(){
        RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();

        sentinelConfiguration.master(MASTER);

        for (String nodeString : NODES.split(",")) {
            String[] node = nodeString.split(":");
            String address = node[0];
            int port = Integer.parseInt(node[1]);

            sentinelConfiguration.sentinel(address, port);
        }

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
            .clientOptions(
                ClientOptions.builder()
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(true)
                .build())
            .readFrom(ReadFrom.REPLICA_PREFERRED) 
            .commandTimeout(Duration.ofSeconds(Long.parseLong(TIMEOUT_SECONDS)))
            .build();

        return new LettuceConnectionFactory(sentinelConfiguration, clientConfig);
    }

    @Bean
    RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(Long.parseLong(TTL_MINUTES)))
            .disableCachingNullValues()
            .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(SerializationPair.fromSerializer(new StringRedisSerializer()));
    }
    
    @Bean
    RedisCacheManager cacheManager(){
        return RedisCacheManager
        .builder(redisConnectionFactory())
        .cacheDefaults(cacheConfiguration())
        .build();
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
