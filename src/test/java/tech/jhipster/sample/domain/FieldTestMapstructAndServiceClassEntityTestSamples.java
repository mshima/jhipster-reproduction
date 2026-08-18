package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class FieldTestMapstructAndServiceClassEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static FieldTestMapstructAndServiceClassEntity getFieldTestMapstructAndServiceClassEntitySample1() {
        return new FieldTestMapstructAndServiceClassEntity()
            .id(1L)
            .stringEva("stringEva1")
            .stringRequiredEva("stringRequiredEva1")
            .stringMinlengthEva("stringMinlengthEva1")
            .stringMaxlengthEva("stringMaxlengthEva1")
            .stringPatternEva("stringPatternEva1")
            .integerEva(1)
            .integerRequiredEva(1)
            .integerMinEva(1)
            .integerMaxEva(1)
            .longEva(1L)
            .longRequiredEva(1L)
            .longMinEva(1L)
            .longMaxEva(1L)
            .uuidEva(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .uuidRequiredEva(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static FieldTestMapstructAndServiceClassEntity getFieldTestMapstructAndServiceClassEntitySample2() {
        return new FieldTestMapstructAndServiceClassEntity()
            .id(2L)
            .stringEva("stringEva2")
            .stringRequiredEva("stringRequiredEva2")
            .stringMinlengthEva("stringMinlengthEva2")
            .stringMaxlengthEva("stringMaxlengthEva2")
            .stringPatternEva("stringPatternEva2")
            .integerEva(2)
            .integerRequiredEva(2)
            .integerMinEva(2)
            .integerMaxEva(2)
            .longEva(2L)
            .longRequiredEva(2L)
            .longMinEva(2L)
            .longMaxEva(2L)
            .uuidEva(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .uuidRequiredEva(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static FieldTestMapstructAndServiceClassEntity getFieldTestMapstructAndServiceClassEntityRandomSampleGenerator() {
        return new FieldTestMapstructAndServiceClassEntity()
            .id(longCount.incrementAndGet())
            .stringEva(UUID.randomUUID().toString())
            .stringRequiredEva(UUID.randomUUID().toString())
            .stringMinlengthEva(UUID.randomUUID().toString())
            .stringMaxlengthEva(UUID.randomUUID().toString())
            .stringPatternEva(UUID.randomUUID().toString())
            .integerEva(intCount.incrementAndGet())
            .integerRequiredEva(intCount.incrementAndGet())
            .integerMinEva(intCount.incrementAndGet())
            .integerMaxEva(intCount.incrementAndGet())
            .longEva(longCount.incrementAndGet())
            .longRequiredEva(longCount.incrementAndGet())
            .longMinEva(longCount.incrementAndGet())
            .longMaxEva(longCount.incrementAndGet())
            .uuidEva(UUID.randomUUID())
            .uuidRequiredEva(UUID.randomUUID());
    }
}
