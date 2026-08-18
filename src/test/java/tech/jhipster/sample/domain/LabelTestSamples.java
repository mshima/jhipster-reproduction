package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class LabelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Label getLabelSample1() {
        return new Label().id(1L).labelName("labelName1");
    }

    public static Label getLabelSample2() {
        return new Label().id(2L).labelName("labelName2");
    }

    public static Label getLabelRandomSampleGenerator() {
        return new Label().id(longCount.incrementAndGet()).labelName(UUID.randomUUID().toString());
    }
}
