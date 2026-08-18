package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class EntityWithServiceImplAndDTOTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static EntityWithServiceImplAndDTO getEntityWithServiceImplAndDTOSample1() {
        return new EntityWithServiceImplAndDTO().id(1L).louis("louis1");
    }

    public static EntityWithServiceImplAndDTO getEntityWithServiceImplAndDTOSample2() {
        return new EntityWithServiceImplAndDTO().id(2L).louis("louis2");
    }

    public static EntityWithServiceImplAndDTO getEntityWithServiceImplAndDTORandomSampleGenerator() {
        return new EntityWithServiceImplAndDTO().id(longCount.incrementAndGet()).louis(UUID.randomUUID().toString());
    }
}
