package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class FieldTestServiceImplEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static FieldTestServiceImplEntity getFieldTestServiceImplEntitySample1() {
        return new FieldTestServiceImplEntity()
            .id(1L)
            .stringMika("stringMika1")
            .stringRequiredMika("stringRequiredMika1")
            .stringMinlengthMika("stringMinlengthMika1")
            .stringMaxlengthMika("stringMaxlengthMika1")
            .stringPatternMika("stringPatternMika1")
            .integerMika(1)
            .integerRequiredMika(1)
            .integerMinMika(1)
            .integerMaxMika(1)
            .longMika(1L)
            .longRequiredMika(1L)
            .longMinMika(1L)
            .longMaxMika(1L)
            .uuidMika(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .uuidRequiredMika(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static FieldTestServiceImplEntity getFieldTestServiceImplEntitySample2() {
        return new FieldTestServiceImplEntity()
            .id(2L)
            .stringMika("stringMika2")
            .stringRequiredMika("stringRequiredMika2")
            .stringMinlengthMika("stringMinlengthMika2")
            .stringMaxlengthMika("stringMaxlengthMika2")
            .stringPatternMika("stringPatternMika2")
            .integerMika(2)
            .integerRequiredMika(2)
            .integerMinMika(2)
            .integerMaxMika(2)
            .longMika(2L)
            .longRequiredMika(2L)
            .longMinMika(2L)
            .longMaxMika(2L)
            .uuidMika(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .uuidRequiredMika(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static FieldTestServiceImplEntity getFieldTestServiceImplEntityRandomSampleGenerator() {
        return new FieldTestServiceImplEntity()
            .id(longCount.incrementAndGet())
            .stringMika(UUID.randomUUID().toString())
            .stringRequiredMika(UUID.randomUUID().toString())
            .stringMinlengthMika(UUID.randomUUID().toString())
            .stringMaxlengthMika(UUID.randomUUID().toString())
            .stringPatternMika(UUID.randomUUID().toString())
            .integerMika(intCount.incrementAndGet())
            .integerRequiredMika(intCount.incrementAndGet())
            .integerMinMika(intCount.incrementAndGet())
            .integerMaxMika(intCount.incrementAndGet())
            .longMika(longCount.incrementAndGet())
            .longRequiredMika(longCount.incrementAndGet())
            .longMinMika(longCount.incrementAndGet())
            .longMaxMika(longCount.incrementAndGet())
            .uuidMika(UUID.randomUUID())
            .uuidRequiredMika(UUID.randomUUID());
    }
}
