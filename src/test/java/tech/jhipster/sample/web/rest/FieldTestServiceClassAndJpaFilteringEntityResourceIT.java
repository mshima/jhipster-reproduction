package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static tech.jhipster.sample.domain.FieldTestServiceClassAndJpaFilteringEntityAsserts.*;
import static tech.jhipster.sample.web.rest.TestUtil.createUpdateProxyForBean;
import static tech.jhipster.sample.web.rest.TestUtil.sameInstant;
import static tech.jhipster.sample.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import tech.jhipster.sample.IntegrationTest;
import tech.jhipster.sample.domain.FieldTestServiceClassAndJpaFilteringEntity;
import tech.jhipster.sample.domain.enumeration.EnumFieldClass;
import tech.jhipster.sample.domain.enumeration.EnumRequiredFieldClass;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.FieldTestServiceClassAndJpaFilteringEntityRepository;

/**
 * Integration tests for the {@link FieldTestServiceClassAndJpaFilteringEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FieldTestServiceClassAndJpaFilteringEntityResourceIT {

    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final String DEFAULT_STRING_BOB = "AAAAAAAAAA";
    private static final String UPDATED_STRING_BOB = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_REQUIRED_BOB = "AAAAAAAAAA";
    private static final String UPDATED_STRING_REQUIRED_BOB = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_MINLENGTH_BOB = "AAAAAAAAAA";
    private static final String UPDATED_STRING_MINLENGTH_BOB = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_MAXLENGTH_BOB = "AAAAAAAAAA";
    private static final String UPDATED_STRING_MAXLENGTH_BOB = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_PATTERN_BOB = "AAAAAAAAAA";
    private static final String UPDATED_STRING_PATTERN_BOB = "BBBBBBBBBB";

    private static final Integer DEFAULT_INTEGER_BOB = 1;
    private static final Integer UPDATED_INTEGER_BOB = 2;
    private static final Integer SMALLER_INTEGER_BOB = 1 - 1;

    private static final Integer DEFAULT_INTEGER_REQUIRED_BOB = 1;
    private static final Integer UPDATED_INTEGER_REQUIRED_BOB = 2;
    private static final Integer SMALLER_INTEGER_REQUIRED_BOB = 1 - 1;

    private static final Integer DEFAULT_INTEGER_MIN_BOB = 0;
    private static final Integer UPDATED_INTEGER_MIN_BOB = 1;
    private static final Integer SMALLER_INTEGER_MIN_BOB = 0 - 1;

    private static final Integer DEFAULT_INTEGER_MAX_BOB = 100;
    private static final Integer UPDATED_INTEGER_MAX_BOB = 99;
    private static final Integer SMALLER_INTEGER_MAX_BOB = 100 - 1;

    private static final Long DEFAULT_LONG_BOB = 1L;
    private static final Long UPDATED_LONG_BOB = 2L;
    private static final Long SMALLER_LONG_BOB = 1L - 1L;

    private static final Long DEFAULT_LONG_REQUIRED_BOB = 1L;
    private static final Long UPDATED_LONG_REQUIRED_BOB = 2L;
    private static final Long SMALLER_LONG_REQUIRED_BOB = 1L - 1L;

    private static final Long DEFAULT_LONG_MIN_BOB = 0L;
    private static final Long UPDATED_LONG_MIN_BOB = 1L;
    private static final Long SMALLER_LONG_MIN_BOB = 0L - 1L;

    private static final Long DEFAULT_LONG_MAX_BOB = 100L;
    private static final Long UPDATED_LONG_MAX_BOB = 99L;
    private static final Long SMALLER_LONG_MAX_BOB = 100L - 1L;

    private static final Float DEFAULT_FLOAT_BOB = 1F;
    private static final Float UPDATED_FLOAT_BOB = 2F;
    private static final Float SMALLER_FLOAT_BOB = 1F - 1F;

    private static final Float DEFAULT_FLOAT_REQUIRED_BOB = 1F;
    private static final Float UPDATED_FLOAT_REQUIRED_BOB = 2F;
    private static final Float SMALLER_FLOAT_REQUIRED_BOB = 1F - 1F;

    private static final Float DEFAULT_FLOAT_MIN_BOB = 0F;
    private static final Float UPDATED_FLOAT_MIN_BOB = 1F;
    private static final Float SMALLER_FLOAT_MIN_BOB = 0F - 1F;

    private static final Float DEFAULT_FLOAT_MAX_BOB = 100F;
    private static final Float UPDATED_FLOAT_MAX_BOB = 99F;
    private static final Float SMALLER_FLOAT_MAX_BOB = 100F - 1F;

    private static final Double DEFAULT_DOUBLE_REQUIRED_BOB = 1D;
    private static final Double UPDATED_DOUBLE_REQUIRED_BOB = 2D;
    private static final Double SMALLER_DOUBLE_REQUIRED_BOB = 1D - 1D;

    private static final Double DEFAULT_DOUBLE_MIN_BOB = 0D;
    private static final Double UPDATED_DOUBLE_MIN_BOB = 1D;
    private static final Double SMALLER_DOUBLE_MIN_BOB = 0D - 1D;

    private static final Double DEFAULT_DOUBLE_MAX_BOB = 100D;
    private static final Double UPDATED_DOUBLE_MAX_BOB = 99D;
    private static final Double SMALLER_DOUBLE_MAX_BOB = 100D - 1D;

    private static final BigDecimal DEFAULT_BIG_DECIMAL_REQUIRED_BOB = new BigDecimal(1);
    private static final BigDecimal UPDATED_BIG_DECIMAL_REQUIRED_BOB = new BigDecimal(2);
    private static final BigDecimal SMALLER_BIG_DECIMAL_REQUIRED_BOB = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_BIG_DECIMAL_MIN_BOB = new BigDecimal(0);
    private static final BigDecimal UPDATED_BIG_DECIMAL_MIN_BOB = new BigDecimal(1);
    private static final BigDecimal SMALLER_BIG_DECIMAL_MIN_BOB = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_BIG_DECIMAL_MAX_BOB = new BigDecimal(100);
    private static final BigDecimal UPDATED_BIG_DECIMAL_MAX_BOB = new BigDecimal(99);
    private static final BigDecimal SMALLER_BIG_DECIMAL_MAX_BOB = new BigDecimal(100 - 1);

    private static final LocalDate DEFAULT_LOCAL_DATE_BOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOCAL_DATE_BOB = LocalDate.parse("2020-08-04");
    private static final LocalDate SMALLER_LOCAL_DATE_BOB = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LOCAL_DATE_REQUIRED_BOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOCAL_DATE_REQUIRED_BOB = LocalDate.parse("2020-08-04");
    private static final LocalDate SMALLER_LOCAL_DATE_REQUIRED_BOB = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_INSTANT_BOB = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSTANT_BOB = Instant.ofEpochMilli(1596513172471L);

    private static final Instant DEFAULT_INSTANTE_REQUIRED_BOB = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSTANTE_REQUIRED_BOB = Instant.ofEpochMilli(1596513172471L);

    private static final ZonedDateTime DEFAULT_ZONED_DATE_TIME_BOB = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ZONED_DATE_TIME_BOB = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(1596513172471L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime SMALLER_ZONED_DATE_TIME_BOB = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_ZONED_DATE_TIME_REQUIRED_BOB = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_ZONED_DATE_TIME_REQUIRED_BOB = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(1596513172471L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime SMALLER_ZONED_DATE_TIME_REQUIRED_BOB = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(-1L),
        ZoneOffset.UTC
    );

    private static final LocalTime DEFAULT_LOCAL_TIME_BOB = LocalTime.NOON;
    private static final LocalTime UPDATED_LOCAL_TIME_BOB = LocalTime.MAX.withNano(0);

    private static final LocalTime DEFAULT_LOCAL_TIME_REQUIRED_BOB = LocalTime.NOON;
    private static final LocalTime UPDATED_LOCAL_TIME_REQUIRED_BOB = LocalTime.MAX.withNano(0);

    private static final Duration DEFAULT_DURATION_BOB = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION_BOB = Duration.ofHours(12);
    private static final Duration SMALLER_DURATION_BOB = Duration.ofHours(5);

    private static final Duration DEFAULT_DURATION_REQUIRED_BOB = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION_REQUIRED_BOB = Duration.ofHours(12);
    private static final Duration SMALLER_DURATION_REQUIRED_BOB = Duration.ofHours(5);

    private static final Boolean DEFAULT_BOOLEAN_BOB = false;
    private static final Boolean UPDATED_BOOLEAN_BOB = true;

    private static final Boolean DEFAULT_BOOLEAN_REQUIRED_BOB = false;
    private static final Boolean UPDATED_BOOLEAN_REQUIRED_BOB = true;

    private static final EnumFieldClass DEFAULT_ENUM_BOB = EnumFieldClass.ENUM_VALUE_1;
    private static final EnumFieldClass UPDATED_ENUM_BOB = EnumFieldClass.ENUM_VALUE_2;

    private static final EnumRequiredFieldClass DEFAULT_ENUM_REQUIRED_BOB = EnumRequiredFieldClass.ENUM_VALUE_1;
    private static final EnumRequiredFieldClass UPDATED_ENUM_REQUIRED_BOB = EnumRequiredFieldClass.ENUM_VALUE_2;

    private static final UUID DEFAULT_UUID_BOB = UUID.randomUUID();
    private static final UUID UPDATED_UUID_BOB = UUID.randomUUID();

    private static final UUID DEFAULT_UUID_REQUIRED_BOB = UUID.randomUUID();
    private static final UUID UPDATED_UUID_REQUIRED_BOB = UUID.randomUUID();

    private static final byte[] DEFAULT_BYTE_IMAGE_BOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_BOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_BOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_BOB_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_IMAGE_REQUIRED_BOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_REQUIRED_BOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_REQUIRED_BOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_REQUIRED_BOB_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_IMAGE_MINBYTES_BOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_MINBYTES_BOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_MINBYTES_BOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_MINBYTES_BOB_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_IMAGE_MAXBYTES_BOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_MAXBYTES_BOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_MAXBYTES_BOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_MAXBYTES_BOB_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_BOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_BOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_BOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_BOB_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_REQUIRED_BOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_REQUIRED_BOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_REQUIRED_BOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_REQUIRED_BOB_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_MINBYTES_BOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_MINBYTES_BOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_MINBYTES_BOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_MINBYTES_BOB_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_MAXBYTES_BOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_MAXBYTES_BOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_MAXBYTES_BOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_MAXBYTES_BOB_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_BYTE_TEXT_BOB = "AAAAAAAAAA";
    private static final String UPDATED_BYTE_TEXT_BOB = "BBBBBBBBBB";

    private static final String DEFAULT_BYTE_TEXT_REQUIRED_BOB = "AAAAAAAAAA";
    private static final String UPDATED_BYTE_TEXT_REQUIRED_BOB = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/field-test-service-class-and-jpa-filtering-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FieldTestServiceClassAndJpaFilteringEntityRepository fieldTestServiceClassAndJpaFilteringEntityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FieldTestServiceClassAndJpaFilteringEntity fieldTestServiceClassAndJpaFilteringEntity;

    private FieldTestServiceClassAndJpaFilteringEntity insertedFieldTestServiceClassAndJpaFilteringEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldTestServiceClassAndJpaFilteringEntity createEntity() {
        return new FieldTestServiceClassAndJpaFilteringEntity()
            .stringBob(DEFAULT_STRING_BOB)
            .stringRequiredBob(DEFAULT_STRING_REQUIRED_BOB)
            .stringMinlengthBob(DEFAULT_STRING_MINLENGTH_BOB)
            .stringMaxlengthBob(DEFAULT_STRING_MAXLENGTH_BOB)
            .stringPatternBob(DEFAULT_STRING_PATTERN_BOB)
            .integerBob(DEFAULT_INTEGER_BOB)
            .integerRequiredBob(DEFAULT_INTEGER_REQUIRED_BOB)
            .integerMinBob(DEFAULT_INTEGER_MIN_BOB)
            .integerMaxBob(DEFAULT_INTEGER_MAX_BOB)
            .longBob(DEFAULT_LONG_BOB)
            .longRequiredBob(DEFAULT_LONG_REQUIRED_BOB)
            .longMinBob(DEFAULT_LONG_MIN_BOB)
            .longMaxBob(DEFAULT_LONG_MAX_BOB)
            .floatBob(DEFAULT_FLOAT_BOB)
            .floatRequiredBob(DEFAULT_FLOAT_REQUIRED_BOB)
            .floatMinBob(DEFAULT_FLOAT_MIN_BOB)
            .floatMaxBob(DEFAULT_FLOAT_MAX_BOB)
            .doubleRequiredBob(DEFAULT_DOUBLE_REQUIRED_BOB)
            .doubleMinBob(DEFAULT_DOUBLE_MIN_BOB)
            .doubleMaxBob(DEFAULT_DOUBLE_MAX_BOB)
            .bigDecimalRequiredBob(DEFAULT_BIG_DECIMAL_REQUIRED_BOB)
            .bigDecimalMinBob(DEFAULT_BIG_DECIMAL_MIN_BOB)
            .bigDecimalMaxBob(DEFAULT_BIG_DECIMAL_MAX_BOB)
            .localDateBob(DEFAULT_LOCAL_DATE_BOB)
            .localDateRequiredBob(DEFAULT_LOCAL_DATE_REQUIRED_BOB)
            .instantBob(DEFAULT_INSTANT_BOB)
            .instanteRequiredBob(DEFAULT_INSTANTE_REQUIRED_BOB)
            .zonedDateTimeBob(DEFAULT_ZONED_DATE_TIME_BOB)
            .zonedDateTimeRequiredBob(DEFAULT_ZONED_DATE_TIME_REQUIRED_BOB)
            .localTimeBob(DEFAULT_LOCAL_TIME_BOB)
            .localTimeRequiredBob(DEFAULT_LOCAL_TIME_REQUIRED_BOB)
            .durationBob(DEFAULT_DURATION_BOB)
            .durationRequiredBob(DEFAULT_DURATION_REQUIRED_BOB)
            .booleanBob(DEFAULT_BOOLEAN_BOB)
            .booleanRequiredBob(DEFAULT_BOOLEAN_REQUIRED_BOB)
            .enumBob(DEFAULT_ENUM_BOB)
            .enumRequiredBob(DEFAULT_ENUM_REQUIRED_BOB)
            .uuidBob(DEFAULT_UUID_BOB)
            .uuidRequiredBob(DEFAULT_UUID_REQUIRED_BOB)
            .byteImageBob(DEFAULT_BYTE_IMAGE_BOB)
            .byteImageBobContentType(DEFAULT_BYTE_IMAGE_BOB_CONTENT_TYPE)
            .byteImageRequiredBob(DEFAULT_BYTE_IMAGE_REQUIRED_BOB)
            .byteImageRequiredBobContentType(DEFAULT_BYTE_IMAGE_REQUIRED_BOB_CONTENT_TYPE)
            .byteImageMinbytesBob(DEFAULT_BYTE_IMAGE_MINBYTES_BOB)
            .byteImageMinbytesBobContentType(DEFAULT_BYTE_IMAGE_MINBYTES_BOB_CONTENT_TYPE)
            .byteImageMaxbytesBob(DEFAULT_BYTE_IMAGE_MAXBYTES_BOB)
            .byteImageMaxbytesBobContentType(DEFAULT_BYTE_IMAGE_MAXBYTES_BOB_CONTENT_TYPE)
            .byteAnyBob(DEFAULT_BYTE_ANY_BOB)
            .byteAnyBobContentType(DEFAULT_BYTE_ANY_BOB_CONTENT_TYPE)
            .byteAnyRequiredBob(DEFAULT_BYTE_ANY_REQUIRED_BOB)
            .byteAnyRequiredBobContentType(DEFAULT_BYTE_ANY_REQUIRED_BOB_CONTENT_TYPE)
            .byteAnyMinbytesBob(DEFAULT_BYTE_ANY_MINBYTES_BOB)
            .byteAnyMinbytesBobContentType(DEFAULT_BYTE_ANY_MINBYTES_BOB_CONTENT_TYPE)
            .byteAnyMaxbytesBob(DEFAULT_BYTE_ANY_MAXBYTES_BOB)
            .byteAnyMaxbytesBobContentType(DEFAULT_BYTE_ANY_MAXBYTES_BOB_CONTENT_TYPE)
            .byteTextBob(DEFAULT_BYTE_TEXT_BOB)
            .byteTextRequiredBob(DEFAULT_BYTE_TEXT_REQUIRED_BOB);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldTestServiceClassAndJpaFilteringEntity createUpdatedEntity() {
        return new FieldTestServiceClassAndJpaFilteringEntity()
            .stringBob(UPDATED_STRING_BOB)
            .stringRequiredBob(UPDATED_STRING_REQUIRED_BOB)
            .stringMinlengthBob(UPDATED_STRING_MINLENGTH_BOB)
            .stringMaxlengthBob(UPDATED_STRING_MAXLENGTH_BOB)
            .stringPatternBob(UPDATED_STRING_PATTERN_BOB)
            .integerBob(UPDATED_INTEGER_BOB)
            .integerRequiredBob(UPDATED_INTEGER_REQUIRED_BOB)
            .integerMinBob(UPDATED_INTEGER_MIN_BOB)
            .integerMaxBob(UPDATED_INTEGER_MAX_BOB)
            .longBob(UPDATED_LONG_BOB)
            .longRequiredBob(UPDATED_LONG_REQUIRED_BOB)
            .longMinBob(UPDATED_LONG_MIN_BOB)
            .longMaxBob(UPDATED_LONG_MAX_BOB)
            .floatBob(UPDATED_FLOAT_BOB)
            .floatRequiredBob(UPDATED_FLOAT_REQUIRED_BOB)
            .floatMinBob(UPDATED_FLOAT_MIN_BOB)
            .floatMaxBob(UPDATED_FLOAT_MAX_BOB)
            .doubleRequiredBob(UPDATED_DOUBLE_REQUIRED_BOB)
            .doubleMinBob(UPDATED_DOUBLE_MIN_BOB)
            .doubleMaxBob(UPDATED_DOUBLE_MAX_BOB)
            .bigDecimalRequiredBob(UPDATED_BIG_DECIMAL_REQUIRED_BOB)
            .bigDecimalMinBob(UPDATED_BIG_DECIMAL_MIN_BOB)
            .bigDecimalMaxBob(UPDATED_BIG_DECIMAL_MAX_BOB)
            .localDateBob(UPDATED_LOCAL_DATE_BOB)
            .localDateRequiredBob(UPDATED_LOCAL_DATE_REQUIRED_BOB)
            .instantBob(UPDATED_INSTANT_BOB)
            .instanteRequiredBob(UPDATED_INSTANTE_REQUIRED_BOB)
            .zonedDateTimeBob(UPDATED_ZONED_DATE_TIME_BOB)
            .zonedDateTimeRequiredBob(UPDATED_ZONED_DATE_TIME_REQUIRED_BOB)
            .localTimeBob(UPDATED_LOCAL_TIME_BOB)
            .localTimeRequiredBob(UPDATED_LOCAL_TIME_REQUIRED_BOB)
            .durationBob(UPDATED_DURATION_BOB)
            .durationRequiredBob(UPDATED_DURATION_REQUIRED_BOB)
            .booleanBob(UPDATED_BOOLEAN_BOB)
            .booleanRequiredBob(UPDATED_BOOLEAN_REQUIRED_BOB)
            .enumBob(UPDATED_ENUM_BOB)
            .enumRequiredBob(UPDATED_ENUM_REQUIRED_BOB)
            .uuidBob(UPDATED_UUID_BOB)
            .uuidRequiredBob(UPDATED_UUID_REQUIRED_BOB)
            .byteImageBob(UPDATED_BYTE_IMAGE_BOB)
            .byteImageBobContentType(UPDATED_BYTE_IMAGE_BOB_CONTENT_TYPE)
            .byteImageRequiredBob(UPDATED_BYTE_IMAGE_REQUIRED_BOB)
            .byteImageRequiredBobContentType(UPDATED_BYTE_IMAGE_REQUIRED_BOB_CONTENT_TYPE)
            .byteImageMinbytesBob(UPDATED_BYTE_IMAGE_MINBYTES_BOB)
            .byteImageMinbytesBobContentType(UPDATED_BYTE_IMAGE_MINBYTES_BOB_CONTENT_TYPE)
            .byteImageMaxbytesBob(UPDATED_BYTE_IMAGE_MAXBYTES_BOB)
            .byteImageMaxbytesBobContentType(UPDATED_BYTE_IMAGE_MAXBYTES_BOB_CONTENT_TYPE)
            .byteAnyBob(UPDATED_BYTE_ANY_BOB)
            .byteAnyBobContentType(UPDATED_BYTE_ANY_BOB_CONTENT_TYPE)
            .byteAnyRequiredBob(UPDATED_BYTE_ANY_REQUIRED_BOB)
            .byteAnyRequiredBobContentType(UPDATED_BYTE_ANY_REQUIRED_BOB_CONTENT_TYPE)
            .byteAnyMinbytesBob(UPDATED_BYTE_ANY_MINBYTES_BOB)
            .byteAnyMinbytesBobContentType(UPDATED_BYTE_ANY_MINBYTES_BOB_CONTENT_TYPE)
            .byteAnyMaxbytesBob(UPDATED_BYTE_ANY_MAXBYTES_BOB)
            .byteAnyMaxbytesBobContentType(UPDATED_BYTE_ANY_MAXBYTES_BOB_CONTENT_TYPE)
            .byteTextBob(UPDATED_BYTE_TEXT_BOB)
            .byteTextRequiredBob(UPDATED_BYTE_TEXT_REQUIRED_BOB);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FieldTestServiceClassAndJpaFilteringEntity.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        fieldTestServiceClassAndJpaFilteringEntity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFieldTestServiceClassAndJpaFilteringEntity != null) {
            fieldTestServiceClassAndJpaFilteringEntityRepository.delete(insertedFieldTestServiceClassAndJpaFilteringEntity).block();
            insertedFieldTestServiceClassAndJpaFilteringEntity = null;
        }
        deleteEntities(em);
    }

    @Test
    void createFieldTestServiceClassAndJpaFilteringEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FieldTestServiceClassAndJpaFilteringEntity
        var returnedFieldTestServiceClassAndJpaFilteringEntity = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FieldTestServiceClassAndJpaFilteringEntity.class)
            .returnResult()
            .getResponseBody();

        // Validate the FieldTestServiceClassAndJpaFilteringEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFieldTestServiceClassAndJpaFilteringEntityUpdatableFieldsEquals(
            returnedFieldTestServiceClassAndJpaFilteringEntity,
            getPersistedFieldTestServiceClassAndJpaFilteringEntity(returnedFieldTestServiceClassAndJpaFilteringEntity)
        );

        insertedFieldTestServiceClassAndJpaFilteringEntity = returnedFieldTestServiceClassAndJpaFilteringEntity;
    }

    @Test
    void createFieldTestServiceClassAndJpaFilteringEntityWithExistingId() throws Exception {
        // Create the FieldTestServiceClassAndJpaFilteringEntity with an existing ID
        fieldTestServiceClassAndJpaFilteringEntity.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestServiceClassAndJpaFilteringEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkStringRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setStringRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIntegerRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setIntegerRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLongRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setLongRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFloatRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setFloatRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDoubleRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setDoubleRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkBigDecimalRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setBigDecimalRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLocalDateRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setLocalDateRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkInstanteRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setInstanteRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkZonedDateTimeRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setZonedDateTimeRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLocalTimeRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setLocalTimeRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDurationRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setDurationRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkBooleanRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setBooleanRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEnumRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setEnumRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUuidRequiredBobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestServiceClassAndJpaFilteringEntity.setUuidRequiredBob(null);

        // Create the FieldTestServiceClassAndJpaFilteringEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntities() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(fieldTestServiceClassAndJpaFilteringEntity.getId().intValue()))
            .jsonPath("$.[*].stringBob")
            .value(hasItem(DEFAULT_STRING_BOB))
            .jsonPath("$.[*].stringRequiredBob")
            .value(hasItem(DEFAULT_STRING_REQUIRED_BOB))
            .jsonPath("$.[*].stringMinlengthBob")
            .value(hasItem(DEFAULT_STRING_MINLENGTH_BOB))
            .jsonPath("$.[*].stringMaxlengthBob")
            .value(hasItem(DEFAULT_STRING_MAXLENGTH_BOB))
            .jsonPath("$.[*].stringPatternBob")
            .value(hasItem(DEFAULT_STRING_PATTERN_BOB))
            .jsonPath("$.[*].integerBob")
            .value(hasItem(DEFAULT_INTEGER_BOB))
            .jsonPath("$.[*].integerRequiredBob")
            .value(hasItem(DEFAULT_INTEGER_REQUIRED_BOB))
            .jsonPath("$.[*].integerMinBob")
            .value(hasItem(DEFAULT_INTEGER_MIN_BOB))
            .jsonPath("$.[*].integerMaxBob")
            .value(hasItem(DEFAULT_INTEGER_MAX_BOB))
            .jsonPath("$.[*].longBob")
            .value(hasItem(DEFAULT_LONG_BOB.intValue()))
            .jsonPath("$.[*].longRequiredBob")
            .value(hasItem(DEFAULT_LONG_REQUIRED_BOB.intValue()))
            .jsonPath("$.[*].longMinBob")
            .value(hasItem(DEFAULT_LONG_MIN_BOB.intValue()))
            .jsonPath("$.[*].longMaxBob")
            .value(hasItem(DEFAULT_LONG_MAX_BOB.intValue()))
            .jsonPath("$.[*].floatBob")
            .value(hasItem(DEFAULT_FLOAT_BOB.doubleValue()))
            .jsonPath("$.[*].floatRequiredBob")
            .value(hasItem(DEFAULT_FLOAT_REQUIRED_BOB.doubleValue()))
            .jsonPath("$.[*].floatMinBob")
            .value(hasItem(DEFAULT_FLOAT_MIN_BOB.doubleValue()))
            .jsonPath("$.[*].floatMaxBob")
            .value(hasItem(DEFAULT_FLOAT_MAX_BOB.doubleValue()))
            .jsonPath("$.[*].doubleRequiredBob")
            .value(hasItem(DEFAULT_DOUBLE_REQUIRED_BOB))
            .jsonPath("$.[*].doubleMinBob")
            .value(hasItem(DEFAULT_DOUBLE_MIN_BOB))
            .jsonPath("$.[*].doubleMaxBob")
            .value(hasItem(DEFAULT_DOUBLE_MAX_BOB))
            .jsonPath("$.[*].bigDecimalRequiredBob")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_REQUIRED_BOB)))
            .jsonPath("$.[*].bigDecimalMinBob")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_MIN_BOB)))
            .jsonPath("$.[*].bigDecimalMaxBob")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_MAX_BOB)))
            .jsonPath("$.[*].localDateBob")
            .value(hasItem(DEFAULT_LOCAL_DATE_BOB.toString()))
            .jsonPath("$.[*].localDateRequiredBob")
            .value(hasItem(DEFAULT_LOCAL_DATE_REQUIRED_BOB.toString()))
            .jsonPath("$.[*].instantBob")
            .value(hasItem(DEFAULT_INSTANT_BOB.toString()))
            .jsonPath("$.[*].instanteRequiredBob")
            .value(hasItem(DEFAULT_INSTANTE_REQUIRED_BOB.toString()))
            .jsonPath("$.[*].zonedDateTimeBob")
            .value(hasItem(sameInstant(DEFAULT_ZONED_DATE_TIME_BOB)))
            .jsonPath("$.[*].zonedDateTimeRequiredBob")
            .value(hasItem(sameInstant(DEFAULT_ZONED_DATE_TIME_REQUIRED_BOB)))
            .jsonPath("$.[*].localTimeBob")
            .value(hasItem(DEFAULT_LOCAL_TIME_BOB.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.[*].localTimeRequiredBob")
            .value(hasItem(DEFAULT_LOCAL_TIME_REQUIRED_BOB.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.[*].durationBob")
            .value(hasItem(DEFAULT_DURATION_BOB.toString()))
            .jsonPath("$.[*].durationRequiredBob")
            .value(hasItem(DEFAULT_DURATION_REQUIRED_BOB.toString()))
            .jsonPath("$.[*].booleanBob")
            .value(hasItem(DEFAULT_BOOLEAN_BOB))
            .jsonPath("$.[*].booleanRequiredBob")
            .value(hasItem(DEFAULT_BOOLEAN_REQUIRED_BOB))
            .jsonPath("$.[*].enumBob")
            .value(hasItem(DEFAULT_ENUM_BOB.toString()))
            .jsonPath("$.[*].enumRequiredBob")
            .value(hasItem(DEFAULT_ENUM_REQUIRED_BOB.toString()))
            .jsonPath("$.[*].uuidBob")
            .value(hasItem(DEFAULT_UUID_BOB.toString()))
            .jsonPath("$.[*].uuidRequiredBob")
            .value(hasItem(DEFAULT_UUID_REQUIRED_BOB.toString()))
            .jsonPath("$.[*].byteImageBobContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_BOB)))
            .jsonPath("$.[*].byteImageRequiredBobContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_REQUIRED_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageRequiredBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_REQUIRED_BOB)))
            .jsonPath("$.[*].byteImageMinbytesBobContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_MINBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageMinbytesBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MINBYTES_BOB)))
            .jsonPath("$.[*].byteImageMaxbytesBobContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_MAXBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageMaxbytesBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MAXBYTES_BOB)))
            .jsonPath("$.[*].byteAnyBobContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_BOB)))
            .jsonPath("$.[*].byteAnyRequiredBobContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_REQUIRED_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyRequiredBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_REQUIRED_BOB)))
            .jsonPath("$.[*].byteAnyMinbytesBobContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_MINBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyMinbytesBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MINBYTES_BOB)))
            .jsonPath("$.[*].byteAnyMaxbytesBobContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_MAXBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyMaxbytesBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MAXBYTES_BOB)))
            .jsonPath("$.[*].byteTextBob")
            .value(hasItem(DEFAULT_BYTE_TEXT_BOB))
            .jsonPath("$.[*].byteTextRequiredBob")
            .value(hasItem(DEFAULT_BYTE_TEXT_REQUIRED_BOB));
    }

    @Test
    void getFieldTestServiceClassAndJpaFilteringEntity() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get the fieldTestServiceClassAndJpaFilteringEntity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, fieldTestServiceClassAndJpaFilteringEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(fieldTestServiceClassAndJpaFilteringEntity.getId().intValue()))
            .jsonPath("$.stringBob")
            .value(is(DEFAULT_STRING_BOB))
            .jsonPath("$.stringRequiredBob")
            .value(is(DEFAULT_STRING_REQUIRED_BOB))
            .jsonPath("$.stringMinlengthBob")
            .value(is(DEFAULT_STRING_MINLENGTH_BOB))
            .jsonPath("$.stringMaxlengthBob")
            .value(is(DEFAULT_STRING_MAXLENGTH_BOB))
            .jsonPath("$.stringPatternBob")
            .value(is(DEFAULT_STRING_PATTERN_BOB))
            .jsonPath("$.integerBob")
            .value(is(DEFAULT_INTEGER_BOB))
            .jsonPath("$.integerRequiredBob")
            .value(is(DEFAULT_INTEGER_REQUIRED_BOB))
            .jsonPath("$.integerMinBob")
            .value(is(DEFAULT_INTEGER_MIN_BOB))
            .jsonPath("$.integerMaxBob")
            .value(is(DEFAULT_INTEGER_MAX_BOB))
            .jsonPath("$.longBob")
            .value(is(DEFAULT_LONG_BOB.intValue()))
            .jsonPath("$.longRequiredBob")
            .value(is(DEFAULT_LONG_REQUIRED_BOB.intValue()))
            .jsonPath("$.longMinBob")
            .value(is(DEFAULT_LONG_MIN_BOB.intValue()))
            .jsonPath("$.longMaxBob")
            .value(is(DEFAULT_LONG_MAX_BOB.intValue()))
            .jsonPath("$.floatBob")
            .value(is(DEFAULT_FLOAT_BOB.doubleValue()))
            .jsonPath("$.floatRequiredBob")
            .value(is(DEFAULT_FLOAT_REQUIRED_BOB.doubleValue()))
            .jsonPath("$.floatMinBob")
            .value(is(DEFAULT_FLOAT_MIN_BOB.doubleValue()))
            .jsonPath("$.floatMaxBob")
            .value(is(DEFAULT_FLOAT_MAX_BOB.doubleValue()))
            .jsonPath("$.doubleRequiredBob")
            .value(is(DEFAULT_DOUBLE_REQUIRED_BOB))
            .jsonPath("$.doubleMinBob")
            .value(is(DEFAULT_DOUBLE_MIN_BOB))
            .jsonPath("$.doubleMaxBob")
            .value(is(DEFAULT_DOUBLE_MAX_BOB))
            .jsonPath("$.bigDecimalRequiredBob")
            .value(is(sameNumber(DEFAULT_BIG_DECIMAL_REQUIRED_BOB)))
            .jsonPath("$.bigDecimalMinBob")
            .value(is(sameNumber(DEFAULT_BIG_DECIMAL_MIN_BOB)))
            .jsonPath("$.bigDecimalMaxBob")
            .value(is(sameNumber(DEFAULT_BIG_DECIMAL_MAX_BOB)))
            .jsonPath("$.localDateBob")
            .value(is(DEFAULT_LOCAL_DATE_BOB.toString()))
            .jsonPath("$.localDateRequiredBob")
            .value(is(DEFAULT_LOCAL_DATE_REQUIRED_BOB.toString()))
            .jsonPath("$.instantBob")
            .value(is(DEFAULT_INSTANT_BOB.toString()))
            .jsonPath("$.instanteRequiredBob")
            .value(is(DEFAULT_INSTANTE_REQUIRED_BOB.toString()))
            .jsonPath("$.zonedDateTimeBob")
            .value(is(sameInstant(DEFAULT_ZONED_DATE_TIME_BOB)))
            .jsonPath("$.zonedDateTimeRequiredBob")
            .value(is(sameInstant(DEFAULT_ZONED_DATE_TIME_REQUIRED_BOB)))
            .jsonPath("$.localTimeBob")
            .value(is(DEFAULT_LOCAL_TIME_BOB.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.localTimeRequiredBob")
            .value(is(DEFAULT_LOCAL_TIME_REQUIRED_BOB.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.durationBob")
            .value(is(DEFAULT_DURATION_BOB.toString()))
            .jsonPath("$.durationRequiredBob")
            .value(is(DEFAULT_DURATION_REQUIRED_BOB.toString()))
            .jsonPath("$.booleanBob")
            .value(is(DEFAULT_BOOLEAN_BOB))
            .jsonPath("$.booleanRequiredBob")
            .value(is(DEFAULT_BOOLEAN_REQUIRED_BOB))
            .jsonPath("$.enumBob")
            .value(is(DEFAULT_ENUM_BOB.toString()))
            .jsonPath("$.enumRequiredBob")
            .value(is(DEFAULT_ENUM_REQUIRED_BOB.toString()))
            .jsonPath("$.uuidBob")
            .value(is(DEFAULT_UUID_BOB.toString()))
            .jsonPath("$.uuidRequiredBob")
            .value(is(DEFAULT_UUID_REQUIRED_BOB.toString()))
            .jsonPath("$.byteImageBobContentType")
            .value(is(DEFAULT_BYTE_IMAGE_BOB_CONTENT_TYPE))
            .jsonPath("$.byteImageBob")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_BOB)))
            .jsonPath("$.byteImageRequiredBobContentType")
            .value(is(DEFAULT_BYTE_IMAGE_REQUIRED_BOB_CONTENT_TYPE))
            .jsonPath("$.byteImageRequiredBob")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_REQUIRED_BOB)))
            .jsonPath("$.byteImageMinbytesBobContentType")
            .value(is(DEFAULT_BYTE_IMAGE_MINBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.byteImageMinbytesBob")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MINBYTES_BOB)))
            .jsonPath("$.byteImageMaxbytesBobContentType")
            .value(is(DEFAULT_BYTE_IMAGE_MAXBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.byteImageMaxbytesBob")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MAXBYTES_BOB)))
            .jsonPath("$.byteAnyBobContentType")
            .value(is(DEFAULT_BYTE_ANY_BOB_CONTENT_TYPE))
            .jsonPath("$.byteAnyBob")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_BOB)))
            .jsonPath("$.byteAnyRequiredBobContentType")
            .value(is(DEFAULT_BYTE_ANY_REQUIRED_BOB_CONTENT_TYPE))
            .jsonPath("$.byteAnyRequiredBob")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_REQUIRED_BOB)))
            .jsonPath("$.byteAnyMinbytesBobContentType")
            .value(is(DEFAULT_BYTE_ANY_MINBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.byteAnyMinbytesBob")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MINBYTES_BOB)))
            .jsonPath("$.byteAnyMaxbytesBobContentType")
            .value(is(DEFAULT_BYTE_ANY_MAXBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.byteAnyMaxbytesBob")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MAXBYTES_BOB)))
            .jsonPath("$.byteTextBob")
            .value(is(DEFAULT_BYTE_TEXT_BOB))
            .jsonPath("$.byteTextRequiredBob")
            .value(is(DEFAULT_BYTE_TEXT_REQUIRED_BOB));
    }

    @Test
    void getFieldTestServiceClassAndJpaFilteringEntitiesByIdFiltering() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        Long id = fieldTestServiceClassAndJpaFilteringEntity.getId();

        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringBob.equals=" + DEFAULT_STRING_BOB,
            "stringBob.equals=" + UPDATED_STRING_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringBob.in=" + DEFAULT_STRING_BOB + "," + UPDATED_STRING_BOB,
            "stringBob.in=" + UPDATED_STRING_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("stringBob.specified=true", "stringBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringBobContainsSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringBob contains
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringBob.contains=" + DEFAULT_STRING_BOB,
            "stringBob.contains=" + UPDATED_STRING_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringBobNotContainsSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringBob does not contain
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringBob.doesNotContain=" + UPDATED_STRING_BOB,
            "stringBob.doesNotContain=" + DEFAULT_STRING_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringRequiredBob.equals=" + DEFAULT_STRING_REQUIRED_BOB,
            "stringRequiredBob.equals=" + UPDATED_STRING_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringRequiredBob.in=" + DEFAULT_STRING_REQUIRED_BOB + "," + UPDATED_STRING_REQUIRED_BOB,
            "stringRequiredBob.in=" + UPDATED_STRING_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("stringRequiredBob.specified=true", "stringRequiredBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringRequiredBobContainsSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringRequiredBob contains
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringRequiredBob.contains=" + DEFAULT_STRING_REQUIRED_BOB,
            "stringRequiredBob.contains=" + UPDATED_STRING_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringRequiredBobNotContainsSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringRequiredBob does not contain
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringRequiredBob.doesNotContain=" + UPDATED_STRING_REQUIRED_BOB,
            "stringRequiredBob.doesNotContain=" + DEFAULT_STRING_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringMinlengthBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringMinlengthBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringMinlengthBob.equals=" + DEFAULT_STRING_MINLENGTH_BOB,
            "stringMinlengthBob.equals=" + UPDATED_STRING_MINLENGTH_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringMinlengthBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringMinlengthBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringMinlengthBob.in=" + DEFAULT_STRING_MINLENGTH_BOB + "," + UPDATED_STRING_MINLENGTH_BOB,
            "stringMinlengthBob.in=" + UPDATED_STRING_MINLENGTH_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringMinlengthBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringMinlengthBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringMinlengthBob.specified=true",
            "stringMinlengthBob.specified=false"
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringMinlengthBobContainsSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringMinlengthBob contains
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringMinlengthBob.contains=" + DEFAULT_STRING_MINLENGTH_BOB,
            "stringMinlengthBob.contains=" + UPDATED_STRING_MINLENGTH_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringMinlengthBobNotContainsSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringMinlengthBob does not contain
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringMinlengthBob.doesNotContain=" + UPDATED_STRING_MINLENGTH_BOB,
            "stringMinlengthBob.doesNotContain=" + DEFAULT_STRING_MINLENGTH_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringMaxlengthBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringMaxlengthBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringMaxlengthBob.equals=" + DEFAULT_STRING_MAXLENGTH_BOB,
            "stringMaxlengthBob.equals=" + UPDATED_STRING_MAXLENGTH_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringMaxlengthBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringMaxlengthBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringMaxlengthBob.in=" + DEFAULT_STRING_MAXLENGTH_BOB + "," + UPDATED_STRING_MAXLENGTH_BOB,
            "stringMaxlengthBob.in=" + UPDATED_STRING_MAXLENGTH_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringMaxlengthBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringMaxlengthBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringMaxlengthBob.specified=true",
            "stringMaxlengthBob.specified=false"
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringMaxlengthBobContainsSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringMaxlengthBob contains
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringMaxlengthBob.contains=" + DEFAULT_STRING_MAXLENGTH_BOB,
            "stringMaxlengthBob.contains=" + UPDATED_STRING_MAXLENGTH_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringMaxlengthBobNotContainsSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringMaxlengthBob does not contain
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringMaxlengthBob.doesNotContain=" + UPDATED_STRING_MAXLENGTH_BOB,
            "stringMaxlengthBob.doesNotContain=" + DEFAULT_STRING_MAXLENGTH_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringPatternBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringPatternBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringPatternBob.equals=" + DEFAULT_STRING_PATTERN_BOB,
            "stringPatternBob.equals=" + UPDATED_STRING_PATTERN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringPatternBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringPatternBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringPatternBob.in=" + DEFAULT_STRING_PATTERN_BOB + "," + UPDATED_STRING_PATTERN_BOB,
            "stringPatternBob.in=" + UPDATED_STRING_PATTERN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringPatternBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringPatternBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("stringPatternBob.specified=true", "stringPatternBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringPatternBobContainsSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringPatternBob contains
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringPatternBob.contains=" + DEFAULT_STRING_PATTERN_BOB,
            "stringPatternBob.contains=" + UPDATED_STRING_PATTERN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByStringPatternBobNotContainsSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where stringPatternBob does not contain
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "stringPatternBob.doesNotContain=" + UPDATED_STRING_PATTERN_BOB,
            "stringPatternBob.doesNotContain=" + DEFAULT_STRING_PATTERN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerBob.equals=" + DEFAULT_INTEGER_BOB,
            "integerBob.equals=" + UPDATED_INTEGER_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerBob.in=" + DEFAULT_INTEGER_BOB + "," + UPDATED_INTEGER_BOB,
            "integerBob.in=" + UPDATED_INTEGER_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("integerBob.specified=true", "integerBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerBob.greaterThanOrEqual=" + DEFAULT_INTEGER_BOB,
            "integerBob.greaterThanOrEqual=" + UPDATED_INTEGER_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerBob.lessThanOrEqual=" + DEFAULT_INTEGER_BOB,
            "integerBob.lessThanOrEqual=" + SMALLER_INTEGER_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerBob.lessThan=" + UPDATED_INTEGER_BOB,
            "integerBob.lessThan=" + DEFAULT_INTEGER_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerBob.greaterThan=" + SMALLER_INTEGER_BOB,
            "integerBob.greaterThan=" + DEFAULT_INTEGER_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerRequiredBob.equals=" + DEFAULT_INTEGER_REQUIRED_BOB,
            "integerRequiredBob.equals=" + UPDATED_INTEGER_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerRequiredBob.in=" + DEFAULT_INTEGER_REQUIRED_BOB + "," + UPDATED_INTEGER_REQUIRED_BOB,
            "integerRequiredBob.in=" + UPDATED_INTEGER_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerRequiredBob.specified=true",
            "integerRequiredBob.specified=false"
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerRequiredBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerRequiredBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerRequiredBob.greaterThanOrEqual=" + DEFAULT_INTEGER_REQUIRED_BOB,
            "integerRequiredBob.greaterThanOrEqual=" + UPDATED_INTEGER_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerRequiredBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerRequiredBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerRequiredBob.lessThanOrEqual=" + DEFAULT_INTEGER_REQUIRED_BOB,
            "integerRequiredBob.lessThanOrEqual=" + SMALLER_INTEGER_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerRequiredBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerRequiredBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerRequiredBob.lessThan=" + UPDATED_INTEGER_REQUIRED_BOB,
            "integerRequiredBob.lessThan=" + DEFAULT_INTEGER_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerRequiredBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerRequiredBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerRequiredBob.greaterThan=" + SMALLER_INTEGER_REQUIRED_BOB,
            "integerRequiredBob.greaterThan=" + DEFAULT_INTEGER_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMinBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMinBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMinBob.equals=" + DEFAULT_INTEGER_MIN_BOB,
            "integerMinBob.equals=" + UPDATED_INTEGER_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMinBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMinBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMinBob.in=" + DEFAULT_INTEGER_MIN_BOB + "," + UPDATED_INTEGER_MIN_BOB,
            "integerMinBob.in=" + UPDATED_INTEGER_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMinBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMinBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("integerMinBob.specified=true", "integerMinBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMinBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMinBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMinBob.greaterThanOrEqual=" + DEFAULT_INTEGER_MIN_BOB,
            "integerMinBob.greaterThanOrEqual=" + UPDATED_INTEGER_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMinBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMinBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMinBob.lessThanOrEqual=" + DEFAULT_INTEGER_MIN_BOB,
            "integerMinBob.lessThanOrEqual=" + SMALLER_INTEGER_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMinBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMinBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMinBob.lessThan=" + UPDATED_INTEGER_MIN_BOB,
            "integerMinBob.lessThan=" + DEFAULT_INTEGER_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMinBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMinBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMinBob.greaterThan=" + SMALLER_INTEGER_MIN_BOB,
            "integerMinBob.greaterThan=" + DEFAULT_INTEGER_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMaxBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMaxBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMaxBob.equals=" + DEFAULT_INTEGER_MAX_BOB,
            "integerMaxBob.equals=" + UPDATED_INTEGER_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMaxBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMaxBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMaxBob.in=" + DEFAULT_INTEGER_MAX_BOB + "," + UPDATED_INTEGER_MAX_BOB,
            "integerMaxBob.in=" + UPDATED_INTEGER_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMaxBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMaxBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("integerMaxBob.specified=true", "integerMaxBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMaxBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMaxBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMaxBob.greaterThanOrEqual=" + DEFAULT_INTEGER_MAX_BOB,
            "integerMaxBob.greaterThanOrEqual=" + (DEFAULT_INTEGER_MAX_BOB + 1)
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMaxBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMaxBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMaxBob.lessThanOrEqual=" + DEFAULT_INTEGER_MAX_BOB,
            "integerMaxBob.lessThanOrEqual=" + SMALLER_INTEGER_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMaxBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMaxBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMaxBob.lessThan=" + (DEFAULT_INTEGER_MAX_BOB + 1),
            "integerMaxBob.lessThan=" + DEFAULT_INTEGER_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByIntegerMaxBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where integerMaxBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "integerMaxBob.greaterThan=" + SMALLER_INTEGER_MAX_BOB,
            "integerMaxBob.greaterThan=" + DEFAULT_INTEGER_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longBob.equals=" + DEFAULT_LONG_BOB,
            "longBob.equals=" + UPDATED_LONG_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longBob.in=" + DEFAULT_LONG_BOB + "," + UPDATED_LONG_BOB,
            "longBob.in=" + UPDATED_LONG_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("longBob.specified=true", "longBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longBob.greaterThanOrEqual=" + DEFAULT_LONG_BOB,
            "longBob.greaterThanOrEqual=" + UPDATED_LONG_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longBob.lessThanOrEqual=" + DEFAULT_LONG_BOB,
            "longBob.lessThanOrEqual=" + SMALLER_LONG_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longBob.lessThan=" + UPDATED_LONG_BOB,
            "longBob.lessThan=" + DEFAULT_LONG_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longBob.greaterThan=" + SMALLER_LONG_BOB,
            "longBob.greaterThan=" + DEFAULT_LONG_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longRequiredBob.equals=" + DEFAULT_LONG_REQUIRED_BOB,
            "longRequiredBob.equals=" + UPDATED_LONG_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longRequiredBob.in=" + DEFAULT_LONG_REQUIRED_BOB + "," + UPDATED_LONG_REQUIRED_BOB,
            "longRequiredBob.in=" + UPDATED_LONG_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("longRequiredBob.specified=true", "longRequiredBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongRequiredBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longRequiredBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longRequiredBob.greaterThanOrEqual=" + DEFAULT_LONG_REQUIRED_BOB,
            "longRequiredBob.greaterThanOrEqual=" + UPDATED_LONG_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongRequiredBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longRequiredBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longRequiredBob.lessThanOrEqual=" + DEFAULT_LONG_REQUIRED_BOB,
            "longRequiredBob.lessThanOrEqual=" + SMALLER_LONG_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongRequiredBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longRequiredBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longRequiredBob.lessThan=" + UPDATED_LONG_REQUIRED_BOB,
            "longRequiredBob.lessThan=" + DEFAULT_LONG_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongRequiredBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longRequiredBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longRequiredBob.greaterThan=" + SMALLER_LONG_REQUIRED_BOB,
            "longRequiredBob.greaterThan=" + DEFAULT_LONG_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMinBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMinBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMinBob.equals=" + DEFAULT_LONG_MIN_BOB,
            "longMinBob.equals=" + UPDATED_LONG_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMinBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMinBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMinBob.in=" + DEFAULT_LONG_MIN_BOB + "," + UPDATED_LONG_MIN_BOB,
            "longMinBob.in=" + UPDATED_LONG_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMinBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMinBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("longMinBob.specified=true", "longMinBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMinBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMinBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMinBob.greaterThanOrEqual=" + DEFAULT_LONG_MIN_BOB,
            "longMinBob.greaterThanOrEqual=" + UPDATED_LONG_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMinBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMinBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMinBob.lessThanOrEqual=" + DEFAULT_LONG_MIN_BOB,
            "longMinBob.lessThanOrEqual=" + SMALLER_LONG_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMinBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMinBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMinBob.lessThan=" + UPDATED_LONG_MIN_BOB,
            "longMinBob.lessThan=" + DEFAULT_LONG_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMinBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMinBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMinBob.greaterThan=" + SMALLER_LONG_MIN_BOB,
            "longMinBob.greaterThan=" + DEFAULT_LONG_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMaxBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMaxBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMaxBob.equals=" + DEFAULT_LONG_MAX_BOB,
            "longMaxBob.equals=" + UPDATED_LONG_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMaxBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMaxBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMaxBob.in=" + DEFAULT_LONG_MAX_BOB + "," + UPDATED_LONG_MAX_BOB,
            "longMaxBob.in=" + UPDATED_LONG_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMaxBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMaxBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("longMaxBob.specified=true", "longMaxBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMaxBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMaxBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMaxBob.greaterThanOrEqual=" + DEFAULT_LONG_MAX_BOB,
            "longMaxBob.greaterThanOrEqual=" + (DEFAULT_LONG_MAX_BOB + 1)
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMaxBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMaxBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMaxBob.lessThanOrEqual=" + DEFAULT_LONG_MAX_BOB,
            "longMaxBob.lessThanOrEqual=" + SMALLER_LONG_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMaxBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMaxBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMaxBob.lessThan=" + (DEFAULT_LONG_MAX_BOB + 1),
            "longMaxBob.lessThan=" + DEFAULT_LONG_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLongMaxBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where longMaxBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "longMaxBob.greaterThan=" + SMALLER_LONG_MAX_BOB,
            "longMaxBob.greaterThan=" + DEFAULT_LONG_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatBob.equals=" + DEFAULT_FLOAT_BOB,
            "floatBob.equals=" + UPDATED_FLOAT_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatBob.in=" + DEFAULT_FLOAT_BOB + "," + UPDATED_FLOAT_BOB,
            "floatBob.in=" + UPDATED_FLOAT_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("floatBob.specified=true", "floatBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatBob.greaterThanOrEqual=" + DEFAULT_FLOAT_BOB,
            "floatBob.greaterThanOrEqual=" + UPDATED_FLOAT_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatBob.lessThanOrEqual=" + DEFAULT_FLOAT_BOB,
            "floatBob.lessThanOrEqual=" + SMALLER_FLOAT_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatBob.lessThan=" + UPDATED_FLOAT_BOB,
            "floatBob.lessThan=" + DEFAULT_FLOAT_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatBob.greaterThan=" + SMALLER_FLOAT_BOB,
            "floatBob.greaterThan=" + DEFAULT_FLOAT_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatRequiredBob.equals=" + DEFAULT_FLOAT_REQUIRED_BOB,
            "floatRequiredBob.equals=" + UPDATED_FLOAT_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatRequiredBob.in=" + DEFAULT_FLOAT_REQUIRED_BOB + "," + UPDATED_FLOAT_REQUIRED_BOB,
            "floatRequiredBob.in=" + UPDATED_FLOAT_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("floatRequiredBob.specified=true", "floatRequiredBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatRequiredBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatRequiredBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatRequiredBob.greaterThanOrEqual=" + DEFAULT_FLOAT_REQUIRED_BOB,
            "floatRequiredBob.greaterThanOrEqual=" + UPDATED_FLOAT_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatRequiredBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatRequiredBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatRequiredBob.lessThanOrEqual=" + DEFAULT_FLOAT_REQUIRED_BOB,
            "floatRequiredBob.lessThanOrEqual=" + SMALLER_FLOAT_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatRequiredBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatRequiredBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatRequiredBob.lessThan=" + UPDATED_FLOAT_REQUIRED_BOB,
            "floatRequiredBob.lessThan=" + DEFAULT_FLOAT_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatRequiredBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatRequiredBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatRequiredBob.greaterThan=" + SMALLER_FLOAT_REQUIRED_BOB,
            "floatRequiredBob.greaterThan=" + DEFAULT_FLOAT_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMinBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMinBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMinBob.equals=" + DEFAULT_FLOAT_MIN_BOB,
            "floatMinBob.equals=" + UPDATED_FLOAT_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMinBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMinBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMinBob.in=" + DEFAULT_FLOAT_MIN_BOB + "," + UPDATED_FLOAT_MIN_BOB,
            "floatMinBob.in=" + UPDATED_FLOAT_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMinBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMinBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("floatMinBob.specified=true", "floatMinBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMinBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMinBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMinBob.greaterThanOrEqual=" + DEFAULT_FLOAT_MIN_BOB,
            "floatMinBob.greaterThanOrEqual=" + UPDATED_FLOAT_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMinBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMinBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMinBob.lessThanOrEqual=" + DEFAULT_FLOAT_MIN_BOB,
            "floatMinBob.lessThanOrEqual=" + SMALLER_FLOAT_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMinBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMinBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMinBob.lessThan=" + UPDATED_FLOAT_MIN_BOB,
            "floatMinBob.lessThan=" + DEFAULT_FLOAT_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMinBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMinBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMinBob.greaterThan=" + SMALLER_FLOAT_MIN_BOB,
            "floatMinBob.greaterThan=" + DEFAULT_FLOAT_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMaxBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMaxBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMaxBob.equals=" + DEFAULT_FLOAT_MAX_BOB,
            "floatMaxBob.equals=" + UPDATED_FLOAT_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMaxBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMaxBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMaxBob.in=" + DEFAULT_FLOAT_MAX_BOB + "," + UPDATED_FLOAT_MAX_BOB,
            "floatMaxBob.in=" + UPDATED_FLOAT_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMaxBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMaxBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("floatMaxBob.specified=true", "floatMaxBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMaxBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMaxBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMaxBob.greaterThanOrEqual=" + DEFAULT_FLOAT_MAX_BOB,
            "floatMaxBob.greaterThanOrEqual=" + (DEFAULT_FLOAT_MAX_BOB + 1)
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMaxBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMaxBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMaxBob.lessThanOrEqual=" + DEFAULT_FLOAT_MAX_BOB,
            "floatMaxBob.lessThanOrEqual=" + SMALLER_FLOAT_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMaxBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMaxBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMaxBob.lessThan=" + (DEFAULT_FLOAT_MAX_BOB + 1),
            "floatMaxBob.lessThan=" + DEFAULT_FLOAT_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByFloatMaxBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where floatMaxBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "floatMaxBob.greaterThan=" + SMALLER_FLOAT_MAX_BOB,
            "floatMaxBob.greaterThan=" + DEFAULT_FLOAT_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleRequiredBob.equals=" + DEFAULT_DOUBLE_REQUIRED_BOB,
            "doubleRequiredBob.equals=" + UPDATED_DOUBLE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleRequiredBob.in=" + DEFAULT_DOUBLE_REQUIRED_BOB + "," + UPDATED_DOUBLE_REQUIRED_BOB,
            "doubleRequiredBob.in=" + UPDATED_DOUBLE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("doubleRequiredBob.specified=true", "doubleRequiredBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleRequiredBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleRequiredBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleRequiredBob.greaterThanOrEqual=" + DEFAULT_DOUBLE_REQUIRED_BOB,
            "doubleRequiredBob.greaterThanOrEqual=" + UPDATED_DOUBLE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleRequiredBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleRequiredBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleRequiredBob.lessThanOrEqual=" + DEFAULT_DOUBLE_REQUIRED_BOB,
            "doubleRequiredBob.lessThanOrEqual=" + SMALLER_DOUBLE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleRequiredBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleRequiredBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleRequiredBob.lessThan=" + UPDATED_DOUBLE_REQUIRED_BOB,
            "doubleRequiredBob.lessThan=" + DEFAULT_DOUBLE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleRequiredBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleRequiredBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleRequiredBob.greaterThan=" + SMALLER_DOUBLE_REQUIRED_BOB,
            "doubleRequiredBob.greaterThan=" + DEFAULT_DOUBLE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMinBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMinBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMinBob.equals=" + DEFAULT_DOUBLE_MIN_BOB,
            "doubleMinBob.equals=" + UPDATED_DOUBLE_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMinBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMinBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMinBob.in=" + DEFAULT_DOUBLE_MIN_BOB + "," + UPDATED_DOUBLE_MIN_BOB,
            "doubleMinBob.in=" + UPDATED_DOUBLE_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMinBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMinBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("doubleMinBob.specified=true", "doubleMinBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMinBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMinBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMinBob.greaterThanOrEqual=" + DEFAULT_DOUBLE_MIN_BOB,
            "doubleMinBob.greaterThanOrEqual=" + UPDATED_DOUBLE_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMinBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMinBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMinBob.lessThanOrEqual=" + DEFAULT_DOUBLE_MIN_BOB,
            "doubleMinBob.lessThanOrEqual=" + SMALLER_DOUBLE_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMinBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMinBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMinBob.lessThan=" + UPDATED_DOUBLE_MIN_BOB,
            "doubleMinBob.lessThan=" + DEFAULT_DOUBLE_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMinBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMinBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMinBob.greaterThan=" + SMALLER_DOUBLE_MIN_BOB,
            "doubleMinBob.greaterThan=" + DEFAULT_DOUBLE_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMaxBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMaxBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMaxBob.equals=" + DEFAULT_DOUBLE_MAX_BOB,
            "doubleMaxBob.equals=" + UPDATED_DOUBLE_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMaxBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMaxBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMaxBob.in=" + DEFAULT_DOUBLE_MAX_BOB + "," + UPDATED_DOUBLE_MAX_BOB,
            "doubleMaxBob.in=" + UPDATED_DOUBLE_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMaxBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMaxBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("doubleMaxBob.specified=true", "doubleMaxBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMaxBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMaxBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMaxBob.greaterThanOrEqual=" + DEFAULT_DOUBLE_MAX_BOB,
            "doubleMaxBob.greaterThanOrEqual=" + (DEFAULT_DOUBLE_MAX_BOB + 1)
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMaxBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMaxBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMaxBob.lessThanOrEqual=" + DEFAULT_DOUBLE_MAX_BOB,
            "doubleMaxBob.lessThanOrEqual=" + SMALLER_DOUBLE_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMaxBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMaxBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMaxBob.lessThan=" + (DEFAULT_DOUBLE_MAX_BOB + 1),
            "doubleMaxBob.lessThan=" + DEFAULT_DOUBLE_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDoubleMaxBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where doubleMaxBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "doubleMaxBob.greaterThan=" + SMALLER_DOUBLE_MAX_BOB,
            "doubleMaxBob.greaterThan=" + DEFAULT_DOUBLE_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalRequiredBob.equals=" + DEFAULT_BIG_DECIMAL_REQUIRED_BOB,
            "bigDecimalRequiredBob.equals=" + UPDATED_BIG_DECIMAL_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalRequiredBob.in=" + DEFAULT_BIG_DECIMAL_REQUIRED_BOB + "," + UPDATED_BIG_DECIMAL_REQUIRED_BOB,
            "bigDecimalRequiredBob.in=" + UPDATED_BIG_DECIMAL_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalRequiredBob.specified=true",
            "bigDecimalRequiredBob.specified=false"
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalRequiredBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalRequiredBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalRequiredBob.greaterThanOrEqual=" + DEFAULT_BIG_DECIMAL_REQUIRED_BOB,
            "bigDecimalRequiredBob.greaterThanOrEqual=" + UPDATED_BIG_DECIMAL_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalRequiredBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalRequiredBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalRequiredBob.lessThanOrEqual=" + DEFAULT_BIG_DECIMAL_REQUIRED_BOB,
            "bigDecimalRequiredBob.lessThanOrEqual=" + SMALLER_BIG_DECIMAL_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalRequiredBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalRequiredBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalRequiredBob.lessThan=" + UPDATED_BIG_DECIMAL_REQUIRED_BOB,
            "bigDecimalRequiredBob.lessThan=" + DEFAULT_BIG_DECIMAL_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalRequiredBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalRequiredBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalRequiredBob.greaterThan=" + SMALLER_BIG_DECIMAL_REQUIRED_BOB,
            "bigDecimalRequiredBob.greaterThan=" + DEFAULT_BIG_DECIMAL_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMinBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMinBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMinBob.equals=" + DEFAULT_BIG_DECIMAL_MIN_BOB,
            "bigDecimalMinBob.equals=" + UPDATED_BIG_DECIMAL_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMinBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMinBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMinBob.in=" + DEFAULT_BIG_DECIMAL_MIN_BOB + "," + UPDATED_BIG_DECIMAL_MIN_BOB,
            "bigDecimalMinBob.in=" + UPDATED_BIG_DECIMAL_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMinBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMinBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("bigDecimalMinBob.specified=true", "bigDecimalMinBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMinBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMinBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMinBob.greaterThanOrEqual=" + DEFAULT_BIG_DECIMAL_MIN_BOB,
            "bigDecimalMinBob.greaterThanOrEqual=" + UPDATED_BIG_DECIMAL_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMinBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMinBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMinBob.lessThanOrEqual=" + DEFAULT_BIG_DECIMAL_MIN_BOB,
            "bigDecimalMinBob.lessThanOrEqual=" + SMALLER_BIG_DECIMAL_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMinBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMinBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMinBob.lessThan=" + UPDATED_BIG_DECIMAL_MIN_BOB,
            "bigDecimalMinBob.lessThan=" + DEFAULT_BIG_DECIMAL_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMinBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMinBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMinBob.greaterThan=" + SMALLER_BIG_DECIMAL_MIN_BOB,
            "bigDecimalMinBob.greaterThan=" + DEFAULT_BIG_DECIMAL_MIN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMaxBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMaxBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMaxBob.equals=" + DEFAULT_BIG_DECIMAL_MAX_BOB,
            "bigDecimalMaxBob.equals=" + UPDATED_BIG_DECIMAL_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMaxBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMaxBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMaxBob.in=" + DEFAULT_BIG_DECIMAL_MAX_BOB + "," + UPDATED_BIG_DECIMAL_MAX_BOB,
            "bigDecimalMaxBob.in=" + UPDATED_BIG_DECIMAL_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMaxBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMaxBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("bigDecimalMaxBob.specified=true", "bigDecimalMaxBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMaxBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMaxBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMaxBob.greaterThanOrEqual=" + DEFAULT_BIG_DECIMAL_MAX_BOB,
            "bigDecimalMaxBob.greaterThanOrEqual=" + DEFAULT_BIG_DECIMAL_MAX_BOB.add(BigDecimal.ONE)
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMaxBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMaxBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMaxBob.lessThanOrEqual=" + DEFAULT_BIG_DECIMAL_MAX_BOB,
            "bigDecimalMaxBob.lessThanOrEqual=" + SMALLER_BIG_DECIMAL_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMaxBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMaxBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMaxBob.lessThan=" + DEFAULT_BIG_DECIMAL_MAX_BOB.add(BigDecimal.ONE),
            "bigDecimalMaxBob.lessThan=" + DEFAULT_BIG_DECIMAL_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBigDecimalMaxBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where bigDecimalMaxBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "bigDecimalMaxBob.greaterThan=" + SMALLER_BIG_DECIMAL_MAX_BOB,
            "bigDecimalMaxBob.greaterThan=" + DEFAULT_BIG_DECIMAL_MAX_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateBob.equals=" + DEFAULT_LOCAL_DATE_BOB,
            "localDateBob.equals=" + UPDATED_LOCAL_DATE_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateBob.in=" + DEFAULT_LOCAL_DATE_BOB + "," + UPDATED_LOCAL_DATE_BOB,
            "localDateBob.in=" + UPDATED_LOCAL_DATE_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("localDateBob.specified=true", "localDateBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateBob.greaterThanOrEqual=" + DEFAULT_LOCAL_DATE_BOB,
            "localDateBob.greaterThanOrEqual=" + UPDATED_LOCAL_DATE_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateBob.lessThanOrEqual=" + DEFAULT_LOCAL_DATE_BOB,
            "localDateBob.lessThanOrEqual=" + SMALLER_LOCAL_DATE_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateBob.lessThan=" + UPDATED_LOCAL_DATE_BOB,
            "localDateBob.lessThan=" + DEFAULT_LOCAL_DATE_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateBob.greaterThan=" + SMALLER_LOCAL_DATE_BOB,
            "localDateBob.greaterThan=" + DEFAULT_LOCAL_DATE_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateRequiredBob.equals=" + DEFAULT_LOCAL_DATE_REQUIRED_BOB,
            "localDateRequiredBob.equals=" + UPDATED_LOCAL_DATE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateRequiredBob.in=" + DEFAULT_LOCAL_DATE_REQUIRED_BOB + "," + UPDATED_LOCAL_DATE_REQUIRED_BOB,
            "localDateRequiredBob.in=" + UPDATED_LOCAL_DATE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateRequiredBob.specified=true",
            "localDateRequiredBob.specified=false"
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateRequiredBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateRequiredBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateRequiredBob.greaterThanOrEqual=" + DEFAULT_LOCAL_DATE_REQUIRED_BOB,
            "localDateRequiredBob.greaterThanOrEqual=" + UPDATED_LOCAL_DATE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateRequiredBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateRequiredBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateRequiredBob.lessThanOrEqual=" + DEFAULT_LOCAL_DATE_REQUIRED_BOB,
            "localDateRequiredBob.lessThanOrEqual=" + SMALLER_LOCAL_DATE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateRequiredBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateRequiredBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateRequiredBob.lessThan=" + UPDATED_LOCAL_DATE_REQUIRED_BOB,
            "localDateRequiredBob.lessThan=" + DEFAULT_LOCAL_DATE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalDateRequiredBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localDateRequiredBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localDateRequiredBob.greaterThan=" + SMALLER_LOCAL_DATE_REQUIRED_BOB,
            "localDateRequiredBob.greaterThan=" + DEFAULT_LOCAL_DATE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByInstantBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where instantBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "instantBob.equals=" + DEFAULT_INSTANT_BOB,
            "instantBob.equals=" + UPDATED_INSTANT_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByInstantBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where instantBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "instantBob.in=" + DEFAULT_INSTANT_BOB + "," + UPDATED_INSTANT_BOB,
            "instantBob.in=" + UPDATED_INSTANT_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByInstantBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where instantBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("instantBob.specified=true", "instantBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByInstanteRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where instanteRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "instanteRequiredBob.equals=" + DEFAULT_INSTANTE_REQUIRED_BOB,
            "instanteRequiredBob.equals=" + UPDATED_INSTANTE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByInstanteRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where instanteRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "instanteRequiredBob.in=" + DEFAULT_INSTANTE_REQUIRED_BOB + "," + UPDATED_INSTANTE_REQUIRED_BOB,
            "instanteRequiredBob.in=" + UPDATED_INSTANTE_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByInstanteRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where instanteRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "instanteRequiredBob.specified=true",
            "instanteRequiredBob.specified=false"
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeBob.equals=" + DEFAULT_ZONED_DATE_TIME_BOB,
            "zonedDateTimeBob.equals=" + UPDATED_ZONED_DATE_TIME_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeBob.in=" + DEFAULT_ZONED_DATE_TIME_BOB + "," + UPDATED_ZONED_DATE_TIME_BOB,
            "zonedDateTimeBob.in=" + UPDATED_ZONED_DATE_TIME_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("zonedDateTimeBob.specified=true", "zonedDateTimeBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeBob.greaterThanOrEqual=" + DEFAULT_ZONED_DATE_TIME_BOB,
            "zonedDateTimeBob.greaterThanOrEqual=" + UPDATED_ZONED_DATE_TIME_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeBob.lessThanOrEqual=" + DEFAULT_ZONED_DATE_TIME_BOB,
            "zonedDateTimeBob.lessThanOrEqual=" + SMALLER_ZONED_DATE_TIME_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeBob.lessThan=" + UPDATED_ZONED_DATE_TIME_BOB,
            "zonedDateTimeBob.lessThan=" + DEFAULT_ZONED_DATE_TIME_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeBob.greaterThan=" + SMALLER_ZONED_DATE_TIME_BOB,
            "zonedDateTimeBob.greaterThan=" + DEFAULT_ZONED_DATE_TIME_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeRequiredBob.equals=" + DEFAULT_ZONED_DATE_TIME_REQUIRED_BOB,
            "zonedDateTimeRequiredBob.equals=" + UPDATED_ZONED_DATE_TIME_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeRequiredBob.in=" + DEFAULT_ZONED_DATE_TIME_REQUIRED_BOB + "," + UPDATED_ZONED_DATE_TIME_REQUIRED_BOB,
            "zonedDateTimeRequiredBob.in=" + UPDATED_ZONED_DATE_TIME_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeRequiredBob.specified=true",
            "zonedDateTimeRequiredBob.specified=false"
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeRequiredBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeRequiredBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeRequiredBob.greaterThanOrEqual=" + DEFAULT_ZONED_DATE_TIME_REQUIRED_BOB,
            "zonedDateTimeRequiredBob.greaterThanOrEqual=" + UPDATED_ZONED_DATE_TIME_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeRequiredBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeRequiredBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeRequiredBob.lessThanOrEqual=" + DEFAULT_ZONED_DATE_TIME_REQUIRED_BOB,
            "zonedDateTimeRequiredBob.lessThanOrEqual=" + SMALLER_ZONED_DATE_TIME_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeRequiredBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeRequiredBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeRequiredBob.lessThan=" + UPDATED_ZONED_DATE_TIME_REQUIRED_BOB,
            "zonedDateTimeRequiredBob.lessThan=" + DEFAULT_ZONED_DATE_TIME_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByZonedDateTimeRequiredBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where zonedDateTimeRequiredBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "zonedDateTimeRequiredBob.greaterThan=" + SMALLER_ZONED_DATE_TIME_REQUIRED_BOB,
            "zonedDateTimeRequiredBob.greaterThan=" + DEFAULT_ZONED_DATE_TIME_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalTimeBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localTimeBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localTimeBob.equals=" + DEFAULT_LOCAL_TIME_BOB,
            "localTimeBob.equals=" + UPDATED_LOCAL_TIME_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalTimeBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localTimeBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localTimeBob.in=" + DEFAULT_LOCAL_TIME_BOB + "," + UPDATED_LOCAL_TIME_BOB,
            "localTimeBob.in=" + UPDATED_LOCAL_TIME_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalTimeBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localTimeBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("localTimeBob.specified=true", "localTimeBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalTimeRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localTimeRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localTimeRequiredBob.equals=" + DEFAULT_LOCAL_TIME_REQUIRED_BOB,
            "localTimeRequiredBob.equals=" + UPDATED_LOCAL_TIME_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalTimeRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localTimeRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localTimeRequiredBob.in=" + DEFAULT_LOCAL_TIME_REQUIRED_BOB + "," + UPDATED_LOCAL_TIME_REQUIRED_BOB,
            "localTimeRequiredBob.in=" + UPDATED_LOCAL_TIME_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByLocalTimeRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where localTimeRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "localTimeRequiredBob.specified=true",
            "localTimeRequiredBob.specified=false"
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationBob.equals=" + DEFAULT_DURATION_BOB,
            "durationBob.equals=" + UPDATED_DURATION_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationBob.in=" + DEFAULT_DURATION_BOB + "," + UPDATED_DURATION_BOB,
            "durationBob.in=" + UPDATED_DURATION_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("durationBob.specified=true", "durationBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationBob.greaterThanOrEqual=" + DEFAULT_DURATION_BOB,
            "durationBob.greaterThanOrEqual=" + UPDATED_DURATION_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationBob.lessThanOrEqual=" + DEFAULT_DURATION_BOB,
            "durationBob.lessThanOrEqual=" + SMALLER_DURATION_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationBob.lessThan=" + UPDATED_DURATION_BOB,
            "durationBob.lessThan=" + DEFAULT_DURATION_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationBob.greaterThan=" + SMALLER_DURATION_BOB,
            "durationBob.greaterThan=" + DEFAULT_DURATION_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationRequiredBob.equals=" + DEFAULT_DURATION_REQUIRED_BOB,
            "durationRequiredBob.equals=" + UPDATED_DURATION_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationRequiredBob.in=" + DEFAULT_DURATION_REQUIRED_BOB + "," + UPDATED_DURATION_REQUIRED_BOB,
            "durationRequiredBob.in=" + UPDATED_DURATION_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationRequiredBob.specified=true",
            "durationRequiredBob.specified=false"
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationRequiredBobIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationRequiredBob is greater than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationRequiredBob.greaterThanOrEqual=" + DEFAULT_DURATION_REQUIRED_BOB,
            "durationRequiredBob.greaterThanOrEqual=" + UPDATED_DURATION_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationRequiredBobIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationRequiredBob is less than or equal to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationRequiredBob.lessThanOrEqual=" + DEFAULT_DURATION_REQUIRED_BOB,
            "durationRequiredBob.lessThanOrEqual=" + SMALLER_DURATION_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationRequiredBobIsLessThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationRequiredBob is less than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationRequiredBob.lessThan=" + UPDATED_DURATION_REQUIRED_BOB,
            "durationRequiredBob.lessThan=" + DEFAULT_DURATION_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByDurationRequiredBobIsGreaterThanSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where durationRequiredBob is greater than
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "durationRequiredBob.greaterThan=" + SMALLER_DURATION_REQUIRED_BOB,
            "durationRequiredBob.greaterThan=" + DEFAULT_DURATION_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBooleanBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where booleanBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "booleanBob.equals=" + DEFAULT_BOOLEAN_BOB,
            "booleanBob.equals=" + UPDATED_BOOLEAN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBooleanBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where booleanBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "booleanBob.in=" + DEFAULT_BOOLEAN_BOB + "," + UPDATED_BOOLEAN_BOB,
            "booleanBob.in=" + UPDATED_BOOLEAN_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBooleanBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where booleanBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("booleanBob.specified=true", "booleanBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBooleanRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where booleanRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "booleanRequiredBob.equals=" + DEFAULT_BOOLEAN_REQUIRED_BOB,
            "booleanRequiredBob.equals=" + UPDATED_BOOLEAN_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBooleanRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where booleanRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "booleanRequiredBob.in=" + DEFAULT_BOOLEAN_REQUIRED_BOB + "," + UPDATED_BOOLEAN_REQUIRED_BOB,
            "booleanRequiredBob.in=" + UPDATED_BOOLEAN_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByBooleanRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where booleanRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "booleanRequiredBob.specified=true",
            "booleanRequiredBob.specified=false"
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByEnumBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where enumBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "enumBob.equals=" + DEFAULT_ENUM_BOB,
            "enumBob.equals=" + UPDATED_ENUM_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByEnumBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where enumBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "enumBob.in=" + DEFAULT_ENUM_BOB + "," + UPDATED_ENUM_BOB,
            "enumBob.in=" + UPDATED_ENUM_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByEnumBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where enumBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("enumBob.specified=true", "enumBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByEnumRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where enumRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "enumRequiredBob.equals=" + DEFAULT_ENUM_REQUIRED_BOB,
            "enumRequiredBob.equals=" + UPDATED_ENUM_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByEnumRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where enumRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "enumRequiredBob.in=" + DEFAULT_ENUM_REQUIRED_BOB + "," + UPDATED_ENUM_REQUIRED_BOB,
            "enumRequiredBob.in=" + UPDATED_ENUM_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByEnumRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where enumRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("enumRequiredBob.specified=true", "enumRequiredBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByUuidBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where uuidBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "uuidBob.equals=" + DEFAULT_UUID_BOB,
            "uuidBob.equals=" + UPDATED_UUID_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByUuidBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where uuidBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "uuidBob.in=" + DEFAULT_UUID_BOB + "," + UPDATED_UUID_BOB,
            "uuidBob.in=" + UPDATED_UUID_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByUuidBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where uuidBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("uuidBob.specified=true", "uuidBob.specified=false");
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByUuidRequiredBobIsEqualToSomething() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where uuidRequiredBob equals to
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "uuidRequiredBob.equals=" + DEFAULT_UUID_REQUIRED_BOB,
            "uuidRequiredBob.equals=" + UPDATED_UUID_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByUuidRequiredBobIsInShouldWork() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where uuidRequiredBob in
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(
            "uuidRequiredBob.in=" + DEFAULT_UUID_REQUIRED_BOB + "," + UPDATED_UUID_REQUIRED_BOB,
            "uuidRequiredBob.in=" + UPDATED_UUID_REQUIRED_BOB
        );
    }

    @Test
    void getAllFieldTestServiceClassAndJpaFilteringEntitiesByUuidRequiredBobIsNullOrNotNull() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        // Get all the fieldTestServiceClassAndJpaFilteringEntityList where uuidRequiredBob is not null
        defaultFieldTestServiceClassAndJpaFilteringEntityFiltering("uuidRequiredBob.specified=true", "uuidRequiredBob.specified=false");
    }

    private void defaultFieldTestServiceClassAndJpaFilteringEntityFiltering(String shouldBeFound, String shouldNotBeFound) {
        defaultFieldTestServiceClassAndJpaFilteringEntityShouldBeFound(shouldBeFound);
        defaultFieldTestServiceClassAndJpaFilteringEntityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFieldTestServiceClassAndJpaFilteringEntityShouldBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(fieldTestServiceClassAndJpaFilteringEntity.getId().intValue()))
            .jsonPath("$.[*].stringBob")
            .value(hasItem(DEFAULT_STRING_BOB))

            .jsonPath("$.[*].stringRequiredBob")
            .value(hasItem(DEFAULT_STRING_REQUIRED_BOB))

            .jsonPath("$.[*].stringMinlengthBob")
            .value(hasItem(DEFAULT_STRING_MINLENGTH_BOB))

            .jsonPath("$.[*].stringMaxlengthBob")
            .value(hasItem(DEFAULT_STRING_MAXLENGTH_BOB))

            .jsonPath("$.[*].stringPatternBob")
            .value(hasItem(DEFAULT_STRING_PATTERN_BOB))

            .jsonPath("$.[*].integerBob")
            .value(hasItem(DEFAULT_INTEGER_BOB))

            .jsonPath("$.[*].integerRequiredBob")
            .value(hasItem(DEFAULT_INTEGER_REQUIRED_BOB))

            .jsonPath("$.[*].integerMinBob")
            .value(hasItem(DEFAULT_INTEGER_MIN_BOB))

            .jsonPath("$.[*].integerMaxBob")
            .value(hasItem(DEFAULT_INTEGER_MAX_BOB))

            .jsonPath("$.[*].longBob")
            .value(hasItem(DEFAULT_LONG_BOB.intValue()))

            .jsonPath("$.[*].longRequiredBob")
            .value(hasItem(DEFAULT_LONG_REQUIRED_BOB.intValue()))

            .jsonPath("$.[*].longMinBob")
            .value(hasItem(DEFAULT_LONG_MIN_BOB.intValue()))

            .jsonPath("$.[*].longMaxBob")
            .value(hasItem(DEFAULT_LONG_MAX_BOB.intValue()))

            .jsonPath("$.[*].floatBob")
            .value(hasItem(DEFAULT_FLOAT_BOB.doubleValue()))

            .jsonPath("$.[*].floatRequiredBob")
            .value(hasItem(DEFAULT_FLOAT_REQUIRED_BOB.doubleValue()))

            .jsonPath("$.[*].floatMinBob")
            .value(hasItem(DEFAULT_FLOAT_MIN_BOB.doubleValue()))

            .jsonPath("$.[*].floatMaxBob")
            .value(hasItem(DEFAULT_FLOAT_MAX_BOB.doubleValue()))

            .jsonPath("$.[*].doubleRequiredBob")
            .value(hasItem(DEFAULT_DOUBLE_REQUIRED_BOB))

            .jsonPath("$.[*].doubleMinBob")
            .value(hasItem(DEFAULT_DOUBLE_MIN_BOB))

            .jsonPath("$.[*].doubleMaxBob")
            .value(hasItem(DEFAULT_DOUBLE_MAX_BOB))

            .jsonPath("$.[*].bigDecimalRequiredBob")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_REQUIRED_BOB)))

            .jsonPath("$.[*].bigDecimalMinBob")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_MIN_BOB)))

            .jsonPath("$.[*].bigDecimalMaxBob")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_MAX_BOB)))

            .jsonPath("$.[*].localDateBob")
            .value(hasItem(DEFAULT_LOCAL_DATE_BOB.toString()))

            .jsonPath("$.[*].localDateRequiredBob")
            .value(hasItem(DEFAULT_LOCAL_DATE_REQUIRED_BOB.toString()))

            .jsonPath("$.[*].instantBob")
            .value(hasItem(DEFAULT_INSTANT_BOB.toString()))

            .jsonPath("$.[*].instanteRequiredBob")
            .value(hasItem(DEFAULT_INSTANTE_REQUIRED_BOB.toString()))

            .jsonPath("$.[*].zonedDateTimeBob")
            .value(hasItem(sameInstant(DEFAULT_ZONED_DATE_TIME_BOB)))

            .jsonPath("$.[*].zonedDateTimeRequiredBob")
            .value(hasItem(sameInstant(DEFAULT_ZONED_DATE_TIME_REQUIRED_BOB)))

            .jsonPath("$.[*].localTimeBob")
            .value(hasItem(DEFAULT_LOCAL_TIME_BOB.format(LOCAL_DATE_TIME_FORMAT)))

            .jsonPath("$.[*].localTimeRequiredBob")
            .value(hasItem(DEFAULT_LOCAL_TIME_REQUIRED_BOB.format(LOCAL_DATE_TIME_FORMAT)))

            .jsonPath("$.[*].durationBob")
            .value(hasItem(DEFAULT_DURATION_BOB.toString()))

            .jsonPath("$.[*].durationRequiredBob")
            .value(hasItem(DEFAULT_DURATION_REQUIRED_BOB.toString()))

            .jsonPath("$.[*].booleanBob")
            .value(hasItem(DEFAULT_BOOLEAN_BOB))

            .jsonPath("$.[*].booleanRequiredBob")
            .value(hasItem(DEFAULT_BOOLEAN_REQUIRED_BOB))

            .jsonPath("$.[*].enumBob")
            .value(hasItem(DEFAULT_ENUM_BOB.toString()))

            .jsonPath("$.[*].enumRequiredBob")
            .value(hasItem(DEFAULT_ENUM_REQUIRED_BOB.toString()))

            .jsonPath("$.[*].uuidBob")
            .value(hasItem(DEFAULT_UUID_BOB.toString()))

            .jsonPath("$.[*].uuidRequiredBob")
            .value(hasItem(DEFAULT_UUID_REQUIRED_BOB.toString()))

            .jsonPath("$.[*].byteImageBobContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_BOB)))

            .jsonPath("$.[*].byteImageRequiredBobContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_REQUIRED_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageRequiredBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_REQUIRED_BOB)))

            .jsonPath("$.[*].byteImageMinbytesBobContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_MINBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageMinbytesBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MINBYTES_BOB)))

            .jsonPath("$.[*].byteImageMaxbytesBobContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_MAXBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageMaxbytesBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MAXBYTES_BOB)))

            .jsonPath("$.[*].byteAnyBobContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_BOB)))

            .jsonPath("$.[*].byteAnyRequiredBobContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_REQUIRED_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyRequiredBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_REQUIRED_BOB)))

            .jsonPath("$.[*].byteAnyMinbytesBobContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_MINBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyMinbytesBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MINBYTES_BOB)))

            .jsonPath("$.[*].byteAnyMaxbytesBobContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_MAXBYTES_BOB_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyMaxbytesBob")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MAXBYTES_BOB)))

            .jsonPath("$.[*].byteTextBob")
            .value(hasItem(DEFAULT_BYTE_TEXT_BOB))

            .jsonPath("$.[*].byteTextRequiredBob")
            .value(hasItem(DEFAULT_BYTE_TEXT_REQUIRED_BOB));

        // Check, that the count call also returns 1
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(1));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFieldTestServiceClassAndJpaFilteringEntityShouldNotBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .isArray()
            .jsonPath("$")
            .isEmpty();

        // Check, that the count call also returns 0
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(0));
    }

    @Test
    void getNonExistingFieldTestServiceClassAndJpaFilteringEntity() {
        // Get the fieldTestServiceClassAndJpaFilteringEntity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFieldTestServiceClassAndJpaFilteringEntity() throws Exception {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestServiceClassAndJpaFilteringEntity
        FieldTestServiceClassAndJpaFilteringEntity updatedFieldTestServiceClassAndJpaFilteringEntity =
            fieldTestServiceClassAndJpaFilteringEntityRepository.findById(fieldTestServiceClassAndJpaFilteringEntity.getId()).block();
        updatedFieldTestServiceClassAndJpaFilteringEntity
            .stringBob(UPDATED_STRING_BOB)
            .stringRequiredBob(UPDATED_STRING_REQUIRED_BOB)
            .stringMinlengthBob(UPDATED_STRING_MINLENGTH_BOB)
            .stringMaxlengthBob(UPDATED_STRING_MAXLENGTH_BOB)
            .stringPatternBob(UPDATED_STRING_PATTERN_BOB)
            .integerBob(UPDATED_INTEGER_BOB)
            .integerRequiredBob(UPDATED_INTEGER_REQUIRED_BOB)
            .integerMinBob(UPDATED_INTEGER_MIN_BOB)
            .integerMaxBob(UPDATED_INTEGER_MAX_BOB)
            .longBob(UPDATED_LONG_BOB)
            .longRequiredBob(UPDATED_LONG_REQUIRED_BOB)
            .longMinBob(UPDATED_LONG_MIN_BOB)
            .longMaxBob(UPDATED_LONG_MAX_BOB)
            .floatBob(UPDATED_FLOAT_BOB)
            .floatRequiredBob(UPDATED_FLOAT_REQUIRED_BOB)
            .floatMinBob(UPDATED_FLOAT_MIN_BOB)
            .floatMaxBob(UPDATED_FLOAT_MAX_BOB)
            .doubleRequiredBob(UPDATED_DOUBLE_REQUIRED_BOB)
            .doubleMinBob(UPDATED_DOUBLE_MIN_BOB)
            .doubleMaxBob(UPDATED_DOUBLE_MAX_BOB)
            .bigDecimalRequiredBob(UPDATED_BIG_DECIMAL_REQUIRED_BOB)
            .bigDecimalMinBob(UPDATED_BIG_DECIMAL_MIN_BOB)
            .bigDecimalMaxBob(UPDATED_BIG_DECIMAL_MAX_BOB)
            .localDateBob(UPDATED_LOCAL_DATE_BOB)
            .localDateRequiredBob(UPDATED_LOCAL_DATE_REQUIRED_BOB)
            .instantBob(UPDATED_INSTANT_BOB)
            .instanteRequiredBob(UPDATED_INSTANTE_REQUIRED_BOB)
            .zonedDateTimeBob(UPDATED_ZONED_DATE_TIME_BOB)
            .zonedDateTimeRequiredBob(UPDATED_ZONED_DATE_TIME_REQUIRED_BOB)
            .localTimeBob(UPDATED_LOCAL_TIME_BOB)
            .localTimeRequiredBob(UPDATED_LOCAL_TIME_REQUIRED_BOB)
            .durationBob(UPDATED_DURATION_BOB)
            .durationRequiredBob(UPDATED_DURATION_REQUIRED_BOB)
            .booleanBob(UPDATED_BOOLEAN_BOB)
            .booleanRequiredBob(UPDATED_BOOLEAN_REQUIRED_BOB)
            .enumBob(UPDATED_ENUM_BOB)
            .enumRequiredBob(UPDATED_ENUM_REQUIRED_BOB)
            .uuidBob(UPDATED_UUID_BOB)
            .uuidRequiredBob(UPDATED_UUID_REQUIRED_BOB)
            .byteImageBob(UPDATED_BYTE_IMAGE_BOB)
            .byteImageBobContentType(UPDATED_BYTE_IMAGE_BOB_CONTENT_TYPE)
            .byteImageRequiredBob(UPDATED_BYTE_IMAGE_REQUIRED_BOB)
            .byteImageRequiredBobContentType(UPDATED_BYTE_IMAGE_REQUIRED_BOB_CONTENT_TYPE)
            .byteImageMinbytesBob(UPDATED_BYTE_IMAGE_MINBYTES_BOB)
            .byteImageMinbytesBobContentType(UPDATED_BYTE_IMAGE_MINBYTES_BOB_CONTENT_TYPE)
            .byteImageMaxbytesBob(UPDATED_BYTE_IMAGE_MAXBYTES_BOB)
            .byteImageMaxbytesBobContentType(UPDATED_BYTE_IMAGE_MAXBYTES_BOB_CONTENT_TYPE)
            .byteAnyBob(UPDATED_BYTE_ANY_BOB)
            .byteAnyBobContentType(UPDATED_BYTE_ANY_BOB_CONTENT_TYPE)
            .byteAnyRequiredBob(UPDATED_BYTE_ANY_REQUIRED_BOB)
            .byteAnyRequiredBobContentType(UPDATED_BYTE_ANY_REQUIRED_BOB_CONTENT_TYPE)
            .byteAnyMinbytesBob(UPDATED_BYTE_ANY_MINBYTES_BOB)
            .byteAnyMinbytesBobContentType(UPDATED_BYTE_ANY_MINBYTES_BOB_CONTENT_TYPE)
            .byteAnyMaxbytesBob(UPDATED_BYTE_ANY_MAXBYTES_BOB)
            .byteAnyMaxbytesBobContentType(UPDATED_BYTE_ANY_MAXBYTES_BOB_CONTENT_TYPE)
            .byteTextBob(UPDATED_BYTE_TEXT_BOB)
            .byteTextRequiredBob(UPDATED_BYTE_TEXT_REQUIRED_BOB);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFieldTestServiceClassAndJpaFilteringEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedFieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestServiceClassAndJpaFilteringEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFieldTestServiceClassAndJpaFilteringEntityToMatchAllProperties(updatedFieldTestServiceClassAndJpaFilteringEntity);
    }

    @Test
    void putNonExistingFieldTestServiceClassAndJpaFilteringEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestServiceClassAndJpaFilteringEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fieldTestServiceClassAndJpaFilteringEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestServiceClassAndJpaFilteringEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFieldTestServiceClassAndJpaFilteringEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestServiceClassAndJpaFilteringEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestServiceClassAndJpaFilteringEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFieldTestServiceClassAndJpaFilteringEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestServiceClassAndJpaFilteringEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FieldTestServiceClassAndJpaFilteringEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFieldTestServiceClassAndJpaFilteringEntityWithPatch() throws Exception {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestServiceClassAndJpaFilteringEntity using partial update
        FieldTestServiceClassAndJpaFilteringEntity partialUpdatedFieldTestServiceClassAndJpaFilteringEntity =
            new FieldTestServiceClassAndJpaFilteringEntity();
        partialUpdatedFieldTestServiceClassAndJpaFilteringEntity.setId(fieldTestServiceClassAndJpaFilteringEntity.getId());

        partialUpdatedFieldTestServiceClassAndJpaFilteringEntity
            .stringRequiredBob(UPDATED_STRING_REQUIRED_BOB)
            .stringMinlengthBob(UPDATED_STRING_MINLENGTH_BOB)
            .stringMaxlengthBob(UPDATED_STRING_MAXLENGTH_BOB)
            .integerRequiredBob(UPDATED_INTEGER_REQUIRED_BOB)
            .integerMinBob(UPDATED_INTEGER_MIN_BOB)
            .integerMaxBob(UPDATED_INTEGER_MAX_BOB)
            .longBob(UPDATED_LONG_BOB)
            .longMaxBob(UPDATED_LONG_MAX_BOB)
            .floatMinBob(UPDATED_FLOAT_MIN_BOB)
            .doubleRequiredBob(UPDATED_DOUBLE_REQUIRED_BOB)
            .localDateRequiredBob(UPDATED_LOCAL_DATE_REQUIRED_BOB)
            .zonedDateTimeBob(UPDATED_ZONED_DATE_TIME_BOB)
            .localTimeRequiredBob(UPDATED_LOCAL_TIME_REQUIRED_BOB)
            .durationRequiredBob(UPDATED_DURATION_REQUIRED_BOB)
            .enumBob(UPDATED_ENUM_BOB)
            .enumRequiredBob(UPDATED_ENUM_REQUIRED_BOB)
            .uuidBob(UPDATED_UUID_BOB)
            .uuidRequiredBob(UPDATED_UUID_REQUIRED_BOB)
            .byteImageBob(UPDATED_BYTE_IMAGE_BOB)
            .byteImageBobContentType(UPDATED_BYTE_IMAGE_BOB_CONTENT_TYPE)
            .byteImageMinbytesBob(UPDATED_BYTE_IMAGE_MINBYTES_BOB)
            .byteImageMinbytesBobContentType(UPDATED_BYTE_IMAGE_MINBYTES_BOB_CONTENT_TYPE)
            .byteImageMaxbytesBob(UPDATED_BYTE_IMAGE_MAXBYTES_BOB)
            .byteImageMaxbytesBobContentType(UPDATED_BYTE_IMAGE_MAXBYTES_BOB_CONTENT_TYPE)
            .byteAnyRequiredBob(UPDATED_BYTE_ANY_REQUIRED_BOB)
            .byteAnyRequiredBobContentType(UPDATED_BYTE_ANY_REQUIRED_BOB_CONTENT_TYPE)
            .byteAnyMaxbytesBob(UPDATED_BYTE_ANY_MAXBYTES_BOB)
            .byteAnyMaxbytesBobContentType(UPDATED_BYTE_ANY_MAXBYTES_BOB_CONTENT_TYPE)
            .byteTextRequiredBob(UPDATED_BYTE_TEXT_REQUIRED_BOB);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFieldTestServiceClassAndJpaFilteringEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestServiceClassAndJpaFilteringEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFieldTestServiceClassAndJpaFilteringEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFieldTestServiceClassAndJpaFilteringEntity, fieldTestServiceClassAndJpaFilteringEntity),
            getPersistedFieldTestServiceClassAndJpaFilteringEntity(fieldTestServiceClassAndJpaFilteringEntity)
        );
    }

    @Test
    void fullUpdateFieldTestServiceClassAndJpaFilteringEntityWithPatch() throws Exception {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestServiceClassAndJpaFilteringEntity using partial update
        FieldTestServiceClassAndJpaFilteringEntity partialUpdatedFieldTestServiceClassAndJpaFilteringEntity =
            new FieldTestServiceClassAndJpaFilteringEntity();
        partialUpdatedFieldTestServiceClassAndJpaFilteringEntity.setId(fieldTestServiceClassAndJpaFilteringEntity.getId());

        partialUpdatedFieldTestServiceClassAndJpaFilteringEntity
            .stringBob(UPDATED_STRING_BOB)
            .stringRequiredBob(UPDATED_STRING_REQUIRED_BOB)
            .stringMinlengthBob(UPDATED_STRING_MINLENGTH_BOB)
            .stringMaxlengthBob(UPDATED_STRING_MAXLENGTH_BOB)
            .stringPatternBob(UPDATED_STRING_PATTERN_BOB)
            .integerBob(UPDATED_INTEGER_BOB)
            .integerRequiredBob(UPDATED_INTEGER_REQUIRED_BOB)
            .integerMinBob(UPDATED_INTEGER_MIN_BOB)
            .integerMaxBob(UPDATED_INTEGER_MAX_BOB)
            .longBob(UPDATED_LONG_BOB)
            .longRequiredBob(UPDATED_LONG_REQUIRED_BOB)
            .longMinBob(UPDATED_LONG_MIN_BOB)
            .longMaxBob(UPDATED_LONG_MAX_BOB)
            .floatBob(UPDATED_FLOAT_BOB)
            .floatRequiredBob(UPDATED_FLOAT_REQUIRED_BOB)
            .floatMinBob(UPDATED_FLOAT_MIN_BOB)
            .floatMaxBob(UPDATED_FLOAT_MAX_BOB)
            .doubleRequiredBob(UPDATED_DOUBLE_REQUIRED_BOB)
            .doubleMinBob(UPDATED_DOUBLE_MIN_BOB)
            .doubleMaxBob(UPDATED_DOUBLE_MAX_BOB)
            .bigDecimalRequiredBob(UPDATED_BIG_DECIMAL_REQUIRED_BOB)
            .bigDecimalMinBob(UPDATED_BIG_DECIMAL_MIN_BOB)
            .bigDecimalMaxBob(UPDATED_BIG_DECIMAL_MAX_BOB)
            .localDateBob(UPDATED_LOCAL_DATE_BOB)
            .localDateRequiredBob(UPDATED_LOCAL_DATE_REQUIRED_BOB)
            .instantBob(UPDATED_INSTANT_BOB)
            .instanteRequiredBob(UPDATED_INSTANTE_REQUIRED_BOB)
            .zonedDateTimeBob(UPDATED_ZONED_DATE_TIME_BOB)
            .zonedDateTimeRequiredBob(UPDATED_ZONED_DATE_TIME_REQUIRED_BOB)
            .localTimeBob(UPDATED_LOCAL_TIME_BOB)
            .localTimeRequiredBob(UPDATED_LOCAL_TIME_REQUIRED_BOB)
            .durationBob(UPDATED_DURATION_BOB)
            .durationRequiredBob(UPDATED_DURATION_REQUIRED_BOB)
            .booleanBob(UPDATED_BOOLEAN_BOB)
            .booleanRequiredBob(UPDATED_BOOLEAN_REQUIRED_BOB)
            .enumBob(UPDATED_ENUM_BOB)
            .enumRequiredBob(UPDATED_ENUM_REQUIRED_BOB)
            .uuidBob(UPDATED_UUID_BOB)
            .uuidRequiredBob(UPDATED_UUID_REQUIRED_BOB)
            .byteImageBob(UPDATED_BYTE_IMAGE_BOB)
            .byteImageBobContentType(UPDATED_BYTE_IMAGE_BOB_CONTENT_TYPE)
            .byteImageRequiredBob(UPDATED_BYTE_IMAGE_REQUIRED_BOB)
            .byteImageRequiredBobContentType(UPDATED_BYTE_IMAGE_REQUIRED_BOB_CONTENT_TYPE)
            .byteImageMinbytesBob(UPDATED_BYTE_IMAGE_MINBYTES_BOB)
            .byteImageMinbytesBobContentType(UPDATED_BYTE_IMAGE_MINBYTES_BOB_CONTENT_TYPE)
            .byteImageMaxbytesBob(UPDATED_BYTE_IMAGE_MAXBYTES_BOB)
            .byteImageMaxbytesBobContentType(UPDATED_BYTE_IMAGE_MAXBYTES_BOB_CONTENT_TYPE)
            .byteAnyBob(UPDATED_BYTE_ANY_BOB)
            .byteAnyBobContentType(UPDATED_BYTE_ANY_BOB_CONTENT_TYPE)
            .byteAnyRequiredBob(UPDATED_BYTE_ANY_REQUIRED_BOB)
            .byteAnyRequiredBobContentType(UPDATED_BYTE_ANY_REQUIRED_BOB_CONTENT_TYPE)
            .byteAnyMinbytesBob(UPDATED_BYTE_ANY_MINBYTES_BOB)
            .byteAnyMinbytesBobContentType(UPDATED_BYTE_ANY_MINBYTES_BOB_CONTENT_TYPE)
            .byteAnyMaxbytesBob(UPDATED_BYTE_ANY_MAXBYTES_BOB)
            .byteAnyMaxbytesBobContentType(UPDATED_BYTE_ANY_MAXBYTES_BOB_CONTENT_TYPE)
            .byteTextBob(UPDATED_BYTE_TEXT_BOB)
            .byteTextRequiredBob(UPDATED_BYTE_TEXT_REQUIRED_BOB);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFieldTestServiceClassAndJpaFilteringEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestServiceClassAndJpaFilteringEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFieldTestServiceClassAndJpaFilteringEntityUpdatableFieldsEquals(
            partialUpdatedFieldTestServiceClassAndJpaFilteringEntity,
            getPersistedFieldTestServiceClassAndJpaFilteringEntity(partialUpdatedFieldTestServiceClassAndJpaFilteringEntity)
        );
    }

    @Test
    void patchNonExistingFieldTestServiceClassAndJpaFilteringEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestServiceClassAndJpaFilteringEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, fieldTestServiceClassAndJpaFilteringEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestServiceClassAndJpaFilteringEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFieldTestServiceClassAndJpaFilteringEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestServiceClassAndJpaFilteringEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestServiceClassAndJpaFilteringEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFieldTestServiceClassAndJpaFilteringEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestServiceClassAndJpaFilteringEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestServiceClassAndJpaFilteringEntity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FieldTestServiceClassAndJpaFilteringEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFieldTestServiceClassAndJpaFilteringEntity() {
        // Initialize the database
        insertedFieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntityRepository
            .save(fieldTestServiceClassAndJpaFilteringEntity)
            .block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fieldTestServiceClassAndJpaFilteringEntity
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, fieldTestServiceClassAndJpaFilteringEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fieldTestServiceClassAndJpaFilteringEntityRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected FieldTestServiceClassAndJpaFilteringEntity getPersistedFieldTestServiceClassAndJpaFilteringEntity(
        FieldTestServiceClassAndJpaFilteringEntity fieldTestServiceClassAndJpaFilteringEntity
    ) {
        return fieldTestServiceClassAndJpaFilteringEntityRepository.findById(fieldTestServiceClassAndJpaFilteringEntity.getId()).block();
    }

    protected void assertPersistedFieldTestServiceClassAndJpaFilteringEntityToMatchAllProperties(
        FieldTestServiceClassAndJpaFilteringEntity expectedFieldTestServiceClassAndJpaFilteringEntity
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFieldTestServiceClassAndJpaFilteringEntityAllPropertiesEquals(expectedFieldTestServiceClassAndJpaFilteringEntity, getPersistedFieldTestServiceClassAndJpaFilteringEntity(expectedFieldTestServiceClassAndJpaFilteringEntity));
        assertFieldTestServiceClassAndJpaFilteringEntityUpdatableFieldsEquals(
            expectedFieldTestServiceClassAndJpaFilteringEntity,
            getPersistedFieldTestServiceClassAndJpaFilteringEntity(expectedFieldTestServiceClassAndJpaFilteringEntity)
        );
    }

    protected void assertPersistedFieldTestServiceClassAndJpaFilteringEntityToMatchUpdatableProperties(
        FieldTestServiceClassAndJpaFilteringEntity expectedFieldTestServiceClassAndJpaFilteringEntity
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFieldTestServiceClassAndJpaFilteringEntityAllUpdatablePropertiesEquals(expectedFieldTestServiceClassAndJpaFilteringEntity, getPersistedFieldTestServiceClassAndJpaFilteringEntity(expectedFieldTestServiceClassAndJpaFilteringEntity));
        assertFieldTestServiceClassAndJpaFilteringEntityUpdatableFieldsEquals(
            expectedFieldTestServiceClassAndJpaFilteringEntity,
            getPersistedFieldTestServiceClassAndJpaFilteringEntity(expectedFieldTestServiceClassAndJpaFilteringEntity)
        );
    }
}
