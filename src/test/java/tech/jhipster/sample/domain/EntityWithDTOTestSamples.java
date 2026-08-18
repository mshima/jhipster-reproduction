package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class EntityWithDTOTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static EntityWithDTO getEntityWithDTOSample1() {
        return new EntityWithDTO().id(1L).emma("emma1");
    }

    public static EntityWithDTO getEntityWithDTOSample2() {
        return new EntityWithDTO().id(2L).emma("emma2");
    }

    public static EntityWithDTO getEntityWithDTORandomSampleGenerator() {
        return new EntityWithDTO().id(longCount.incrementAndGet()).emma(UUID.randomUUID().toString());
    }
}
