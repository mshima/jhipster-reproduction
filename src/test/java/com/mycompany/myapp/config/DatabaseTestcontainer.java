package com.mycompany.myapp.config;

import org.slf4j.LoggerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.postgresql.PostgreSQLContainer;

public interface DatabaseTestcontainer {
    @Container
    PostgreSQLContainer databaseContainer = new PostgreSQLContainer("postgres:18.6")
        .withDatabaseName("jhipster")

        .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(DatabaseTestcontainer.class)))
        .withReuse(true);

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> databaseContainer.getJdbcUrl().replace("jdbc", "r2dbc") + "");
        registry.add("spring.r2dbc.username", databaseContainer::getUsername);
        registry.add("spring.r2dbc.password", databaseContainer::getPassword);
        registry.add("spring.liquibase.url", databaseContainer::getJdbcUrl);
    }
}
