package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class FieldTestEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static FieldTestEntity getFieldTestEntitySample1() {
        return new FieldTestEntity()
            .id(1L)
            .stringTom("stringTom1")
            .stringRequiredTom("stringRequiredTom1")
            .stringMinlengthTom("stringMinlengthTom1")
            .stringMaxlengthTom("stringMaxlengthTom1")
            .stringPatternTom("stringPatternTom1")
            .numberPatternTom("numberPatternTom1")
            .numberPatternRequiredTom("numberPatternRequiredTom1")
            .integerTom(1)
            .integerRequiredTom(1)
            .integerMinTom(1)
            .integerMaxTom(1)
            .longTom(1L)
            .longRequiredTom(1L)
            .longMinTom(1L)
            .longMaxTom(1L)
            .uuidTom(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .uuidRequiredTom(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static FieldTestEntity getFieldTestEntitySample2() {
        return new FieldTestEntity()
            .id(2L)
            .stringTom("stringTom2")
            .stringRequiredTom("stringRequiredTom2")
            .stringMinlengthTom("stringMinlengthTom2")
            .stringMaxlengthTom("stringMaxlengthTom2")
            .stringPatternTom("stringPatternTom2")
            .numberPatternTom("numberPatternTom2")
            .numberPatternRequiredTom("numberPatternRequiredTom2")
            .integerTom(2)
            .integerRequiredTom(2)
            .integerMinTom(2)
            .integerMaxTom(2)
            .longTom(2L)
            .longRequiredTom(2L)
            .longMinTom(2L)
            .longMaxTom(2L)
            .uuidTom(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .uuidRequiredTom(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static FieldTestEntity getFieldTestEntityRandomSampleGenerator() {
        return new FieldTestEntity()
            .id(longCount.incrementAndGet())
            .stringTom(UUID.randomUUID().toString())
            .stringRequiredTom(UUID.randomUUID().toString())
            .stringMinlengthTom(UUID.randomUUID().toString())
            .stringMaxlengthTom(UUID.randomUUID().toString())
            .stringPatternTom(UUID.randomUUID().toString())
            .numberPatternTom(UUID.randomUUID().toString())
            .numberPatternRequiredTom(UUID.randomUUID().toString())
            .integerTom(intCount.incrementAndGet())
            .integerRequiredTom(intCount.incrementAndGet())
            .integerMinTom(intCount.incrementAndGet())
            .integerMaxTom(intCount.incrementAndGet())
            .longTom(longCount.incrementAndGet())
            .longRequiredTom(longCount.incrementAndGet())
            .longMinTom(longCount.incrementAndGet())
            .longMaxTom(longCount.incrementAndGet())
            .uuidTom(UUID.randomUUID())
            .uuidRequiredTom(UUID.randomUUID());
    }
}
