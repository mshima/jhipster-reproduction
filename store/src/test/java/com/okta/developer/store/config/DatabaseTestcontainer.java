package com.okta.developer.store.config;

import org.slf4j.LoggerFactory;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;

public interface DatabaseTestcontainer {
    @Container
    @ServiceConnection
    MySQLContainer<?> databaseContainer = (MySQLContainer) new MySQLContainer<>("mysql:26.7.0")
        .withDatabaseName("store")
        .withConfigurationOverride("conf/mysql")
        .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(DatabaseTestcontainer.class)))
        .withReuse(true);
}
