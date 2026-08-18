package com.okta.developer.gateway.config;

import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

@TestConfiguration(proxyBeanMethods = false)
public class DatabaseTestcontainer {

    private static final MySQLContainer<?> DATABASE_CONTAINER = (MySQLContainer) new MySQLContainer<>("mysql:26.7.0")
        .withDatabaseName("gateway")
        .withConfigurationOverride("conf/mysql")
        .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(DatabaseTestcontainer.class)))
        .withReuse(true);

    @Bean
    @ServiceConnection
    MySQLContainer<?> databaseContainer() {
        return DATABASE_CONTAINER;
    }

    @Bean
    DynamicPropertyRegistrar databaseProperties(MySQLContainer<?> databaseContainer) {
        return registry -> {
            registry.add(
                "spring.liquibase.url",
                () ->
                    databaseContainer.getJdbcUrl() +
                    "?useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&createDatabaseIfNotExist=true"
            );
            registry.add("spring.liquibase.user", databaseContainer::getUsername);
            registry.add("spring.liquibase.password", databaseContainer::getPassword);
        };
    }
}
