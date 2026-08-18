package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class FieldTestInfiniteScrollEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static FieldTestInfiniteScrollEntity getFieldTestInfiniteScrollEntitySample1() {
        return new FieldTestInfiniteScrollEntity()
            .id(1L)
            .stringHugo("stringHugo1")
            .stringRequiredHugo("stringRequiredHugo1")
            .stringMinlengthHugo("stringMinlengthHugo1")
            .stringMaxlengthHugo("stringMaxlengthHugo1")
            .stringPatternHugo("stringPatternHugo1")
            .integerHugo(1)
            .integerRequiredHugo(1)
            .integerMinHugo(1)
            .integerMaxHugo(1)
            .longHugo(1L)
            .longRequiredHugo(1L)
            .longMinHugo(1L)
            .longMaxHugo(1L)
            .uuidHugo(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .uuidRequiredHugo(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static FieldTestInfiniteScrollEntity getFieldTestInfiniteScrollEntitySample2() {
        return new FieldTestInfiniteScrollEntity()
            .id(2L)
            .stringHugo("stringHugo2")
            .stringRequiredHugo("stringRequiredHugo2")
            .stringMinlengthHugo("stringMinlengthHugo2")
            .stringMaxlengthHugo("stringMaxlengthHugo2")
            .stringPatternHugo("stringPatternHugo2")
            .integerHugo(2)
            .integerRequiredHugo(2)
            .integerMinHugo(2)
            .integerMaxHugo(2)
            .longHugo(2L)
            .longRequiredHugo(2L)
            .longMinHugo(2L)
            .longMaxHugo(2L)
            .uuidHugo(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .uuidRequiredHugo(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static FieldTestInfiniteScrollEntity getFieldTestInfiniteScrollEntityRandomSampleGenerator() {
        return new FieldTestInfiniteScrollEntity()
            .id(longCount.incrementAndGet())
            .stringHugo(UUID.randomUUID().toString())
            .stringRequiredHugo(UUID.randomUUID().toString())
            .stringMinlengthHugo(UUID.randomUUID().toString())
            .stringMaxlengthHugo(UUID.randomUUID().toString())
            .stringPatternHugo(UUID.randomUUID().toString())
            .integerHugo(intCount.incrementAndGet())
            .integerRequiredHugo(intCount.incrementAndGet())
            .integerMinHugo(intCount.incrementAndGet())
            .integerMaxHugo(intCount.incrementAndGet())
            .longHugo(longCount.incrementAndGet())
            .longRequiredHugo(longCount.incrementAndGet())
            .longMinHugo(longCount.incrementAndGet())
            .longMaxHugo(longCount.incrementAndGet())
            .uuidHugo(UUID.randomUUID())
            .uuidRequiredHugo(UUID.randomUUID());
    }
}
