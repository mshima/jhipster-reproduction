package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class FieldTestEnumWithValueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static FieldTestEnumWithValue getFieldTestEnumWithValueSample1() {
        return new FieldTestEnumWithValue().id(1L);
    }

    public static FieldTestEnumWithValue getFieldTestEnumWithValueSample2() {
        return new FieldTestEnumWithValue().id(2L);
    }

    public static FieldTestEnumWithValue getFieldTestEnumWithValueRandomSampleGenerator() {
        return new FieldTestEnumWithValue().id(longCount.incrementAndGet());
    }
}
