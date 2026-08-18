package com.okta.developer.gateway.config;

import org.slf4j.LoggerFactory;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;

public interface DatabaseTestcontainer {
    @Container
    @ServiceConnection
    MySQLContainer<?> databaseContainer = (MySQLContainer) new MySQLContainer<>("mysql:26.7.0")
        .withDatabaseName("gateway")
        .withConfigurationOverride("conf/mysql")
        .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(DatabaseTestcontainer.class)))
        .withReuse(true);

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add(
            "spring.liquibase.url",
            () ->
                databaseContainer.getJdbcUrl() +
                "?useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&createDatabaseIfNotExist=true"
        );
        registry.add("spring.liquibase.user", databaseContainer::getUsername);
        registry.add("spring.liquibase.password", databaseContainer::getPassword);
    }
}
