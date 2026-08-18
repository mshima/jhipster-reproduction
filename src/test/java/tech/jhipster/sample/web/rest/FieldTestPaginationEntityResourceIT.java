package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static tech.jhipster.sample.domain.FieldTestPaginationEntityAsserts.*;
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
import tech.jhipster.sample.domain.FieldTestPaginationEntity;
import tech.jhipster.sample.domain.enumeration.EnumFieldClass;
import tech.jhipster.sample.domain.enumeration.EnumRequiredFieldClass;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.FieldTestPaginationEntityRepository;

/**
 * Integration tests for the {@link FieldTestPaginationEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FieldTestPaginationEntityResourceIT {

    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final String DEFAULT_STRING_ALICE = "AAAAAAAAAA";
    private static final String UPDATED_STRING_ALICE = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_REQUIRED_ALICE = "AAAAAAAAAA";
    private static final String UPDATED_STRING_REQUIRED_ALICE = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_MINLENGTH_ALICE = "AAAAAAAAAA";
    private static final String UPDATED_STRING_MINLENGTH_ALICE = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_MAXLENGTH_ALICE = "AAAAAAAAAA";
    private static final String UPDATED_STRING_MAXLENGTH_ALICE = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_PATTERN_ALICE = "AAAAAAAAAA";
    private static final String UPDATED_STRING_PATTERN_ALICE = "BBBBBBBBBB";

    private static final Integer DEFAULT_INTEGER_ALICE = 1;
    private static final Integer UPDATED_INTEGER_ALICE = 2;

    private static final Integer DEFAULT_INTEGER_REQUIRED_ALICE = 1;
    private static final Integer UPDATED_INTEGER_REQUIRED_ALICE = 2;

    private static final Integer DEFAULT_INTEGER_MIN_ALICE = 0;
    private static final Integer UPDATED_INTEGER_MIN_ALICE = 1;

    private static final Integer DEFAULT_INTEGER_MAX_ALICE = 100;
    private static final Integer UPDATED_INTEGER_MAX_ALICE = 99;

    private static final Long DEFAULT_LONG_ALICE = 1L;
    private static final Long UPDATED_LONG_ALICE = 2L;

    private static final Long DEFAULT_LONG_REQUIRED_ALICE = 1L;
    private static final Long UPDATED_LONG_REQUIRED_ALICE = 2L;

    private static final Long DEFAULT_LONG_MIN_ALICE = 0L;
    private static final Long UPDATED_LONG_MIN_ALICE = 1L;

    private static final Long DEFAULT_LONG_MAX_ALICE = 100L;
    private static final Long UPDATED_LONG_MAX_ALICE = 99L;

    private static final Float DEFAULT_FLOAT_ALICE = 1F;
    private static final Float UPDATED_FLOAT_ALICE = 2F;

    private static final Float DEFAULT_FLOAT_REQUIRED_ALICE = 1F;
    private static final Float UPDATED_FLOAT_REQUIRED_ALICE = 2F;

    private static final Float DEFAULT_FLOAT_MIN_ALICE = 0F;
    private static final Float UPDATED_FLOAT_MIN_ALICE = 1F;

    private static final Float DEFAULT_FLOAT_MAX_ALICE = 100F;
    private static final Float UPDATED_FLOAT_MAX_ALICE = 99F;

    private static final Double DEFAULT_DOUBLE_REQUIRED_ALICE = 1D;
    private static final Double UPDATED_DOUBLE_REQUIRED_ALICE = 2D;

    private static final Double DEFAULT_DOUBLE_MIN_ALICE = 0D;
    private static final Double UPDATED_DOUBLE_MIN_ALICE = 1D;

    private static final Double DEFAULT_DOUBLE_MAX_ALICE = 100D;
    private static final Double UPDATED_DOUBLE_MAX_ALICE = 99D;

    private static final BigDecimal DEFAULT_BIG_DECIMAL_REQUIRED_ALICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BIG_DECIMAL_REQUIRED_ALICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BIG_DECIMAL_MIN_ALICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_BIG_DECIMAL_MIN_ALICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_BIG_DECIMAL_MAX_ALICE = new BigDecimal(100);
    private static final BigDecimal UPDATED_BIG_DECIMAL_MAX_ALICE = new BigDecimal(99);

    private static final LocalDate DEFAULT_LOCAL_DATE_ALICE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOCAL_DATE_ALICE = LocalDate.parse("2020-08-04");

    private static final LocalDate DEFAULT_LOCAL_DATE_REQUIRED_ALICE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOCAL_DATE_REQUIRED_ALICE = LocalDate.parse("2020-08-04");

    private static final Instant DEFAULT_INSTANT_ALICE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSTANT_ALICE = Instant.ofEpochMilli(1596513172471L);

    private static final Instant DEFAULT_INSTANTE_REQUIRED_ALICE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSTANTE_REQUIRED_ALICE = Instant.ofEpochMilli(1596513172471L);

    private static final ZonedDateTime DEFAULT_ZONED_DATE_TIME_ALICE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ZONED_DATE_TIME_ALICE = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(1596513172471L),
        ZoneOffset.UTC
    );

    private static final ZonedDateTime DEFAULT_ZONED_DATE_TIME_REQUIRED_ALICE = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_ZONED_DATE_TIME_REQUIRED_ALICE = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(1596513172471L),
        ZoneOffset.UTC
    );

    private static final LocalTime DEFAULT_LOCAL_TIME_ALICE = LocalTime.NOON;
    private static final LocalTime UPDATED_LOCAL_TIME_ALICE = LocalTime.MAX.withNano(0);

    private static final LocalTime DEFAULT_LOCAL_TIME_REQUIRED_ALICE = LocalTime.NOON;
    private static final LocalTime UPDATED_LOCAL_TIME_REQUIRED_ALICE = LocalTime.MAX.withNano(0);

    private static final Duration DEFAULT_DURATION_ALICE = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION_ALICE = Duration.ofHours(12);

    private static final Duration DEFAULT_DURATION_REQUIRED_ALICE = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION_REQUIRED_ALICE = Duration.ofHours(12);

    private static final Boolean DEFAULT_BOOLEAN_ALICE = false;
    private static final Boolean UPDATED_BOOLEAN_ALICE = true;

    private static final Boolean DEFAULT_BOOLEAN_REQUIRED_ALICE = false;
    private static final Boolean UPDATED_BOOLEAN_REQUIRED_ALICE = true;

    private static final EnumFieldClass DEFAULT_ENUM_ALICE = EnumFieldClass.ENUM_VALUE_1;
    private static final EnumFieldClass UPDATED_ENUM_ALICE = EnumFieldClass.ENUM_VALUE_2;

    private static final EnumRequiredFieldClass DEFAULT_ENUM_REQUIRED_ALICE = EnumRequiredFieldClass.ENUM_VALUE_1;
    private static final EnumRequiredFieldClass UPDATED_ENUM_REQUIRED_ALICE = EnumRequiredFieldClass.ENUM_VALUE_2;

    private static final UUID DEFAULT_UUID_ALICE = UUID.randomUUID();
    private static final UUID UPDATED_UUID_ALICE = UUID.randomUUID();

    private static final UUID DEFAULT_UUID_REQUIRED_ALICE = UUID.randomUUID();
    private static final UUID UPDATED_UUID_REQUIRED_ALICE = UUID.randomUUID();

    private static final byte[] DEFAULT_BYTE_IMAGE_ALICE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_ALICE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_ALICE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_ALICE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_IMAGE_REQUIRED_ALICE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_REQUIRED_ALICE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_REQUIRED_ALICE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_REQUIRED_ALICE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_IMAGE_MINBYTES_ALICE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_MINBYTES_ALICE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_MINBYTES_ALICE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_MINBYTES_ALICE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_IMAGE_MAXBYTES_ALICE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_MAXBYTES_ALICE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_MAXBYTES_ALICE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_MAXBYTES_ALICE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_ALICE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_ALICE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_ALICE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_ALICE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_REQUIRED_ALICE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_REQUIRED_ALICE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_REQUIRED_ALICE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_REQUIRED_ALICE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_MINBYTES_ALICE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_MINBYTES_ALICE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_MINBYTES_ALICE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_MINBYTES_ALICE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_MAXBYTES_ALICE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_MAXBYTES_ALICE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_MAXBYTES_ALICE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_MAXBYTES_ALICE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_BYTE_TEXT_ALICE = "AAAAAAAAAA";
    private static final String UPDATED_BYTE_TEXT_ALICE = "BBBBBBBBBB";

    private static final String DEFAULT_BYTE_TEXT_REQUIRED_ALICE = "AAAAAAAAAA";
    private static final String UPDATED_BYTE_TEXT_REQUIRED_ALICE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/field-test-pagination-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FieldTestPaginationEntityRepository fieldTestPaginationEntityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FieldTestPaginationEntity fieldTestPaginationEntity;

    private FieldTestPaginationEntity insertedFieldTestPaginationEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldTestPaginationEntity createEntity() {
        return new FieldTestPaginationEntity()
            .stringAlice(DEFAULT_STRING_ALICE)
            .stringRequiredAlice(DEFAULT_STRING_REQUIRED_ALICE)
            .stringMinlengthAlice(DEFAULT_STRING_MINLENGTH_ALICE)
            .stringMaxlengthAlice(DEFAULT_STRING_MAXLENGTH_ALICE)
            .stringPatternAlice(DEFAULT_STRING_PATTERN_ALICE)
            .integerAlice(DEFAULT_INTEGER_ALICE)
            .integerRequiredAlice(DEFAULT_INTEGER_REQUIRED_ALICE)
            .integerMinAlice(DEFAULT_INTEGER_MIN_ALICE)
            .integerMaxAlice(DEFAULT_INTEGER_MAX_ALICE)
            .longAlice(DEFAULT_LONG_ALICE)
            .longRequiredAlice(DEFAULT_LONG_REQUIRED_ALICE)
            .longMinAlice(DEFAULT_LONG_MIN_ALICE)
            .longMaxAlice(DEFAULT_LONG_MAX_ALICE)
            .floatAlice(DEFAULT_FLOAT_ALICE)
            .floatRequiredAlice(DEFAULT_FLOAT_REQUIRED_ALICE)
            .floatMinAlice(DEFAULT_FLOAT_MIN_ALICE)
            .floatMaxAlice(DEFAULT_FLOAT_MAX_ALICE)
            .doubleRequiredAlice(DEFAULT_DOUBLE_REQUIRED_ALICE)
            .doubleMinAlice(DEFAULT_DOUBLE_MIN_ALICE)
            .doubleMaxAlice(DEFAULT_DOUBLE_MAX_ALICE)
            .bigDecimalRequiredAlice(DEFAULT_BIG_DECIMAL_REQUIRED_ALICE)
            .bigDecimalMinAlice(DEFAULT_BIG_DECIMAL_MIN_ALICE)
            .bigDecimalMaxAlice(DEFAULT_BIG_DECIMAL_MAX_ALICE)
            .localDateAlice(DEFAULT_LOCAL_DATE_ALICE)
            .localDateRequiredAlice(DEFAULT_LOCAL_DATE_REQUIRED_ALICE)
            .instantAlice(DEFAULT_INSTANT_ALICE)
            .instanteRequiredAlice(DEFAULT_INSTANTE_REQUIRED_ALICE)
            .zonedDateTimeAlice(DEFAULT_ZONED_DATE_TIME_ALICE)
            .zonedDateTimeRequiredAlice(DEFAULT_ZONED_DATE_TIME_REQUIRED_ALICE)
            .localTimeAlice(DEFAULT_LOCAL_TIME_ALICE)
            .localTimeRequiredAlice(DEFAULT_LOCAL_TIME_REQUIRED_ALICE)
            .durationAlice(DEFAULT_DURATION_ALICE)
            .durationRequiredAlice(DEFAULT_DURATION_REQUIRED_ALICE)
            .booleanAlice(DEFAULT_BOOLEAN_ALICE)
            .booleanRequiredAlice(DEFAULT_BOOLEAN_REQUIRED_ALICE)
            .enumAlice(DEFAULT_ENUM_ALICE)
            .enumRequiredAlice(DEFAULT_ENUM_REQUIRED_ALICE)
            .uuidAlice(DEFAULT_UUID_ALICE)
            .uuidRequiredAlice(DEFAULT_UUID_REQUIRED_ALICE)
            .byteImageAlice(DEFAULT_BYTE_IMAGE_ALICE)
            .byteImageAliceContentType(DEFAULT_BYTE_IMAGE_ALICE_CONTENT_TYPE)
            .byteImageRequiredAlice(DEFAULT_BYTE_IMAGE_REQUIRED_ALICE)
            .byteImageRequiredAliceContentType(DEFAULT_BYTE_IMAGE_REQUIRED_ALICE_CONTENT_TYPE)
            .byteImageMinbytesAlice(DEFAULT_BYTE_IMAGE_MINBYTES_ALICE)
            .byteImageMinbytesAliceContentType(DEFAULT_BYTE_IMAGE_MINBYTES_ALICE_CONTENT_TYPE)
            .byteImageMaxbytesAlice(DEFAULT_BYTE_IMAGE_MAXBYTES_ALICE)
            .byteImageMaxbytesAliceContentType(DEFAULT_BYTE_IMAGE_MAXBYTES_ALICE_CONTENT_TYPE)
            .byteAnyAlice(DEFAULT_BYTE_ANY_ALICE)
            .byteAnyAliceContentType(DEFAULT_BYTE_ANY_ALICE_CONTENT_TYPE)
            .byteAnyRequiredAlice(DEFAULT_BYTE_ANY_REQUIRED_ALICE)
            .byteAnyRequiredAliceContentType(DEFAULT_BYTE_ANY_REQUIRED_ALICE_CONTENT_TYPE)
            .byteAnyMinbytesAlice(DEFAULT_BYTE_ANY_MINBYTES_ALICE)
            .byteAnyMinbytesAliceContentType(DEFAULT_BYTE_ANY_MINBYTES_ALICE_CONTENT_TYPE)
            .byteAnyMaxbytesAlice(DEFAULT_BYTE_ANY_MAXBYTES_ALICE)
            .byteAnyMaxbytesAliceContentType(DEFAULT_BYTE_ANY_MAXBYTES_ALICE_CONTENT_TYPE)
            .byteTextAlice(DEFAULT_BYTE_TEXT_ALICE)
            .byteTextRequiredAlice(DEFAULT_BYTE_TEXT_REQUIRED_ALICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldTestPaginationEntity createUpdatedEntity() {
        return new FieldTestPaginationEntity()
            .stringAlice(UPDATED_STRING_ALICE)
            .stringRequiredAlice(UPDATED_STRING_REQUIRED_ALICE)
            .stringMinlengthAlice(UPDATED_STRING_MINLENGTH_ALICE)
            .stringMaxlengthAlice(UPDATED_STRING_MAXLENGTH_ALICE)
            .stringPatternAlice(UPDATED_STRING_PATTERN_ALICE)
            .integerAlice(UPDATED_INTEGER_ALICE)
            .integerRequiredAlice(UPDATED_INTEGER_REQUIRED_ALICE)
            .integerMinAlice(UPDATED_INTEGER_MIN_ALICE)
            .integerMaxAlice(UPDATED_INTEGER_MAX_ALICE)
            .longAlice(UPDATED_LONG_ALICE)
            .longRequiredAlice(UPDATED_LONG_REQUIRED_ALICE)
            .longMinAlice(UPDATED_LONG_MIN_ALICE)
            .longMaxAlice(UPDATED_LONG_MAX_ALICE)
            .floatAlice(UPDATED_FLOAT_ALICE)
            .floatRequiredAlice(UPDATED_FLOAT_REQUIRED_ALICE)
            .floatMinAlice(UPDATED_FLOAT_MIN_ALICE)
            .floatMaxAlice(UPDATED_FLOAT_MAX_ALICE)
            .doubleRequiredAlice(UPDATED_DOUBLE_REQUIRED_ALICE)
            .doubleMinAlice(UPDATED_DOUBLE_MIN_ALICE)
            .doubleMaxAlice(UPDATED_DOUBLE_MAX_ALICE)
            .bigDecimalRequiredAlice(UPDATED_BIG_DECIMAL_REQUIRED_ALICE)
            .bigDecimalMinAlice(UPDATED_BIG_DECIMAL_MIN_ALICE)
            .bigDecimalMaxAlice(UPDATED_BIG_DECIMAL_MAX_ALICE)
            .localDateAlice(UPDATED_LOCAL_DATE_ALICE)
            .localDateRequiredAlice(UPDATED_LOCAL_DATE_REQUIRED_ALICE)
            .instantAlice(UPDATED_INSTANT_ALICE)
            .instanteRequiredAlice(UPDATED_INSTANTE_REQUIRED_ALICE)
            .zonedDateTimeAlice(UPDATED_ZONED_DATE_TIME_ALICE)
            .zonedDateTimeRequiredAlice(UPDATED_ZONED_DATE_TIME_REQUIRED_ALICE)
            .localTimeAlice(UPDATED_LOCAL_TIME_ALICE)
            .localTimeRequiredAlice(UPDATED_LOCAL_TIME_REQUIRED_ALICE)
            .durationAlice(UPDATED_DURATION_ALICE)
            .durationRequiredAlice(UPDATED_DURATION_REQUIRED_ALICE)
            .booleanAlice(UPDATED_BOOLEAN_ALICE)
            .booleanRequiredAlice(UPDATED_BOOLEAN_REQUIRED_ALICE)
            .enumAlice(UPDATED_ENUM_ALICE)
            .enumRequiredAlice(UPDATED_ENUM_REQUIRED_ALICE)
            .uuidAlice(UPDATED_UUID_ALICE)
            .uuidRequiredAlice(UPDATED_UUID_REQUIRED_ALICE)
            .byteImageAlice(UPDATED_BYTE_IMAGE_ALICE)
            .byteImageAliceContentType(UPDATED_BYTE_IMAGE_ALICE_CONTENT_TYPE)
            .byteImageRequiredAlice(UPDATED_BYTE_IMAGE_REQUIRED_ALICE)
            .byteImageRequiredAliceContentType(UPDATED_BYTE_IMAGE_REQUIRED_ALICE_CONTENT_TYPE)
            .byteImageMinbytesAlice(UPDATED_BYTE_IMAGE_MINBYTES_ALICE)
            .byteImageMinbytesAliceContentType(UPDATED_BYTE_IMAGE_MINBYTES_ALICE_CONTENT_TYPE)
            .byteImageMaxbytesAlice(UPDATED_BYTE_IMAGE_MAXBYTES_ALICE)
            .byteImageMaxbytesAliceContentType(UPDATED_BYTE_IMAGE_MAXBYTES_ALICE_CONTENT_TYPE)
            .byteAnyAlice(UPDATED_BYTE_ANY_ALICE)
            .byteAnyAliceContentType(UPDATED_BYTE_ANY_ALICE_CONTENT_TYPE)
            .byteAnyRequiredAlice(UPDATED_BYTE_ANY_REQUIRED_ALICE)
            .byteAnyRequiredAliceContentType(UPDATED_BYTE_ANY_REQUIRED_ALICE_CONTENT_TYPE)
            .byteAnyMinbytesAlice(UPDATED_BYTE_ANY_MINBYTES_ALICE)
            .byteAnyMinbytesAliceContentType(UPDATED_BYTE_ANY_MINBYTES_ALICE_CONTENT_TYPE)
            .byteAnyMaxbytesAlice(UPDATED_BYTE_ANY_MAXBYTES_ALICE)
            .byteAnyMaxbytesAliceContentType(UPDATED_BYTE_ANY_MAXBYTES_ALICE_CONTENT_TYPE)
            .byteTextAlice(UPDATED_BYTE_TEXT_ALICE)
            .byteTextRequiredAlice(UPDATED_BYTE_TEXT_REQUIRED_ALICE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FieldTestPaginationEntity.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        fieldTestPaginationEntity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFieldTestPaginationEntity != null) {
            fieldTestPaginationEntityRepository.delete(insertedFieldTestPaginationEntity).block();
            insertedFieldTestPaginationEntity = null;
        }
        deleteEntities(em);
    }

    @Test
    void createFieldTestPaginationEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FieldTestPaginationEntity
        var returnedFieldTestPaginationEntity = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FieldTestPaginationEntity.class)
            .returnResult()
            .getResponseBody();

        // Validate the FieldTestPaginationEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFieldTestPaginationEntityUpdatableFieldsEquals(
            returnedFieldTestPaginationEntity,
            getPersistedFieldTestPaginationEntity(returnedFieldTestPaginationEntity)
        );

        insertedFieldTestPaginationEntity = returnedFieldTestPaginationEntity;
    }

    @Test
    void createFieldTestPaginationEntityWithExistingId() throws Exception {
        // Create the FieldTestPaginationEntity with an existing ID
        fieldTestPaginationEntity.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestPaginationEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkStringRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setStringRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIntegerRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setIntegerRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLongRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setLongRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFloatRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setFloatRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDoubleRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setDoubleRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkBigDecimalRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setBigDecimalRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLocalDateRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setLocalDateRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkInstanteRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setInstanteRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkZonedDateTimeRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setZonedDateTimeRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLocalTimeRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setLocalTimeRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDurationRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setDurationRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkBooleanRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setBooleanRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEnumRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setEnumRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUuidRequiredAliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestPaginationEntity.setUuidRequiredAlice(null);

        // Create the FieldTestPaginationEntity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFieldTestPaginationEntities() {
        // Initialize the database
        insertedFieldTestPaginationEntity = fieldTestPaginationEntityRepository.save(fieldTestPaginationEntity).block();

        // Get all the fieldTestPaginationEntityList
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
            .value(hasItem(fieldTestPaginationEntity.getId().intValue()))
            .jsonPath("$.[*].stringAlice")
            .value(hasItem(DEFAULT_STRING_ALICE))
            .jsonPath("$.[*].stringRequiredAlice")
            .value(hasItem(DEFAULT_STRING_REQUIRED_ALICE))
            .jsonPath("$.[*].stringMinlengthAlice")
            .value(hasItem(DEFAULT_STRING_MINLENGTH_ALICE))
            .jsonPath("$.[*].stringMaxlengthAlice")
            .value(hasItem(DEFAULT_STRING_MAXLENGTH_ALICE))
            .jsonPath("$.[*].stringPatternAlice")
            .value(hasItem(DEFAULT_STRING_PATTERN_ALICE))
            .jsonPath("$.[*].integerAlice")
            .value(hasItem(DEFAULT_INTEGER_ALICE))
            .jsonPath("$.[*].integerRequiredAlice")
            .value(hasItem(DEFAULT_INTEGER_REQUIRED_ALICE))
            .jsonPath("$.[*].integerMinAlice")
            .value(hasItem(DEFAULT_INTEGER_MIN_ALICE))
            .jsonPath("$.[*].integerMaxAlice")
            .value(hasItem(DEFAULT_INTEGER_MAX_ALICE))
            .jsonPath("$.[*].longAlice")
            .value(hasItem(DEFAULT_LONG_ALICE.intValue()))
            .jsonPath("$.[*].longRequiredAlice")
            .value(hasItem(DEFAULT_LONG_REQUIRED_ALICE.intValue()))
            .jsonPath("$.[*].longMinAlice")
            .value(hasItem(DEFAULT_LONG_MIN_ALICE.intValue()))
            .jsonPath("$.[*].longMaxAlice")
            .value(hasItem(DEFAULT_LONG_MAX_ALICE.intValue()))
            .jsonPath("$.[*].floatAlice")
            .value(hasItem(DEFAULT_FLOAT_ALICE.doubleValue()))
            .jsonPath("$.[*].floatRequiredAlice")
            .value(hasItem(DEFAULT_FLOAT_REQUIRED_ALICE.doubleValue()))
            .jsonPath("$.[*].floatMinAlice")
            .value(hasItem(DEFAULT_FLOAT_MIN_ALICE.doubleValue()))
            .jsonPath("$.[*].floatMaxAlice")
            .value(hasItem(DEFAULT_FLOAT_MAX_ALICE.doubleValue()))
            .jsonPath("$.[*].doubleRequiredAlice")
            .value(hasItem(DEFAULT_DOUBLE_REQUIRED_ALICE))
            .jsonPath("$.[*].doubleMinAlice")
            .value(hasItem(DEFAULT_DOUBLE_MIN_ALICE))
            .jsonPath("$.[*].doubleMaxAlice")
            .value(hasItem(DEFAULT_DOUBLE_MAX_ALICE))
            .jsonPath("$.[*].bigDecimalRequiredAlice")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_REQUIRED_ALICE)))
            .jsonPath("$.[*].bigDecimalMinAlice")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_MIN_ALICE)))
            .jsonPath("$.[*].bigDecimalMaxAlice")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_MAX_ALICE)))
            .jsonPath("$.[*].localDateAlice")
            .value(hasItem(DEFAULT_LOCAL_DATE_ALICE.toString()))
            .jsonPath("$.[*].localDateRequiredAlice")
            .value(hasItem(DEFAULT_LOCAL_DATE_REQUIRED_ALICE.toString()))
            .jsonPath("$.[*].instantAlice")
            .value(hasItem(DEFAULT_INSTANT_ALICE.toString()))
            .jsonPath("$.[*].instanteRequiredAlice")
            .value(hasItem(DEFAULT_INSTANTE_REQUIRED_ALICE.toString()))
            .jsonPath("$.[*].zonedDateTimeAlice")
            .value(hasItem(sameInstant(DEFAULT_ZONED_DATE_TIME_ALICE)))
            .jsonPath("$.[*].zonedDateTimeRequiredAlice")
            .value(hasItem(sameInstant(DEFAULT_ZONED_DATE_TIME_REQUIRED_ALICE)))
            .jsonPath("$.[*].localTimeAlice")
            .value(hasItem(DEFAULT_LOCAL_TIME_ALICE.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.[*].localTimeRequiredAlice")
            .value(hasItem(DEFAULT_LOCAL_TIME_REQUIRED_ALICE.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.[*].durationAlice")
            .value(hasItem(DEFAULT_DURATION_ALICE.toString()))
            .jsonPath("$.[*].durationRequiredAlice")
            .value(hasItem(DEFAULT_DURATION_REQUIRED_ALICE.toString()))
            .jsonPath("$.[*].booleanAlice")
            .value(hasItem(DEFAULT_BOOLEAN_ALICE))
            .jsonPath("$.[*].booleanRequiredAlice")
            .value(hasItem(DEFAULT_BOOLEAN_REQUIRED_ALICE))
            .jsonPath("$.[*].enumAlice")
            .value(hasItem(DEFAULT_ENUM_ALICE.toString()))
            .jsonPath("$.[*].enumRequiredAlice")
            .value(hasItem(DEFAULT_ENUM_REQUIRED_ALICE.toString()))
            .jsonPath("$.[*].uuidAlice")
            .value(hasItem(DEFAULT_UUID_ALICE.toString()))
            .jsonPath("$.[*].uuidRequiredAlice")
            .value(hasItem(DEFAULT_UUID_REQUIRED_ALICE.toString()))
            .jsonPath("$.[*].byteImageAliceContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_ALICE_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageAlice")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_ALICE)))
            .jsonPath("$.[*].byteImageRequiredAliceContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_REQUIRED_ALICE_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageRequiredAlice")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_REQUIRED_ALICE)))
            .jsonPath("$.[*].byteImageMinbytesAliceContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_MINBYTES_ALICE_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageMinbytesAlice")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MINBYTES_ALICE)))
            .jsonPath("$.[*].byteImageMaxbytesAliceContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_MAXBYTES_ALICE_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageMaxbytesAlice")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MAXBYTES_ALICE)))
            .jsonPath("$.[*].byteAnyAliceContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_ALICE_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyAlice")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_ALICE)))
            .jsonPath("$.[*].byteAnyRequiredAliceContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_REQUIRED_ALICE_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyRequiredAlice")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_REQUIRED_ALICE)))
            .jsonPath("$.[*].byteAnyMinbytesAliceContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_MINBYTES_ALICE_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyMinbytesAlice")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MINBYTES_ALICE)))
            .jsonPath("$.[*].byteAnyMaxbytesAliceContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_MAXBYTES_ALICE_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyMaxbytesAlice")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MAXBYTES_ALICE)))
            .jsonPath("$.[*].byteTextAlice")
            .value(hasItem(DEFAULT_BYTE_TEXT_ALICE))
            .jsonPath("$.[*].byteTextRequiredAlice")
            .value(hasItem(DEFAULT_BYTE_TEXT_REQUIRED_ALICE));
    }

    @Test
    void getFieldTestPaginationEntity() {
        // Initialize the database
        insertedFieldTestPaginationEntity = fieldTestPaginationEntityRepository.save(fieldTestPaginationEntity).block();

        // Get the fieldTestPaginationEntity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, fieldTestPaginationEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(fieldTestPaginationEntity.getId().intValue()))
            .jsonPath("$.stringAlice")
            .value(is(DEFAULT_STRING_ALICE))
            .jsonPath("$.stringRequiredAlice")
            .value(is(DEFAULT_STRING_REQUIRED_ALICE))
            .jsonPath("$.stringMinlengthAlice")
            .value(is(DEFAULT_STRING_MINLENGTH_ALICE))
            .jsonPath("$.stringMaxlengthAlice")
            .value(is(DEFAULT_STRING_MAXLENGTH_ALICE))
            .jsonPath("$.stringPatternAlice")
            .value(is(DEFAULT_STRING_PATTERN_ALICE))
            .jsonPath("$.integerAlice")
            .value(is(DEFAULT_INTEGER_ALICE))
            .jsonPath("$.integerRequiredAlice")
            .value(is(DEFAULT_INTEGER_REQUIRED_ALICE))
            .jsonPath("$.integerMinAlice")
            .value(is(DEFAULT_INTEGER_MIN_ALICE))
            .jsonPath("$.integerMaxAlice")
            .value(is(DEFAULT_INTEGER_MAX_ALICE))
            .jsonPath("$.longAlice")
            .value(is(DEFAULT_LONG_ALICE.intValue()))
            .jsonPath("$.longRequiredAlice")
            .value(is(DEFAULT_LONG_REQUIRED_ALICE.intValue()))
            .jsonPath("$.longMinAlice")
            .value(is(DEFAULT_LONG_MIN_ALICE.intValue()))
            .jsonPath("$.longMaxAlice")
            .value(is(DEFAULT_LONG_MAX_ALICE.intValue()))
            .jsonPath("$.floatAlice")
            .value(is(DEFAULT_FLOAT_ALICE.doubleValue()))
            .jsonPath("$.floatRequiredAlice")
            .value(is(DEFAULT_FLOAT_REQUIRED_ALICE.doubleValue()))
            .jsonPath("$.floatMinAlice")
            .value(is(DEFAULT_FLOAT_MIN_ALICE.doubleValue()))
            .jsonPath("$.floatMaxAlice")
            .value(is(DEFAULT_FLOAT_MAX_ALICE.doubleValue()))
            .jsonPath("$.doubleRequiredAlice")
            .value(is(DEFAULT_DOUBLE_REQUIRED_ALICE))
            .jsonPath("$.doubleMinAlice")
            .value(is(DEFAULT_DOUBLE_MIN_ALICE))
            .jsonPath("$.doubleMaxAlice")
            .value(is(DEFAULT_DOUBLE_MAX_ALICE))
            .jsonPath("$.bigDecimalRequiredAlice")
            .value(is(sameNumber(DEFAULT_BIG_DECIMAL_REQUIRED_ALICE)))
            .jsonPath("$.bigDecimalMinAlice")
            .value(is(sameNumber(DEFAULT_BIG_DECIMAL_MIN_ALICE)))
            .jsonPath("$.bigDecimalMaxAlice")
            .value(is(sameNumber(DEFAULT_BIG_DECIMAL_MAX_ALICE)))
            .jsonPath("$.localDateAlice")
            .value(is(DEFAULT_LOCAL_DATE_ALICE.toString()))
            .jsonPath("$.localDateRequiredAlice")
            .value(is(DEFAULT_LOCAL_DATE_REQUIRED_ALICE.toString()))
            .jsonPath("$.instantAlice")
            .value(is(DEFAULT_INSTANT_ALICE.toString()))
            .jsonPath("$.instanteRequiredAlice")
            .value(is(DEFAULT_INSTANTE_REQUIRED_ALICE.toString()))
            .jsonPath("$.zonedDateTimeAlice")
            .value(is(sameInstant(DEFAULT_ZONED_DATE_TIME_ALICE)))
            .jsonPath("$.zonedDateTimeRequiredAlice")
            .value(is(sameInstant(DEFAULT_ZONED_DATE_TIME_REQUIRED_ALICE)))
            .jsonPath("$.localTimeAlice")
            .value(is(DEFAULT_LOCAL_TIME_ALICE.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.localTimeRequiredAlice")
            .value(is(DEFAULT_LOCAL_TIME_REQUIRED_ALICE.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.durationAlice")
            .value(is(DEFAULT_DURATION_ALICE.toString()))
            .jsonPath("$.durationRequiredAlice")
            .value(is(DEFAULT_DURATION_REQUIRED_ALICE.toString()))
            .jsonPath("$.booleanAlice")
            .value(is(DEFAULT_BOOLEAN_ALICE))
            .jsonPath("$.booleanRequiredAlice")
            .value(is(DEFAULT_BOOLEAN_REQUIRED_ALICE))
            .jsonPath("$.enumAlice")
            .value(is(DEFAULT_ENUM_ALICE.toString()))
            .jsonPath("$.enumRequiredAlice")
            .value(is(DEFAULT_ENUM_REQUIRED_ALICE.toString()))
            .jsonPath("$.uuidAlice")
            .value(is(DEFAULT_UUID_ALICE.toString()))
            .jsonPath("$.uuidRequiredAlice")
            .value(is(DEFAULT_UUID_REQUIRED_ALICE.toString()))
            .jsonPath("$.byteImageAliceContentType")
            .value(is(DEFAULT_BYTE_IMAGE_ALICE_CONTENT_TYPE))
            .jsonPath("$.byteImageAlice")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_ALICE)))
            .jsonPath("$.byteImageRequiredAliceContentType")
            .value(is(DEFAULT_BYTE_IMAGE_REQUIRED_ALICE_CONTENT_TYPE))
            .jsonPath("$.byteImageRequiredAlice")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_REQUIRED_ALICE)))
            .jsonPath("$.byteImageMinbytesAliceContentType")
            .value(is(DEFAULT_BYTE_IMAGE_MINBYTES_ALICE_CONTENT_TYPE))
            .jsonPath("$.byteImageMinbytesAlice")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MINBYTES_ALICE)))
            .jsonPath("$.byteImageMaxbytesAliceContentType")
            .value(is(DEFAULT_BYTE_IMAGE_MAXBYTES_ALICE_CONTENT_TYPE))
            .jsonPath("$.byteImageMaxbytesAlice")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MAXBYTES_ALICE)))
            .jsonPath("$.byteAnyAliceContentType")
            .value(is(DEFAULT_BYTE_ANY_ALICE_CONTENT_TYPE))
            .jsonPath("$.byteAnyAlice")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_ALICE)))
            .jsonPath("$.byteAnyRequiredAliceContentType")
            .value(is(DEFAULT_BYTE_ANY_REQUIRED_ALICE_CONTENT_TYPE))
            .jsonPath("$.byteAnyRequiredAlice")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_REQUIRED_ALICE)))
            .jsonPath("$.byteAnyMinbytesAliceContentType")
            .value(is(DEFAULT_BYTE_ANY_MINBYTES_ALICE_CONTENT_TYPE))
            .jsonPath("$.byteAnyMinbytesAlice")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MINBYTES_ALICE)))
            .jsonPath("$.byteAnyMaxbytesAliceContentType")
            .value(is(DEFAULT_BYTE_ANY_MAXBYTES_ALICE_CONTENT_TYPE))
            .jsonPath("$.byteAnyMaxbytesAlice")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MAXBYTES_ALICE)))
            .jsonPath("$.byteTextAlice")
            .value(is(DEFAULT_BYTE_TEXT_ALICE))
            .jsonPath("$.byteTextRequiredAlice")
            .value(is(DEFAULT_BYTE_TEXT_REQUIRED_ALICE));
    }

    @Test
    void getNonExistingFieldTestPaginationEntity() {
        // Get the fieldTestPaginationEntity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFieldTestPaginationEntity() throws Exception {
        // Initialize the database
        insertedFieldTestPaginationEntity = fieldTestPaginationEntityRepository.save(fieldTestPaginationEntity).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestPaginationEntity
        FieldTestPaginationEntity updatedFieldTestPaginationEntity = fieldTestPaginationEntityRepository
            .findById(fieldTestPaginationEntity.getId())
            .block();
        updatedFieldTestPaginationEntity
            .stringAlice(UPDATED_STRING_ALICE)
            .stringRequiredAlice(UPDATED_STRING_REQUIRED_ALICE)
            .stringMinlengthAlice(UPDATED_STRING_MINLENGTH_ALICE)
            .stringMaxlengthAlice(UPDATED_STRING_MAXLENGTH_ALICE)
            .stringPatternAlice(UPDATED_STRING_PATTERN_ALICE)
            .integerAlice(UPDATED_INTEGER_ALICE)
            .integerRequiredAlice(UPDATED_INTEGER_REQUIRED_ALICE)
            .integerMinAlice(UPDATED_INTEGER_MIN_ALICE)
            .integerMaxAlice(UPDATED_INTEGER_MAX_ALICE)
            .longAlice(UPDATED_LONG_ALICE)
            .longRequiredAlice(UPDATED_LONG_REQUIRED_ALICE)
            .longMinAlice(UPDATED_LONG_MIN_ALICE)
            .longMaxAlice(UPDATED_LONG_MAX_ALICE)
            .floatAlice(UPDATED_FLOAT_ALICE)
            .floatRequiredAlice(UPDATED_FLOAT_REQUIRED_ALICE)
            .floatMinAlice(UPDATED_FLOAT_MIN_ALICE)
            .floatMaxAlice(UPDATED_FLOAT_MAX_ALICE)
            .doubleRequiredAlice(UPDATED_DOUBLE_REQUIRED_ALICE)
            .doubleMinAlice(UPDATED_DOUBLE_MIN_ALICE)
            .doubleMaxAlice(UPDATED_DOUBLE_MAX_ALICE)
            .bigDecimalRequiredAlice(UPDATED_BIG_DECIMAL_REQUIRED_ALICE)
            .bigDecimalMinAlice(UPDATED_BIG_DECIMAL_MIN_ALICE)
            .bigDecimalMaxAlice(UPDATED_BIG_DECIMAL_MAX_ALICE)
            .localDateAlice(UPDATED_LOCAL_DATE_ALICE)
            .localDateRequiredAlice(UPDATED_LOCAL_DATE_REQUIRED_ALICE)
            .instantAlice(UPDATED_INSTANT_ALICE)
            .instanteRequiredAlice(UPDATED_INSTANTE_REQUIRED_ALICE)
            .zonedDateTimeAlice(UPDATED_ZONED_DATE_TIME_ALICE)
            .zonedDateTimeRequiredAlice(UPDATED_ZONED_DATE_TIME_REQUIRED_ALICE)
            .localTimeAlice(UPDATED_LOCAL_TIME_ALICE)
            .localTimeRequiredAlice(UPDATED_LOCAL_TIME_REQUIRED_ALICE)
            .durationAlice(UPDATED_DURATION_ALICE)
            .durationRequiredAlice(UPDATED_DURATION_REQUIRED_ALICE)
            .booleanAlice(UPDATED_BOOLEAN_ALICE)
            .booleanRequiredAlice(UPDATED_BOOLEAN_REQUIRED_ALICE)
            .enumAlice(UPDATED_ENUM_ALICE)
            .enumRequiredAlice(UPDATED_ENUM_REQUIRED_ALICE)
            .uuidAlice(UPDATED_UUID_ALICE)
            .uuidRequiredAlice(UPDATED_UUID_REQUIRED_ALICE)
            .byteImageAlice(UPDATED_BYTE_IMAGE_ALICE)
            .byteImageAliceContentType(UPDATED_BYTE_IMAGE_ALICE_CONTENT_TYPE)
            .byteImageRequiredAlice(UPDATED_BYTE_IMAGE_REQUIRED_ALICE)
            .byteImageRequiredAliceContentType(UPDATED_BYTE_IMAGE_REQUIRED_ALICE_CONTENT_TYPE)
            .byteImageMinbytesAlice(UPDATED_BYTE_IMAGE_MINBYTES_ALICE)
            .byteImageMinbytesAliceContentType(UPDATED_BYTE_IMAGE_MINBYTES_ALICE_CONTENT_TYPE)
            .byteImageMaxbytesAlice(UPDATED_BYTE_IMAGE_MAXBYTES_ALICE)
            .byteImageMaxbytesAliceContentType(UPDATED_BYTE_IMAGE_MAXBYTES_ALICE_CONTENT_TYPE)
            .byteAnyAlice(UPDATED_BYTE_ANY_ALICE)
            .byteAnyAliceContentType(UPDATED_BYTE_ANY_ALICE_CONTENT_TYPE)
            .byteAnyRequiredAlice(UPDATED_BYTE_ANY_REQUIRED_ALICE)
            .byteAnyRequiredAliceContentType(UPDATED_BYTE_ANY_REQUIRED_ALICE_CONTENT_TYPE)
            .byteAnyMinbytesAlice(UPDATED_BYTE_ANY_MINBYTES_ALICE)
            .byteAnyMinbytesAliceContentType(UPDATED_BYTE_ANY_MINBYTES_ALICE_CONTENT_TYPE)
            .byteAnyMaxbytesAlice(UPDATED_BYTE_ANY_MAXBYTES_ALICE)
            .byteAnyMaxbytesAliceContentType(UPDATED_BYTE_ANY_MAXBYTES_ALICE_CONTENT_TYPE)
            .byteTextAlice(UPDATED_BYTE_TEXT_ALICE)
            .byteTextRequiredAlice(UPDATED_BYTE_TEXT_REQUIRED_ALICE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFieldTestPaginationEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedFieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestPaginationEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFieldTestPaginationEntityToMatchAllProperties(updatedFieldTestPaginationEntity);
    }

    @Test
    void putNonExistingFieldTestPaginationEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestPaginationEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fieldTestPaginationEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestPaginationEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFieldTestPaginationEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestPaginationEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestPaginationEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFieldTestPaginationEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestPaginationEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FieldTestPaginationEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFieldTestPaginationEntityWithPatch() throws Exception {
        // Initialize the database
        insertedFieldTestPaginationEntity = fieldTestPaginationEntityRepository.save(fieldTestPaginationEntity).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestPaginationEntity using partial update
        FieldTestPaginationEntity partialUpdatedFieldTestPaginationEntity = new FieldTestPaginationEntity();
        partialUpdatedFieldTestPaginationEntity.setId(fieldTestPaginationEntity.getId());

        partialUpdatedFieldTestPaginationEntity
            .stringAlice(UPDATED_STRING_ALICE)
            .stringRequiredAlice(UPDATED_STRING_REQUIRED_ALICE)
            .stringMaxlengthAlice(UPDATED_STRING_MAXLENGTH_ALICE)
            .integerAlice(UPDATED_INTEGER_ALICE)
            .integerMaxAlice(UPDATED_INTEGER_MAX_ALICE)
            .longMinAlice(UPDATED_LONG_MIN_ALICE)
            .longMaxAlice(UPDATED_LONG_MAX_ALICE)
            .floatAlice(UPDATED_FLOAT_ALICE)
            .floatRequiredAlice(UPDATED_FLOAT_REQUIRED_ALICE)
            .bigDecimalMinAlice(UPDATED_BIG_DECIMAL_MIN_ALICE)
            .localDateRequiredAlice(UPDATED_LOCAL_DATE_REQUIRED_ALICE)
            .zonedDateTimeRequiredAlice(UPDATED_ZONED_DATE_TIME_REQUIRED_ALICE)
            .enumAlice(UPDATED_ENUM_ALICE)
            .enumRequiredAlice(UPDATED_ENUM_REQUIRED_ALICE)
            .uuidRequiredAlice(UPDATED_UUID_REQUIRED_ALICE)
            .byteImageMinbytesAlice(UPDATED_BYTE_IMAGE_MINBYTES_ALICE)
            .byteImageMinbytesAliceContentType(UPDATED_BYTE_IMAGE_MINBYTES_ALICE_CONTENT_TYPE)
            .byteAnyRequiredAlice(UPDATED_BYTE_ANY_REQUIRED_ALICE)
            .byteAnyRequiredAliceContentType(UPDATED_BYTE_ANY_REQUIRED_ALICE_CONTENT_TYPE)
            .byteAnyMinbytesAlice(UPDATED_BYTE_ANY_MINBYTES_ALICE)
            .byteAnyMinbytesAliceContentType(UPDATED_BYTE_ANY_MINBYTES_ALICE_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFieldTestPaginationEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestPaginationEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFieldTestPaginationEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFieldTestPaginationEntity, fieldTestPaginationEntity),
            getPersistedFieldTestPaginationEntity(fieldTestPaginationEntity)
        );
    }

    @Test
    void fullUpdateFieldTestPaginationEntityWithPatch() throws Exception {
        // Initialize the database
        insertedFieldTestPaginationEntity = fieldTestPaginationEntityRepository.save(fieldTestPaginationEntity).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestPaginationEntity using partial update
        FieldTestPaginationEntity partialUpdatedFieldTestPaginationEntity = new FieldTestPaginationEntity();
        partialUpdatedFieldTestPaginationEntity.setId(fieldTestPaginationEntity.getId());

        partialUpdatedFieldTestPaginationEntity
            .stringAlice(UPDATED_STRING_ALICE)
            .stringRequiredAlice(UPDATED_STRING_REQUIRED_ALICE)
            .stringMinlengthAlice(UPDATED_STRING_MINLENGTH_ALICE)
            .stringMaxlengthAlice(UPDATED_STRING_MAXLENGTH_ALICE)
            .stringPatternAlice(UPDATED_STRING_PATTERN_ALICE)
            .integerAlice(UPDATED_INTEGER_ALICE)
            .integerRequiredAlice(UPDATED_INTEGER_REQUIRED_ALICE)
            .integerMinAlice(UPDATED_INTEGER_MIN_ALICE)
            .integerMaxAlice(UPDATED_INTEGER_MAX_ALICE)
            .longAlice(UPDATED_LONG_ALICE)
            .longRequiredAlice(UPDATED_LONG_REQUIRED_ALICE)
            .longMinAlice(UPDATED_LONG_MIN_ALICE)
            .longMaxAlice(UPDATED_LONG_MAX_ALICE)
            .floatAlice(UPDATED_FLOAT_ALICE)
            .floatRequiredAlice(UPDATED_FLOAT_REQUIRED_ALICE)
            .floatMinAlice(UPDATED_FLOAT_MIN_ALICE)
            .floatMaxAlice(UPDATED_FLOAT_MAX_ALICE)
            .doubleRequiredAlice(UPDATED_DOUBLE_REQUIRED_ALICE)
            .doubleMinAlice(UPDATED_DOUBLE_MIN_ALICE)
            .doubleMaxAlice(UPDATED_DOUBLE_MAX_ALICE)
            .bigDecimalRequiredAlice(UPDATED_BIG_DECIMAL_REQUIRED_ALICE)
            .bigDecimalMinAlice(UPDATED_BIG_DECIMAL_MIN_ALICE)
            .bigDecimalMaxAlice(UPDATED_BIG_DECIMAL_MAX_ALICE)
            .localDateAlice(UPDATED_LOCAL_DATE_ALICE)
            .localDateRequiredAlice(UPDATED_LOCAL_DATE_REQUIRED_ALICE)
            .instantAlice(UPDATED_INSTANT_ALICE)
            .instanteRequiredAlice(UPDATED_INSTANTE_REQUIRED_ALICE)
            .zonedDateTimeAlice(UPDATED_ZONED_DATE_TIME_ALICE)
            .zonedDateTimeRequiredAlice(UPDATED_ZONED_DATE_TIME_REQUIRED_ALICE)
            .localTimeAlice(UPDATED_LOCAL_TIME_ALICE)
            .localTimeRequiredAlice(UPDATED_LOCAL_TIME_REQUIRED_ALICE)
            .durationAlice(UPDATED_DURATION_ALICE)
            .durationRequiredAlice(UPDATED_DURATION_REQUIRED_ALICE)
            .booleanAlice(UPDATED_BOOLEAN_ALICE)
            .booleanRequiredAlice(UPDATED_BOOLEAN_REQUIRED_ALICE)
            .enumAlice(UPDATED_ENUM_ALICE)
            .enumRequiredAlice(UPDATED_ENUM_REQUIRED_ALICE)
            .uuidAlice(UPDATED_UUID_ALICE)
            .uuidRequiredAlice(UPDATED_UUID_REQUIRED_ALICE)
            .byteImageAlice(UPDATED_BYTE_IMAGE_ALICE)
            .byteImageAliceContentType(UPDATED_BYTE_IMAGE_ALICE_CONTENT_TYPE)
            .byteImageRequiredAlice(UPDATED_BYTE_IMAGE_REQUIRED_ALICE)
            .byteImageRequiredAliceContentType(UPDATED_BYTE_IMAGE_REQUIRED_ALICE_CONTENT_TYPE)
            .byteImageMinbytesAlice(UPDATED_BYTE_IMAGE_MINBYTES_ALICE)
            .byteImageMinbytesAliceContentType(UPDATED_BYTE_IMAGE_MINBYTES_ALICE_CONTENT_TYPE)
            .byteImageMaxbytesAlice(UPDATED_BYTE_IMAGE_MAXBYTES_ALICE)
            .byteImageMaxbytesAliceContentType(UPDATED_BYTE_IMAGE_MAXBYTES_ALICE_CONTENT_TYPE)
            .byteAnyAlice(UPDATED_BYTE_ANY_ALICE)
            .byteAnyAliceContentType(UPDATED_BYTE_ANY_ALICE_CONTENT_TYPE)
            .byteAnyRequiredAlice(UPDATED_BYTE_ANY_REQUIRED_ALICE)
            .byteAnyRequiredAliceContentType(UPDATED_BYTE_ANY_REQUIRED_ALICE_CONTENT_TYPE)
            .byteAnyMinbytesAlice(UPDATED_BYTE_ANY_MINBYTES_ALICE)
            .byteAnyMinbytesAliceContentType(UPDATED_BYTE_ANY_MINBYTES_ALICE_CONTENT_TYPE)
            .byteAnyMaxbytesAlice(UPDATED_BYTE_ANY_MAXBYTES_ALICE)
            .byteAnyMaxbytesAliceContentType(UPDATED_BYTE_ANY_MAXBYTES_ALICE_CONTENT_TYPE)
            .byteTextAlice(UPDATED_BYTE_TEXT_ALICE)
            .byteTextRequiredAlice(UPDATED_BYTE_TEXT_REQUIRED_ALICE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFieldTestPaginationEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestPaginationEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFieldTestPaginationEntityUpdatableFieldsEquals(
            partialUpdatedFieldTestPaginationEntity,
            getPersistedFieldTestPaginationEntity(partialUpdatedFieldTestPaginationEntity)
        );
    }

    @Test
    void patchNonExistingFieldTestPaginationEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestPaginationEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, fieldTestPaginationEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestPaginationEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFieldTestPaginationEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestPaginationEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestPaginationEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFieldTestPaginationEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestPaginationEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestPaginationEntity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FieldTestPaginationEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFieldTestPaginationEntity() {
        // Initialize the database
        insertedFieldTestPaginationEntity = fieldTestPaginationEntityRepository.save(fieldTestPaginationEntity).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fieldTestPaginationEntity
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, fieldTestPaginationEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fieldTestPaginationEntityRepository.count().block();
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

    protected FieldTestPaginationEntity getPersistedFieldTestPaginationEntity(FieldTestPaginationEntity fieldTestPaginationEntity) {
        return fieldTestPaginationEntityRepository.findById(fieldTestPaginationEntity.getId()).block();
    }

    protected void assertPersistedFieldTestPaginationEntityToMatchAllProperties(
        FieldTestPaginationEntity expectedFieldTestPaginationEntity
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFieldTestPaginationEntityAllPropertiesEquals(expectedFieldTestPaginationEntity, getPersistedFieldTestPaginationEntity(expectedFieldTestPaginationEntity));
        assertFieldTestPaginationEntityUpdatableFieldsEquals(
            expectedFieldTestPaginationEntity,
            getPersistedFieldTestPaginationEntity(expectedFieldTestPaginationEntity)
        );
    }

    protected void assertPersistedFieldTestPaginationEntityToMatchUpdatableProperties(
        FieldTestPaginationEntity expectedFieldTestPaginationEntity
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFieldTestPaginationEntityAllUpdatablePropertiesEquals(expectedFieldTestPaginationEntity, getPersistedFieldTestPaginationEntity(expectedFieldTestPaginationEntity));
        assertFieldTestPaginationEntityUpdatableFieldsEquals(
            expectedFieldTestPaginationEntity,
            getPersistedFieldTestPaginationEntity(expectedFieldTestPaginationEntity)
        );
    }
}
