package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class MapsIdUserProfileWithDTOTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static MapsIdUserProfileWithDTO getMapsIdUserProfileWithDTOSample1() {
        return new MapsIdUserProfileWithDTO().id(1L);
    }

    public static MapsIdUserProfileWithDTO getMapsIdUserProfileWithDTOSample2() {
        return new MapsIdUserProfileWithDTO().id(2L);
    }

    public static MapsIdUserProfileWithDTO getMapsIdUserProfileWithDTORandomSampleGenerator() {
        return new MapsIdUserProfileWithDTO().id(longCount.incrementAndGet());
    }
}
