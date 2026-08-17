package tech.jhipster.sample;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import tech.jhipster.sample.config.AsyncSyncConfiguration;
import tech.jhipster.sample.config.ElasticsearchTestConfiguration;
import tech.jhipster.sample.config.ElasticsearchTestContainer;
import tech.jhipster.sample.config.EmbeddedSQL;
import tech.jhipster.sample.config.JacksonConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        SampleWebfluxH2MemApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        ElasticsearchTestConfiguration.class,
    }
)
@EmbeddedSQL
@ImportTestcontainers(ElasticsearchTestContainer.class)
public @interface IntegrationTest {
    // 5s is Spring's default https://github.com/spring-projects/spring-framework/blob/main/spring-test/src/main/java/org/springframework/test/web/reactive/server/DefaultWebTestClient.java#L106
    String DEFAULT_TIMEOUT = "PT5S";
    String DEFAULT_ENTITY_TIMEOUT = "PT5S";
}
