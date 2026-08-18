package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class EntityWithServiceClassAndPaginationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static EntityWithServiceClassAndPagination getEntityWithServiceClassAndPaginationSample1() {
        return new EntityWithServiceClassAndPagination().id(1L).enzo("enzo1");
    }

    public static EntityWithServiceClassAndPagination getEntityWithServiceClassAndPaginationSample2() {
        return new EntityWithServiceClassAndPagination().id(2L).enzo("enzo2");
    }

    public static EntityWithServiceClassAndPagination getEntityWithServiceClassAndPaginationRandomSampleGenerator() {
        return new EntityWithServiceClassAndPagination().id(longCount.incrementAndGet()).enzo(UUID.randomUUID().toString());
    }
}
