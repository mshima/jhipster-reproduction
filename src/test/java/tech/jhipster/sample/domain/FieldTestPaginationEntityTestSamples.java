package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

public class FieldTestPaginationEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static FieldTestPaginationEntity getFieldTestPaginationEntitySample1() {
        return new FieldTestPaginationEntity()
            .id(1L)
            .stringAlice("stringAlice1")
            .stringRequiredAlice("stringRequiredAlice1")
            .stringMinlengthAlice("stringMinlengthAlice1")
            .stringMaxlengthAlice("stringMaxlengthAlice1")
            .stringPatternAlice("stringPatternAlice1")
            .integerAlice(1)
            .integerRequiredAlice(1)
            .integerMinAlice(1)
            .integerMaxAlice(1)
            .longAlice(1L)
            .longRequiredAlice(1L)
            .longMinAlice(1L)
            .longMaxAlice(1L)
            .uuidAlice(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .uuidRequiredAlice(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static FieldTestPaginationEntity getFieldTestPaginationEntitySample2() {
        return new FieldTestPaginationEntity()
            .id(2L)
            .stringAlice("stringAlice2")
            .stringRequiredAlice("stringRequiredAlice2")
            .stringMinlengthAlice("stringMinlengthAlice2")
            .stringMaxlengthAlice("stringMaxlengthAlice2")
            .stringPatternAlice("stringPatternAlice2")
            .integerAlice(2)
            .integerRequiredAlice(2)
            .integerMinAlice(2)
            .integerMaxAlice(2)
            .longAlice(2L)
            .longRequiredAlice(2L)
            .longMinAlice(2L)
            .longMaxAlice(2L)
            .uuidAlice(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .uuidRequiredAlice(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static FieldTestPaginationEntity getFieldTestPaginationEntityRandomSampleGenerator() {
        return new FieldTestPaginationEntity()
            .id(longCount.incrementAndGet())
            .stringAlice(UUID.randomUUID().toString())
            .stringRequiredAlice(UUID.randomUUID().toString())
            .stringMinlengthAlice(UUID.randomUUID().toString())
            .stringMaxlengthAlice(UUID.randomUUID().toString())
            .stringPatternAlice(UUID.randomUUID().toString())
            .integerAlice(intCount.incrementAndGet())
            .integerRequiredAlice(intCount.incrementAndGet())
            .integerMinAlice(intCount.incrementAndGet())
            .integerMaxAlice(intCount.incrementAndGet())
            .longAlice(longCount.incrementAndGet())
            .longRequiredAlice(longCount.incrementAndGet())
            .longMinAlice(longCount.incrementAndGet())
            .longMaxAlice(longCount.incrementAndGet())
            .uuidAlice(UUID.randomUUID())
            .uuidRequiredAlice(UUID.randomUUID());
    }
}
