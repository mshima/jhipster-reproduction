package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class EntityWithServiceClassPaginationAndDTOTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static EntityWithServiceClassPaginationAndDTO getEntityWithServiceClassPaginationAndDTOSample1() {
        return new EntityWithServiceClassPaginationAndDTO().id(1L).lena("lena1");
    }

    public static EntityWithServiceClassPaginationAndDTO getEntityWithServiceClassPaginationAndDTOSample2() {
        return new EntityWithServiceClassPaginationAndDTO().id(2L).lena("lena2");
    }

    public static EntityWithServiceClassPaginationAndDTO getEntityWithServiceClassPaginationAndDTORandomSampleGenerator() {
        return new EntityWithServiceClassPaginationAndDTO().id(longCount.incrementAndGet()).lena(UUID.randomUUID().toString());
    }
}
