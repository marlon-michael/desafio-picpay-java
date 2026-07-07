package com.desafio.pixpay.infra;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.wait.strategy.Wait;


public class TestContainers implements BeforeAllCallback  {

    private static final Network redisNetwork = Network.newNetwork();

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    private static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.7.0"));

    private static final GenericContainer<?> redisMaster = new GenericContainer<>(DockerImageName.parse("bitnami/redis:latest"));

    private static final GenericContainer<?> redisSlave = new GenericContainer<>(DockerImageName.parse("bitnami/redis:latest"));

    private static final GenericContainer<?> sentinel1 = new GenericContainer<>(DockerImageName.parse("bitnami/redis-sentinel:latest"));

    private static final GenericContainer<?> sentinel2 = new GenericContainer<>(DockerImageName.parse("bitnami/redis-sentinel:latest"));

    private static final GenericContainer<?> sentinel3 = new GenericContainer<>(DockerImageName.parse("bitnami/redis-sentinel:latest"));

    static {
        postgres.withDatabaseName("pixpay_test")
            .withUsername("test")
            .withPassword("test")
            .waitingFor(Wait.forListeningPort());

        redisMaster.withNetwork(redisNetwork)
            .withNetworkAliases("redis-master")
            .withEnv("REDIS_REPLICATION_MODE", "master")
            .withEnv("ALLOW_EMPTY_PASSWORD", "yes")
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());

        redisSlave.withNetwork(redisNetwork)
            .withEnv("REDIS_REPLICATION_MODE", "slave")
            .withEnv("REDIS_MASTER_HOST", "redis-master")
            .withEnv("REDIS_MASTER_PORT_NUMBER", "6379")
            .withEnv("ALLOW_EMPTY_PASSWORD", "yes")
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());

        sentinel1.withNetwork(redisNetwork)
            .withEnv("REDIS_MASTER_HOST", "redis-master")
            .withEnv("REDIS_MASTER_SET", "mymaster")
            .withEnv("REDIS_SENTINEL_QUORUM", "2")
            .withEnv("REDIS_SENTINEL_RESOLVE_HOSTNAMES", "yes")
            .withEnv("ALLOW_EMPTY_PASSWORD", "yes")
            .withExposedPorts(26379)
            .waitingFor(Wait.forListeningPort());

        sentinel2.withNetwork(redisNetwork)
            .withEnv("REDIS_MASTER_HOST", "redis-master")
            .withEnv("REDIS_MASTER_SET", "mymaster")
            .withEnv("REDIS_SENTINEL_QUORUM", "2")
            .withEnv("REDIS_SENTINEL_RESOLVE_HOSTNAMES", "yes")
            .withEnv("ALLOW_EMPTY_PASSWORD", "yes")
            .withExposedPorts(26379)
            .waitingFor(Wait.forListeningPort());

        sentinel3.withNetwork(redisNetwork)
            .withEnv("REDIS_MASTER_HOST", "redis-master")
            .withEnv("REDIS_MASTER_SET", "mymaster")
            .withEnv("REDIS_SENTINEL_QUORUM", "2")
            .withEnv("REDIS_SENTINEL_RESOLVE_HOSTNAMES", "yes")
            .withEnv("ALLOW_EMPTY_PASSWORD", "yes")
            .withExposedPorts(26379)
            .waitingFor(Wait.forListeningPort());
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!postgres.isRunning()) postgres.start();
        if (!kafka.isRunning()) kafka.start();
        if (!redisMaster.isRunning()) redisMaster.start();
        if (!redisSlave.isRunning()) redisSlave.start();
        if (!sentinel1.isRunning()) sentinel1.start();
        if (!sentinel2.isRunning()) sentinel2.start();
        if (!sentinel3.isRunning()) sentinel3.start();

        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());

        String sentinelNodes = String.format("%s:%d,%s:%d,%s:%d",
            sentinel1.getHost(), sentinel1.getMappedPort(26379),
            sentinel2.getHost(), sentinel2.getMappedPort(26379),
            sentinel3.getHost(), sentinel3.getMappedPort(26379)
        );

        System.setProperty("app.redis.sentinel.master", "mymaster");
        System.setProperty("app.redis.sentinel.nodes", sentinelNodes);
        
        System.setProperty("REDIS_PASSWORD", ""); 
        System.setProperty("app.redis.sentinel.password", "");
        System.setProperty("DB_HOST", "localhost");
    }

}
