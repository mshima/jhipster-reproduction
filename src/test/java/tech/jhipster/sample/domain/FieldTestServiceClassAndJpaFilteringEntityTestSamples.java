package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class FieldTestServiceClassAndJpaFilteringEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static FieldTestServiceClassAndJpaFilteringEntity getFieldTestServiceClassAndJpaFilteringEntitySample1() {
        return new FieldTestServiceClassAndJpaFilteringEntity()
            .id(1L)
            .stringBob("stringBob1")
            .stringRequiredBob("stringRequiredBob1")
            .stringMinlengthBob("stringMinlengthBob1")
            .stringMaxlengthBob("stringMaxlengthBob1")
            .stringPatternBob("stringPatternBob1")
            .integerBob(1)
            .integerRequiredBob(1)
            .integerMinBob(1)
            .integerMaxBob(1)
            .longBob(1L)
            .longRequiredBob(1L)
            .longMinBob(1L)
            .longMaxBob(1L)
            .uuidBob(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .uuidRequiredBob(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static FieldTestServiceClassAndJpaFilteringEntity getFieldTestServiceClassAndJpaFilteringEntitySample2() {
        return new FieldTestServiceClassAndJpaFilteringEntity()
            .id(2L)
            .stringBob("stringBob2")
            .stringRequiredBob("stringRequiredBob2")
            .stringMinlengthBob("stringMinlengthBob2")
            .stringMaxlengthBob("stringMaxlengthBob2")
            .stringPatternBob("stringPatternBob2")
            .integerBob(2)
            .integerRequiredBob(2)
            .integerMinBob(2)
            .integerMaxBob(2)
            .longBob(2L)
            .longRequiredBob(2L)
            .longMinBob(2L)
            .longMaxBob(2L)
            .uuidBob(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .uuidRequiredBob(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static FieldTestServiceClassAndJpaFilteringEntity getFieldTestServiceClassAndJpaFilteringEntityRandomSampleGenerator() {
        return new FieldTestServiceClassAndJpaFilteringEntity()
            .id(longCount.incrementAndGet())
            .stringBob(UUID.randomUUID().toString())
            .stringRequiredBob(UUID.randomUUID().toString())
            .stringMinlengthBob(UUID.randomUUID().toString())
            .stringMaxlengthBob(UUID.randomUUID().toString())
            .stringPatternBob(UUID.randomUUID().toString())
            .integerBob(intCount.incrementAndGet())
            .integerRequiredBob(intCount.incrementAndGet())
            .integerMinBob(intCount.incrementAndGet())
            .integerMaxBob(intCount.incrementAndGet())
            .longBob(longCount.incrementAndGet())
            .longRequiredBob(longCount.incrementAndGet())
            .longMinBob(longCount.incrementAndGet())
            .longMaxBob(longCount.incrementAndGet())
            .uuidBob(UUID.randomUUID())
            .uuidRequiredBob(UUID.randomUUID());
    }
}
