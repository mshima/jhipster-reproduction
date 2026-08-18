package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class EntityWithServiceImplAndPaginationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static EntityWithServiceImplAndPagination getEntityWithServiceImplAndPaginationSample1() {
        return new EntityWithServiceImplAndPagination().id(1L).hugo("hugo1");
    }

    public static EntityWithServiceImplAndPagination getEntityWithServiceImplAndPaginationSample2() {
        return new EntityWithServiceImplAndPagination().id(2L).hugo("hugo2");
    }

    public static EntityWithServiceImplAndPagination getEntityWithServiceImplAndPaginationRandomSampleGenerator() {
        return new EntityWithServiceImplAndPagination().id(longCount.incrementAndGet()).hugo(UUID.randomUUID().toString());
    }
}
