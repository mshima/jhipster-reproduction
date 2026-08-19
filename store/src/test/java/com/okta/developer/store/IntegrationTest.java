package com.okta.developer.store;

import com.okta.developer.store.StoreApp;
import com.okta.developer.store.config.AsyncSyncConfiguration;
import com.okta.developer.store.config.DatabaseTestcontainer;
import com.okta.developer.store.config.JacksonConfiguration;
import com.okta.developer.store.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.cache.test.autoconfigure.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        StoreApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        TestSecurityConfiguration.class,
        com.okta.developer.store.config.JacksonHibernateConfiguration.class,
    }
)
@ImportTestcontainers(DatabaseTestcontainer.class)
@AutoConfigureCache(cacheProvider = CacheType.INFINISPAN)
public @interface IntegrationTest {}
