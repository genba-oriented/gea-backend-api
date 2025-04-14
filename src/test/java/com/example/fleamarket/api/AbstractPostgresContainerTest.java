package com.example.fleamarket.api;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractPostgresContainerTest {
    @ServiceConnection
    static final PostgreSQLContainer<?> postgres;
    static {
        postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
        postgres.start();
    }
}
