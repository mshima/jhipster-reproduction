package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class EntityWithPaginationAndDTOTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static EntityWithPaginationAndDTO getEntityWithPaginationAndDTOSample1() {
        return new EntityWithPaginationAndDTO().id(1L).lea("lea1");
    }

    public static EntityWithPaginationAndDTO getEntityWithPaginationAndDTOSample2() {
        return new EntityWithPaginationAndDTO().id(2L).lea("lea2");
    }

    public static EntityWithPaginationAndDTO getEntityWithPaginationAndDTORandomSampleGenerator() {
        return new EntityWithPaginationAndDTO().id(longCount.incrementAndGet()).lea(UUID.randomUUID().toString());
    }
}
