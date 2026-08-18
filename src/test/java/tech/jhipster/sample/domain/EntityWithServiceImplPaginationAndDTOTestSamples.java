package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class EntityWithServiceImplPaginationAndDTOTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static EntityWithServiceImplPaginationAndDTO getEntityWithServiceImplPaginationAndDTOSample1() {
        return new EntityWithServiceImplPaginationAndDTO().id(1L).theo("theo1");
    }

    public static EntityWithServiceImplPaginationAndDTO getEntityWithServiceImplPaginationAndDTOSample2() {
        return new EntityWithServiceImplPaginationAndDTO().id(2L).theo("theo2");
    }

    public static EntityWithServiceImplPaginationAndDTO getEntityWithServiceImplPaginationAndDTORandomSampleGenerator() {
        return new EntityWithServiceImplPaginationAndDTO().id(longCount.incrementAndGet()).theo(UUID.randomUUID().toString());
    }
}
