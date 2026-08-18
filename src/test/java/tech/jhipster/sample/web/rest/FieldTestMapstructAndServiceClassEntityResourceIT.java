package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static tech.jhipster.sample.domain.FieldTestMapstructAndServiceClassEntityAsserts.*;
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
import tech.jhipster.sample.domain.FieldTestMapstructAndServiceClassEntity;
import tech.jhipster.sample.domain.enumeration.EnumFieldClass;
import tech.jhipster.sample.domain.enumeration.EnumRequiredFieldClass;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.FieldTestMapstructAndServiceClassEntityRepository;
import tech.jhipster.sample.service.dto.FieldTestMapstructAndServiceClassEntityDTO;
import tech.jhipster.sample.service.mapper.FieldTestMapstructAndServiceClassEntityMapper;

/**
 * Integration tests for the {@link FieldTestMapstructAndServiceClassEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FieldTestMapstructAndServiceClassEntityResourceIT {

    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final String DEFAULT_STRING_EVA = "AAAAAAAAAA";
    private static final String UPDATED_STRING_EVA = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_REQUIRED_EVA = "AAAAAAAAAA";
    private static final String UPDATED_STRING_REQUIRED_EVA = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_MINLENGTH_EVA = "AAAAAAAAAA";
    private static final String UPDATED_STRING_MINLENGTH_EVA = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_MAXLENGTH_EVA = "AAAAAAAAAA";
    private static final String UPDATED_STRING_MAXLENGTH_EVA = "BBBBBBBBBB";

    private static final String DEFAULT_STRING_PATTERN_EVA = "AAAAAAAAAA";
    private static final String UPDATED_STRING_PATTERN_EVA = "BBBBBBBBBB";

    private static final Integer DEFAULT_INTEGER_EVA = 1;
    private static final Integer UPDATED_INTEGER_EVA = 2;

    private static final Integer DEFAULT_INTEGER_REQUIRED_EVA = 1;
    private static final Integer UPDATED_INTEGER_REQUIRED_EVA = 2;

    private static final Integer DEFAULT_INTEGER_MIN_EVA = 0;
    private static final Integer UPDATED_INTEGER_MIN_EVA = 1;

    private static final Integer DEFAULT_INTEGER_MAX_EVA = 100;
    private static final Integer UPDATED_INTEGER_MAX_EVA = 99;

    private static final Long DEFAULT_LONG_EVA = 1L;
    private static final Long UPDATED_LONG_EVA = 2L;

    private static final Long DEFAULT_LONG_REQUIRED_EVA = 1L;
    private static final Long UPDATED_LONG_REQUIRED_EVA = 2L;

    private static final Long DEFAULT_LONG_MIN_EVA = 0L;
    private static final Long UPDATED_LONG_MIN_EVA = 1L;

    private static final Long DEFAULT_LONG_MAX_EVA = 100L;
    private static final Long UPDATED_LONG_MAX_EVA = 99L;

    private static final Float DEFAULT_FLOAT_EVA = 1F;
    private static final Float UPDATED_FLOAT_EVA = 2F;

    private static final Float DEFAULT_FLOAT_REQUIRED_EVA = 1F;
    private static final Float UPDATED_FLOAT_REQUIRED_EVA = 2F;

    private static final Float DEFAULT_FLOAT_MIN_EVA = 0F;
    private static final Float UPDATED_FLOAT_MIN_EVA = 1F;

    private static final Float DEFAULT_FLOAT_MAX_EVA = 100F;
    private static final Float UPDATED_FLOAT_MAX_EVA = 99F;

    private static final Double DEFAULT_DOUBLE_REQUIRED_EVA = 1D;
    private static final Double UPDATED_DOUBLE_REQUIRED_EVA = 2D;

    private static final Double DEFAULT_DOUBLE_MIN_EVA = 0D;
    private static final Double UPDATED_DOUBLE_MIN_EVA = 1D;

    private static final Double DEFAULT_DOUBLE_MAX_EVA = 100D;
    private static final Double UPDATED_DOUBLE_MAX_EVA = 99D;

    private static final BigDecimal DEFAULT_BIG_DECIMAL_REQUIRED_EVA = new BigDecimal(1);
    private static final BigDecimal UPDATED_BIG_DECIMAL_REQUIRED_EVA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BIG_DECIMAL_MIN_EVA = new BigDecimal(0);
    private static final BigDecimal UPDATED_BIG_DECIMAL_MIN_EVA = new BigDecimal(1);

    private static final BigDecimal DEFAULT_BIG_DECIMAL_MAX_EVA = new BigDecimal(100);
    private static final BigDecimal UPDATED_BIG_DECIMAL_MAX_EVA = new BigDecimal(99);

    private static final LocalDate DEFAULT_LOCAL_DATE_EVA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOCAL_DATE_EVA = LocalDate.parse("2020-08-04");

    private static final LocalDate DEFAULT_LOCAL_DATE_REQUIRED_EVA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOCAL_DATE_REQUIRED_EVA = LocalDate.parse("2020-08-04");

    private static final Instant DEFAULT_INSTANT_EVA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSTANT_EVA = Instant.ofEpochMilli(1596513172471L);

    private static final Instant DEFAULT_INSTANTE_REQUIRED_EVA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSTANTE_REQUIRED_EVA = Instant.ofEpochMilli(1596513172471L);

    private static final ZonedDateTime DEFAULT_ZONED_DATE_TIME_EVA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ZONED_DATE_TIME_EVA = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(1596513172471L),
        ZoneOffset.UTC
    );

    private static final ZonedDateTime DEFAULT_ZONED_DATE_TIME_REQUIRED_EVA = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_ZONED_DATE_TIME_REQUIRED_EVA = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(1596513172471L),
        ZoneOffset.UTC
    );

    private static final LocalTime DEFAULT_LOCAL_TIME_EVA = LocalTime.NOON;
    private static final LocalTime UPDATED_LOCAL_TIME_EVA = LocalTime.MAX.withNano(0);

    private static final LocalTime DEFAULT_LOCAL_TIME_REQUIRED_EVA = LocalTime.NOON;
    private static final LocalTime UPDATED_LOCAL_TIME_REQUIRED_EVA = LocalTime.MAX.withNano(0);

    private static final Duration DEFAULT_DURATION_EVA = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION_EVA = Duration.ofHours(12);

    private static final Duration DEFAULT_DURATION_REQUIRED_EVA = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION_REQUIRED_EVA = Duration.ofHours(12);

    private static final Boolean DEFAULT_BOOLEAN_EVA = false;
    private static final Boolean UPDATED_BOOLEAN_EVA = true;

    private static final Boolean DEFAULT_BOOLEAN_REQUIRED_EVA = false;
    private static final Boolean UPDATED_BOOLEAN_REQUIRED_EVA = true;

    private static final EnumFieldClass DEFAULT_ENUM_EVA = EnumFieldClass.ENUM_VALUE_1;
    private static final EnumFieldClass UPDATED_ENUM_EVA = EnumFieldClass.ENUM_VALUE_2;

    private static final EnumRequiredFieldClass DEFAULT_ENUM_REQUIRED_EVA = EnumRequiredFieldClass.ENUM_VALUE_1;
    private static final EnumRequiredFieldClass UPDATED_ENUM_REQUIRED_EVA = EnumRequiredFieldClass.ENUM_VALUE_2;

    private static final UUID DEFAULT_UUID_EVA = UUID.randomUUID();
    private static final UUID UPDATED_UUID_EVA = UUID.randomUUID();

    private static final UUID DEFAULT_UUID_REQUIRED_EVA = UUID.randomUUID();
    private static final UUID UPDATED_UUID_REQUIRED_EVA = UUID.randomUUID();

    private static final byte[] DEFAULT_BYTE_IMAGE_EVA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_EVA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_EVA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_EVA_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_IMAGE_REQUIRED_EVA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_REQUIRED_EVA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_REQUIRED_EVA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_REQUIRED_EVA_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_IMAGE_MINBYTES_EVA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_MINBYTES_EVA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_MINBYTES_EVA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_MINBYTES_EVA_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_IMAGE_MAXBYTES_EVA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_IMAGE_MAXBYTES_EVA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_IMAGE_MAXBYTES_EVA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_IMAGE_MAXBYTES_EVA_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_EVA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_EVA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_EVA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_EVA_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_REQUIRED_EVA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_REQUIRED_EVA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_REQUIRED_EVA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_REQUIRED_EVA_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_MINBYTES_EVA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_MINBYTES_EVA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_MINBYTES_EVA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_MINBYTES_EVA_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BYTE_ANY_MAXBYTES_EVA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTE_ANY_MAXBYTES_EVA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTE_ANY_MAXBYTES_EVA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTE_ANY_MAXBYTES_EVA_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_BYTE_TEXT_EVA = "AAAAAAAAAA";
    private static final String UPDATED_BYTE_TEXT_EVA = "BBBBBBBBBB";

    private static final String DEFAULT_BYTE_TEXT_REQUIRED_EVA = "AAAAAAAAAA";
    private static final String UPDATED_BYTE_TEXT_REQUIRED_EVA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/field-test-mapstruct-and-service-class-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FieldTestMapstructAndServiceClassEntityRepository fieldTestMapstructAndServiceClassEntityRepository;

    @Autowired
    private FieldTestMapstructAndServiceClassEntityMapper fieldTestMapstructAndServiceClassEntityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FieldTestMapstructAndServiceClassEntity fieldTestMapstructAndServiceClassEntity;

    private FieldTestMapstructAndServiceClassEntity insertedFieldTestMapstructAndServiceClassEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldTestMapstructAndServiceClassEntity createEntity() {
        return new FieldTestMapstructAndServiceClassEntity()
            .stringEva(DEFAULT_STRING_EVA)
            .stringRequiredEva(DEFAULT_STRING_REQUIRED_EVA)
            .stringMinlengthEva(DEFAULT_STRING_MINLENGTH_EVA)
            .stringMaxlengthEva(DEFAULT_STRING_MAXLENGTH_EVA)
            .stringPatternEva(DEFAULT_STRING_PATTERN_EVA)
            .integerEva(DEFAULT_INTEGER_EVA)
            .integerRequiredEva(DEFAULT_INTEGER_REQUIRED_EVA)
            .integerMinEva(DEFAULT_INTEGER_MIN_EVA)
            .integerMaxEva(DEFAULT_INTEGER_MAX_EVA)
            .longEva(DEFAULT_LONG_EVA)
            .longRequiredEva(DEFAULT_LONG_REQUIRED_EVA)
            .longMinEva(DEFAULT_LONG_MIN_EVA)
            .longMaxEva(DEFAULT_LONG_MAX_EVA)
            .floatEva(DEFAULT_FLOAT_EVA)
            .floatRequiredEva(DEFAULT_FLOAT_REQUIRED_EVA)
            .floatMinEva(DEFAULT_FLOAT_MIN_EVA)
            .floatMaxEva(DEFAULT_FLOAT_MAX_EVA)
            .doubleRequiredEva(DEFAULT_DOUBLE_REQUIRED_EVA)
            .doubleMinEva(DEFAULT_DOUBLE_MIN_EVA)
            .doubleMaxEva(DEFAULT_DOUBLE_MAX_EVA)
            .bigDecimalRequiredEva(DEFAULT_BIG_DECIMAL_REQUIRED_EVA)
            .bigDecimalMinEva(DEFAULT_BIG_DECIMAL_MIN_EVA)
            .bigDecimalMaxEva(DEFAULT_BIG_DECIMAL_MAX_EVA)
            .localDateEva(DEFAULT_LOCAL_DATE_EVA)
            .localDateRequiredEva(DEFAULT_LOCAL_DATE_REQUIRED_EVA)
            .instantEva(DEFAULT_INSTANT_EVA)
            .instanteRequiredEva(DEFAULT_INSTANTE_REQUIRED_EVA)
            .zonedDateTimeEva(DEFAULT_ZONED_DATE_TIME_EVA)
            .zonedDateTimeRequiredEva(DEFAULT_ZONED_DATE_TIME_REQUIRED_EVA)
            .localTimeEva(DEFAULT_LOCAL_TIME_EVA)
            .localTimeRequiredEva(DEFAULT_LOCAL_TIME_REQUIRED_EVA)
            .durationEva(DEFAULT_DURATION_EVA)
            .durationRequiredEva(DEFAULT_DURATION_REQUIRED_EVA)
            .booleanEva(DEFAULT_BOOLEAN_EVA)
            .booleanRequiredEva(DEFAULT_BOOLEAN_REQUIRED_EVA)
            .enumEva(DEFAULT_ENUM_EVA)
            .enumRequiredEva(DEFAULT_ENUM_REQUIRED_EVA)
            .uuidEva(DEFAULT_UUID_EVA)
            .uuidRequiredEva(DEFAULT_UUID_REQUIRED_EVA)
            .byteImageEva(DEFAULT_BYTE_IMAGE_EVA)
            .byteImageEvaContentType(DEFAULT_BYTE_IMAGE_EVA_CONTENT_TYPE)
            .byteImageRequiredEva(DEFAULT_BYTE_IMAGE_REQUIRED_EVA)
            .byteImageRequiredEvaContentType(DEFAULT_BYTE_IMAGE_REQUIRED_EVA_CONTENT_TYPE)
            .byteImageMinbytesEva(DEFAULT_BYTE_IMAGE_MINBYTES_EVA)
            .byteImageMinbytesEvaContentType(DEFAULT_BYTE_IMAGE_MINBYTES_EVA_CONTENT_TYPE)
            .byteImageMaxbytesEva(DEFAULT_BYTE_IMAGE_MAXBYTES_EVA)
            .byteImageMaxbytesEvaContentType(DEFAULT_BYTE_IMAGE_MAXBYTES_EVA_CONTENT_TYPE)
            .byteAnyEva(DEFAULT_BYTE_ANY_EVA)
            .byteAnyEvaContentType(DEFAULT_BYTE_ANY_EVA_CONTENT_TYPE)
            .byteAnyRequiredEva(DEFAULT_BYTE_ANY_REQUIRED_EVA)
            .byteAnyRequiredEvaContentType(DEFAULT_BYTE_ANY_REQUIRED_EVA_CONTENT_TYPE)
            .byteAnyMinbytesEva(DEFAULT_BYTE_ANY_MINBYTES_EVA)
            .byteAnyMinbytesEvaContentType(DEFAULT_BYTE_ANY_MINBYTES_EVA_CONTENT_TYPE)
            .byteAnyMaxbytesEva(DEFAULT_BYTE_ANY_MAXBYTES_EVA)
            .byteAnyMaxbytesEvaContentType(DEFAULT_BYTE_ANY_MAXBYTES_EVA_CONTENT_TYPE)
            .byteTextEva(DEFAULT_BYTE_TEXT_EVA)
            .byteTextRequiredEva(DEFAULT_BYTE_TEXT_REQUIRED_EVA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldTestMapstructAndServiceClassEntity createUpdatedEntity() {
        return new FieldTestMapstructAndServiceClassEntity()
            .stringEva(UPDATED_STRING_EVA)
            .stringRequiredEva(UPDATED_STRING_REQUIRED_EVA)
            .stringMinlengthEva(UPDATED_STRING_MINLENGTH_EVA)
            .stringMaxlengthEva(UPDATED_STRING_MAXLENGTH_EVA)
            .stringPatternEva(UPDATED_STRING_PATTERN_EVA)
            .integerEva(UPDATED_INTEGER_EVA)
            .integerRequiredEva(UPDATED_INTEGER_REQUIRED_EVA)
            .integerMinEva(UPDATED_INTEGER_MIN_EVA)
            .integerMaxEva(UPDATED_INTEGER_MAX_EVA)
            .longEva(UPDATED_LONG_EVA)
            .longRequiredEva(UPDATED_LONG_REQUIRED_EVA)
            .longMinEva(UPDATED_LONG_MIN_EVA)
            .longMaxEva(UPDATED_LONG_MAX_EVA)
            .floatEva(UPDATED_FLOAT_EVA)
            .floatRequiredEva(UPDATED_FLOAT_REQUIRED_EVA)
            .floatMinEva(UPDATED_FLOAT_MIN_EVA)
            .floatMaxEva(UPDATED_FLOAT_MAX_EVA)
            .doubleRequiredEva(UPDATED_DOUBLE_REQUIRED_EVA)
            .doubleMinEva(UPDATED_DOUBLE_MIN_EVA)
            .doubleMaxEva(UPDATED_DOUBLE_MAX_EVA)
            .bigDecimalRequiredEva(UPDATED_BIG_DECIMAL_REQUIRED_EVA)
            .bigDecimalMinEva(UPDATED_BIG_DECIMAL_MIN_EVA)
            .bigDecimalMaxEva(UPDATED_BIG_DECIMAL_MAX_EVA)
            .localDateEva(UPDATED_LOCAL_DATE_EVA)
            .localDateRequiredEva(UPDATED_LOCAL_DATE_REQUIRED_EVA)
            .instantEva(UPDATED_INSTANT_EVA)
            .instanteRequiredEva(UPDATED_INSTANTE_REQUIRED_EVA)
            .zonedDateTimeEva(UPDATED_ZONED_DATE_TIME_EVA)
            .zonedDateTimeRequiredEva(UPDATED_ZONED_DATE_TIME_REQUIRED_EVA)
            .localTimeEva(UPDATED_LOCAL_TIME_EVA)
            .localTimeRequiredEva(UPDATED_LOCAL_TIME_REQUIRED_EVA)
            .durationEva(UPDATED_DURATION_EVA)
            .durationRequiredEva(UPDATED_DURATION_REQUIRED_EVA)
            .booleanEva(UPDATED_BOOLEAN_EVA)
            .booleanRequiredEva(UPDATED_BOOLEAN_REQUIRED_EVA)
            .enumEva(UPDATED_ENUM_EVA)
            .enumRequiredEva(UPDATED_ENUM_REQUIRED_EVA)
            .uuidEva(UPDATED_UUID_EVA)
            .uuidRequiredEva(UPDATED_UUID_REQUIRED_EVA)
            .byteImageEva(UPDATED_BYTE_IMAGE_EVA)
            .byteImageEvaContentType(UPDATED_BYTE_IMAGE_EVA_CONTENT_TYPE)
            .byteImageRequiredEva(UPDATED_BYTE_IMAGE_REQUIRED_EVA)
            .byteImageRequiredEvaContentType(UPDATED_BYTE_IMAGE_REQUIRED_EVA_CONTENT_TYPE)
            .byteImageMinbytesEva(UPDATED_BYTE_IMAGE_MINBYTES_EVA)
            .byteImageMinbytesEvaContentType(UPDATED_BYTE_IMAGE_MINBYTES_EVA_CONTENT_TYPE)
            .byteImageMaxbytesEva(UPDATED_BYTE_IMAGE_MAXBYTES_EVA)
            .byteImageMaxbytesEvaContentType(UPDATED_BYTE_IMAGE_MAXBYTES_EVA_CONTENT_TYPE)
            .byteAnyEva(UPDATED_BYTE_ANY_EVA)
            .byteAnyEvaContentType(UPDATED_BYTE_ANY_EVA_CONTENT_TYPE)
            .byteAnyRequiredEva(UPDATED_BYTE_ANY_REQUIRED_EVA)
            .byteAnyRequiredEvaContentType(UPDATED_BYTE_ANY_REQUIRED_EVA_CONTENT_TYPE)
            .byteAnyMinbytesEva(UPDATED_BYTE_ANY_MINBYTES_EVA)
            .byteAnyMinbytesEvaContentType(UPDATED_BYTE_ANY_MINBYTES_EVA_CONTENT_TYPE)
            .byteAnyMaxbytesEva(UPDATED_BYTE_ANY_MAXBYTES_EVA)
            .byteAnyMaxbytesEvaContentType(UPDATED_BYTE_ANY_MAXBYTES_EVA_CONTENT_TYPE)
            .byteTextEva(UPDATED_BYTE_TEXT_EVA)
            .byteTextRequiredEva(UPDATED_BYTE_TEXT_REQUIRED_EVA);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FieldTestMapstructAndServiceClassEntity.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        fieldTestMapstructAndServiceClassEntity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFieldTestMapstructAndServiceClassEntity != null) {
            fieldTestMapstructAndServiceClassEntityRepository.delete(insertedFieldTestMapstructAndServiceClassEntity).block();
            insertedFieldTestMapstructAndServiceClassEntity = null;
        }
        deleteEntities(em);
    }

    @Test
    void createFieldTestMapstructAndServiceClassEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FieldTestMapstructAndServiceClassEntity
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);
        var returnedFieldTestMapstructAndServiceClassEntityDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FieldTestMapstructAndServiceClassEntityDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the FieldTestMapstructAndServiceClassEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFieldTestMapstructAndServiceClassEntity = fieldTestMapstructAndServiceClassEntityMapper.toEntity(
            returnedFieldTestMapstructAndServiceClassEntityDTO
        );
        assertFieldTestMapstructAndServiceClassEntityUpdatableFieldsEquals(
            returnedFieldTestMapstructAndServiceClassEntity,
            getPersistedFieldTestMapstructAndServiceClassEntity(returnedFieldTestMapstructAndServiceClassEntity)
        );

        insertedFieldTestMapstructAndServiceClassEntity = returnedFieldTestMapstructAndServiceClassEntity;
    }

    @Test
    void createFieldTestMapstructAndServiceClassEntityWithExistingId() throws Exception {
        // Create the FieldTestMapstructAndServiceClassEntity with an existing ID
        fieldTestMapstructAndServiceClassEntity.setId(1L);
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestMapstructAndServiceClassEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkStringRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setStringRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIntegerRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setIntegerRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLongRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setLongRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFloatRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setFloatRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDoubleRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setDoubleRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkBigDecimalRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setBigDecimalRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLocalDateRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setLocalDateRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkInstanteRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setInstanteRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkZonedDateTimeRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setZonedDateTimeRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLocalTimeRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setLocalTimeRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDurationRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setDurationRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkBooleanRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setBooleanRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEnumRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setEnumRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUuidRequiredEvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldTestMapstructAndServiceClassEntity.setUuidRequiredEva(null);

        // Create the FieldTestMapstructAndServiceClassEntity, which fails.
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFieldTestMapstructAndServiceClassEntitiesAsStream() {
        // Initialize the database
        fieldTestMapstructAndServiceClassEntityRepository.save(fieldTestMapstructAndServiceClassEntity).block();

        List<FieldTestMapstructAndServiceClassEntity> fieldTestMapstructAndServiceClassEntityList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(FieldTestMapstructAndServiceClassEntityDTO.class)
            .getResponseBody()
            .map(fieldTestMapstructAndServiceClassEntityMapper::toEntity)
            .filter(fieldTestMapstructAndServiceClassEntity::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(fieldTestMapstructAndServiceClassEntityList).isNotNull();
        assertThat(fieldTestMapstructAndServiceClassEntityList).hasSize(1);
        FieldTestMapstructAndServiceClassEntity testFieldTestMapstructAndServiceClassEntity =
            fieldTestMapstructAndServiceClassEntityList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertFieldTestMapstructAndServiceClassEntityAllPropertiesEquals(fieldTestMapstructAndServiceClassEntity, testFieldTestMapstructAndServiceClassEntity);
        assertFieldTestMapstructAndServiceClassEntityUpdatableFieldsEquals(
            fieldTestMapstructAndServiceClassEntity,
            testFieldTestMapstructAndServiceClassEntity
        );
    }

    @Test
    void getAllFieldTestMapstructAndServiceClassEntities() {
        // Initialize the database
        insertedFieldTestMapstructAndServiceClassEntity = fieldTestMapstructAndServiceClassEntityRepository
            .save(fieldTestMapstructAndServiceClassEntity)
            .block();

        // Get all the fieldTestMapstructAndServiceClassEntityList
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
            .value(hasItem(fieldTestMapstructAndServiceClassEntity.getId().intValue()))
            .jsonPath("$.[*].stringEva")
            .value(hasItem(DEFAULT_STRING_EVA))
            .jsonPath("$.[*].stringRequiredEva")
            .value(hasItem(DEFAULT_STRING_REQUIRED_EVA))
            .jsonPath("$.[*].stringMinlengthEva")
            .value(hasItem(DEFAULT_STRING_MINLENGTH_EVA))
            .jsonPath("$.[*].stringMaxlengthEva")
            .value(hasItem(DEFAULT_STRING_MAXLENGTH_EVA))
            .jsonPath("$.[*].stringPatternEva")
            .value(hasItem(DEFAULT_STRING_PATTERN_EVA))
            .jsonPath("$.[*].integerEva")
            .value(hasItem(DEFAULT_INTEGER_EVA))
            .jsonPath("$.[*].integerRequiredEva")
            .value(hasItem(DEFAULT_INTEGER_REQUIRED_EVA))
            .jsonPath("$.[*].integerMinEva")
            .value(hasItem(DEFAULT_INTEGER_MIN_EVA))
            .jsonPath("$.[*].integerMaxEva")
            .value(hasItem(DEFAULT_INTEGER_MAX_EVA))
            .jsonPath("$.[*].longEva")
            .value(hasItem(DEFAULT_LONG_EVA.intValue()))
            .jsonPath("$.[*].longRequiredEva")
            .value(hasItem(DEFAULT_LONG_REQUIRED_EVA.intValue()))
            .jsonPath("$.[*].longMinEva")
            .value(hasItem(DEFAULT_LONG_MIN_EVA.intValue()))
            .jsonPath("$.[*].longMaxEva")
            .value(hasItem(DEFAULT_LONG_MAX_EVA.intValue()))
            .jsonPath("$.[*].floatEva")
            .value(hasItem(DEFAULT_FLOAT_EVA.doubleValue()))
            .jsonPath("$.[*].floatRequiredEva")
            .value(hasItem(DEFAULT_FLOAT_REQUIRED_EVA.doubleValue()))
            .jsonPath("$.[*].floatMinEva")
            .value(hasItem(DEFAULT_FLOAT_MIN_EVA.doubleValue()))
            .jsonPath("$.[*].floatMaxEva")
            .value(hasItem(DEFAULT_FLOAT_MAX_EVA.doubleValue()))
            .jsonPath("$.[*].doubleRequiredEva")
            .value(hasItem(DEFAULT_DOUBLE_REQUIRED_EVA))
            .jsonPath("$.[*].doubleMinEva")
            .value(hasItem(DEFAULT_DOUBLE_MIN_EVA))
            .jsonPath("$.[*].doubleMaxEva")
            .value(hasItem(DEFAULT_DOUBLE_MAX_EVA))
            .jsonPath("$.[*].bigDecimalRequiredEva")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_REQUIRED_EVA)))
            .jsonPath("$.[*].bigDecimalMinEva")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_MIN_EVA)))
            .jsonPath("$.[*].bigDecimalMaxEva")
            .value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_MAX_EVA)))
            .jsonPath("$.[*].localDateEva")
            .value(hasItem(DEFAULT_LOCAL_DATE_EVA.toString()))
            .jsonPath("$.[*].localDateRequiredEva")
            .value(hasItem(DEFAULT_LOCAL_DATE_REQUIRED_EVA.toString()))
            .jsonPath("$.[*].instantEva")
            .value(hasItem(DEFAULT_INSTANT_EVA.toString()))
            .jsonPath("$.[*].instanteRequiredEva")
            .value(hasItem(DEFAULT_INSTANTE_REQUIRED_EVA.toString()))
            .jsonPath("$.[*].zonedDateTimeEva")
            .value(hasItem(sameInstant(DEFAULT_ZONED_DATE_TIME_EVA)))
            .jsonPath("$.[*].zonedDateTimeRequiredEva")
            .value(hasItem(sameInstant(DEFAULT_ZONED_DATE_TIME_REQUIRED_EVA)))
            .jsonPath("$.[*].localTimeEva")
            .value(hasItem(DEFAULT_LOCAL_TIME_EVA.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.[*].localTimeRequiredEva")
            .value(hasItem(DEFAULT_LOCAL_TIME_REQUIRED_EVA.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.[*].durationEva")
            .value(hasItem(DEFAULT_DURATION_EVA.toString()))
            .jsonPath("$.[*].durationRequiredEva")
            .value(hasItem(DEFAULT_DURATION_REQUIRED_EVA.toString()))
            .jsonPath("$.[*].booleanEva")
            .value(hasItem(DEFAULT_BOOLEAN_EVA))
            .jsonPath("$.[*].booleanRequiredEva")
            .value(hasItem(DEFAULT_BOOLEAN_REQUIRED_EVA))
            .jsonPath("$.[*].enumEva")
            .value(hasItem(DEFAULT_ENUM_EVA.toString()))
            .jsonPath("$.[*].enumRequiredEva")
            .value(hasItem(DEFAULT_ENUM_REQUIRED_EVA.toString()))
            .jsonPath("$.[*].uuidEva")
            .value(hasItem(DEFAULT_UUID_EVA.toString()))
            .jsonPath("$.[*].uuidRequiredEva")
            .value(hasItem(DEFAULT_UUID_REQUIRED_EVA.toString()))
            .jsonPath("$.[*].byteImageEvaContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_EVA_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageEva")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_EVA)))
            .jsonPath("$.[*].byteImageRequiredEvaContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_REQUIRED_EVA_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageRequiredEva")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_REQUIRED_EVA)))
            .jsonPath("$.[*].byteImageMinbytesEvaContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_MINBYTES_EVA_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageMinbytesEva")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MINBYTES_EVA)))
            .jsonPath("$.[*].byteImageMaxbytesEvaContentType")
            .value(hasItem(DEFAULT_BYTE_IMAGE_MAXBYTES_EVA_CONTENT_TYPE))
            .jsonPath("$.[*].byteImageMaxbytesEva")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MAXBYTES_EVA)))
            .jsonPath("$.[*].byteAnyEvaContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_EVA_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyEva")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_EVA)))
            .jsonPath("$.[*].byteAnyRequiredEvaContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_REQUIRED_EVA_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyRequiredEva")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_REQUIRED_EVA)))
            .jsonPath("$.[*].byteAnyMinbytesEvaContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_MINBYTES_EVA_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyMinbytesEva")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MINBYTES_EVA)))
            .jsonPath("$.[*].byteAnyMaxbytesEvaContentType")
            .value(hasItem(DEFAULT_BYTE_ANY_MAXBYTES_EVA_CONTENT_TYPE))
            .jsonPath("$.[*].byteAnyMaxbytesEva")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MAXBYTES_EVA)))
            .jsonPath("$.[*].byteTextEva")
            .value(hasItem(DEFAULT_BYTE_TEXT_EVA))
            .jsonPath("$.[*].byteTextRequiredEva")
            .value(hasItem(DEFAULT_BYTE_TEXT_REQUIRED_EVA));
    }

    @Test
    void getFieldTestMapstructAndServiceClassEntity() {
        // Initialize the database
        insertedFieldTestMapstructAndServiceClassEntity = fieldTestMapstructAndServiceClassEntityRepository
            .save(fieldTestMapstructAndServiceClassEntity)
            .block();

        // Get the fieldTestMapstructAndServiceClassEntity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, fieldTestMapstructAndServiceClassEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(fieldTestMapstructAndServiceClassEntity.getId().intValue()))
            .jsonPath("$.stringEva")
            .value(is(DEFAULT_STRING_EVA))
            .jsonPath("$.stringRequiredEva")
            .value(is(DEFAULT_STRING_REQUIRED_EVA))
            .jsonPath("$.stringMinlengthEva")
            .value(is(DEFAULT_STRING_MINLENGTH_EVA))
            .jsonPath("$.stringMaxlengthEva")
            .value(is(DEFAULT_STRING_MAXLENGTH_EVA))
            .jsonPath("$.stringPatternEva")
            .value(is(DEFAULT_STRING_PATTERN_EVA))
            .jsonPath("$.integerEva")
            .value(is(DEFAULT_INTEGER_EVA))
            .jsonPath("$.integerRequiredEva")
            .value(is(DEFAULT_INTEGER_REQUIRED_EVA))
            .jsonPath("$.integerMinEva")
            .value(is(DEFAULT_INTEGER_MIN_EVA))
            .jsonPath("$.integerMaxEva")
            .value(is(DEFAULT_INTEGER_MAX_EVA))
            .jsonPath("$.longEva")
            .value(is(DEFAULT_LONG_EVA.intValue()))
            .jsonPath("$.longRequiredEva")
            .value(is(DEFAULT_LONG_REQUIRED_EVA.intValue()))
            .jsonPath("$.longMinEva")
            .value(is(DEFAULT_LONG_MIN_EVA.intValue()))
            .jsonPath("$.longMaxEva")
            .value(is(DEFAULT_LONG_MAX_EVA.intValue()))
            .jsonPath("$.floatEva")
            .value(is(DEFAULT_FLOAT_EVA.doubleValue()))
            .jsonPath("$.floatRequiredEva")
            .value(is(DEFAULT_FLOAT_REQUIRED_EVA.doubleValue()))
            .jsonPath("$.floatMinEva")
            .value(is(DEFAULT_FLOAT_MIN_EVA.doubleValue()))
            .jsonPath("$.floatMaxEva")
            .value(is(DEFAULT_FLOAT_MAX_EVA.doubleValue()))
            .jsonPath("$.doubleRequiredEva")
            .value(is(DEFAULT_DOUBLE_REQUIRED_EVA))
            .jsonPath("$.doubleMinEva")
            .value(is(DEFAULT_DOUBLE_MIN_EVA))
            .jsonPath("$.doubleMaxEva")
            .value(is(DEFAULT_DOUBLE_MAX_EVA))
            .jsonPath("$.bigDecimalRequiredEva")
            .value(is(sameNumber(DEFAULT_BIG_DECIMAL_REQUIRED_EVA)))
            .jsonPath("$.bigDecimalMinEva")
            .value(is(sameNumber(DEFAULT_BIG_DECIMAL_MIN_EVA)))
            .jsonPath("$.bigDecimalMaxEva")
            .value(is(sameNumber(DEFAULT_BIG_DECIMAL_MAX_EVA)))
            .jsonPath("$.localDateEva")
            .value(is(DEFAULT_LOCAL_DATE_EVA.toString()))
            .jsonPath("$.localDateRequiredEva")
            .value(is(DEFAULT_LOCAL_DATE_REQUIRED_EVA.toString()))
            .jsonPath("$.instantEva")
            .value(is(DEFAULT_INSTANT_EVA.toString()))
            .jsonPath("$.instanteRequiredEva")
            .value(is(DEFAULT_INSTANTE_REQUIRED_EVA.toString()))
            .jsonPath("$.zonedDateTimeEva")
            .value(is(sameInstant(DEFAULT_ZONED_DATE_TIME_EVA)))
            .jsonPath("$.zonedDateTimeRequiredEva")
            .value(is(sameInstant(DEFAULT_ZONED_DATE_TIME_REQUIRED_EVA)))
            .jsonPath("$.localTimeEva")
            .value(is(DEFAULT_LOCAL_TIME_EVA.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.localTimeRequiredEva")
            .value(is(DEFAULT_LOCAL_TIME_REQUIRED_EVA.format(LOCAL_DATE_TIME_FORMAT)))
            .jsonPath("$.durationEva")
            .value(is(DEFAULT_DURATION_EVA.toString()))
            .jsonPath("$.durationRequiredEva")
            .value(is(DEFAULT_DURATION_REQUIRED_EVA.toString()))
            .jsonPath("$.booleanEva")
            .value(is(DEFAULT_BOOLEAN_EVA))
            .jsonPath("$.booleanRequiredEva")
            .value(is(DEFAULT_BOOLEAN_REQUIRED_EVA))
            .jsonPath("$.enumEva")
            .value(is(DEFAULT_ENUM_EVA.toString()))
            .jsonPath("$.enumRequiredEva")
            .value(is(DEFAULT_ENUM_REQUIRED_EVA.toString()))
            .jsonPath("$.uuidEva")
            .value(is(DEFAULT_UUID_EVA.toString()))
            .jsonPath("$.uuidRequiredEva")
            .value(is(DEFAULT_UUID_REQUIRED_EVA.toString()))
            .jsonPath("$.byteImageEvaContentType")
            .value(is(DEFAULT_BYTE_IMAGE_EVA_CONTENT_TYPE))
            .jsonPath("$.byteImageEva")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_EVA)))
            .jsonPath("$.byteImageRequiredEvaContentType")
            .value(is(DEFAULT_BYTE_IMAGE_REQUIRED_EVA_CONTENT_TYPE))
            .jsonPath("$.byteImageRequiredEva")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_REQUIRED_EVA)))
            .jsonPath("$.byteImageMinbytesEvaContentType")
            .value(is(DEFAULT_BYTE_IMAGE_MINBYTES_EVA_CONTENT_TYPE))
            .jsonPath("$.byteImageMinbytesEva")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MINBYTES_EVA)))
            .jsonPath("$.byteImageMaxbytesEvaContentType")
            .value(is(DEFAULT_BYTE_IMAGE_MAXBYTES_EVA_CONTENT_TYPE))
            .jsonPath("$.byteImageMaxbytesEva")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_IMAGE_MAXBYTES_EVA)))
            .jsonPath("$.byteAnyEvaContentType")
            .value(is(DEFAULT_BYTE_ANY_EVA_CONTENT_TYPE))
            .jsonPath("$.byteAnyEva")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_EVA)))
            .jsonPath("$.byteAnyRequiredEvaContentType")
            .value(is(DEFAULT_BYTE_ANY_REQUIRED_EVA_CONTENT_TYPE))
            .jsonPath("$.byteAnyRequiredEva")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_REQUIRED_EVA)))
            .jsonPath("$.byteAnyMinbytesEvaContentType")
            .value(is(DEFAULT_BYTE_ANY_MINBYTES_EVA_CONTENT_TYPE))
            .jsonPath("$.byteAnyMinbytesEva")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MINBYTES_EVA)))
            .jsonPath("$.byteAnyMaxbytesEvaContentType")
            .value(is(DEFAULT_BYTE_ANY_MAXBYTES_EVA_CONTENT_TYPE))
            .jsonPath("$.byteAnyMaxbytesEva")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BYTE_ANY_MAXBYTES_EVA)))
            .jsonPath("$.byteTextEva")
            .value(is(DEFAULT_BYTE_TEXT_EVA))
            .jsonPath("$.byteTextRequiredEva")
            .value(is(DEFAULT_BYTE_TEXT_REQUIRED_EVA));
    }

    @Test
    void getNonExistingFieldTestMapstructAndServiceClassEntity() {
        // Get the fieldTestMapstructAndServiceClassEntity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFieldTestMapstructAndServiceClassEntity() throws Exception {
        // Initialize the database
        insertedFieldTestMapstructAndServiceClassEntity = fieldTestMapstructAndServiceClassEntityRepository
            .save(fieldTestMapstructAndServiceClassEntity)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestMapstructAndServiceClassEntity
        FieldTestMapstructAndServiceClassEntity updatedFieldTestMapstructAndServiceClassEntity =
            fieldTestMapstructAndServiceClassEntityRepository.findById(fieldTestMapstructAndServiceClassEntity.getId()).block();
        updatedFieldTestMapstructAndServiceClassEntity
            .stringEva(UPDATED_STRING_EVA)
            .stringRequiredEva(UPDATED_STRING_REQUIRED_EVA)
            .stringMinlengthEva(UPDATED_STRING_MINLENGTH_EVA)
            .stringMaxlengthEva(UPDATED_STRING_MAXLENGTH_EVA)
            .stringPatternEva(UPDATED_STRING_PATTERN_EVA)
            .integerEva(UPDATED_INTEGER_EVA)
            .integerRequiredEva(UPDATED_INTEGER_REQUIRED_EVA)
            .integerMinEva(UPDATED_INTEGER_MIN_EVA)
            .integerMaxEva(UPDATED_INTEGER_MAX_EVA)
            .longEva(UPDATED_LONG_EVA)
            .longRequiredEva(UPDATED_LONG_REQUIRED_EVA)
            .longMinEva(UPDATED_LONG_MIN_EVA)
            .longMaxEva(UPDATED_LONG_MAX_EVA)
            .floatEva(UPDATED_FLOAT_EVA)
            .floatRequiredEva(UPDATED_FLOAT_REQUIRED_EVA)
            .floatMinEva(UPDATED_FLOAT_MIN_EVA)
            .floatMaxEva(UPDATED_FLOAT_MAX_EVA)
            .doubleRequiredEva(UPDATED_DOUBLE_REQUIRED_EVA)
            .doubleMinEva(UPDATED_DOUBLE_MIN_EVA)
            .doubleMaxEva(UPDATED_DOUBLE_MAX_EVA)
            .bigDecimalRequiredEva(UPDATED_BIG_DECIMAL_REQUIRED_EVA)
            .bigDecimalMinEva(UPDATED_BIG_DECIMAL_MIN_EVA)
            .bigDecimalMaxEva(UPDATED_BIG_DECIMAL_MAX_EVA)
            .localDateEva(UPDATED_LOCAL_DATE_EVA)
            .localDateRequiredEva(UPDATED_LOCAL_DATE_REQUIRED_EVA)
            .instantEva(UPDATED_INSTANT_EVA)
            .instanteRequiredEva(UPDATED_INSTANTE_REQUIRED_EVA)
            .zonedDateTimeEva(UPDATED_ZONED_DATE_TIME_EVA)
            .zonedDateTimeRequiredEva(UPDATED_ZONED_DATE_TIME_REQUIRED_EVA)
            .localTimeEva(UPDATED_LOCAL_TIME_EVA)
            .localTimeRequiredEva(UPDATED_LOCAL_TIME_REQUIRED_EVA)
            .durationEva(UPDATED_DURATION_EVA)
            .durationRequiredEva(UPDATED_DURATION_REQUIRED_EVA)
            .booleanEva(UPDATED_BOOLEAN_EVA)
            .booleanRequiredEva(UPDATED_BOOLEAN_REQUIRED_EVA)
            .enumEva(UPDATED_ENUM_EVA)
            .enumRequiredEva(UPDATED_ENUM_REQUIRED_EVA)
            .uuidEva(UPDATED_UUID_EVA)
            .uuidRequiredEva(UPDATED_UUID_REQUIRED_EVA)
            .byteImageEva(UPDATED_BYTE_IMAGE_EVA)
            .byteImageEvaContentType(UPDATED_BYTE_IMAGE_EVA_CONTENT_TYPE)
            .byteImageRequiredEva(UPDATED_BYTE_IMAGE_REQUIRED_EVA)
            .byteImageRequiredEvaContentType(UPDATED_BYTE_IMAGE_REQUIRED_EVA_CONTENT_TYPE)
            .byteImageMinbytesEva(UPDATED_BYTE_IMAGE_MINBYTES_EVA)
            .byteImageMinbytesEvaContentType(UPDATED_BYTE_IMAGE_MINBYTES_EVA_CONTENT_TYPE)
            .byteImageMaxbytesEva(UPDATED_BYTE_IMAGE_MAXBYTES_EVA)
            .byteImageMaxbytesEvaContentType(UPDATED_BYTE_IMAGE_MAXBYTES_EVA_CONTENT_TYPE)
            .byteAnyEva(UPDATED_BYTE_ANY_EVA)
            .byteAnyEvaContentType(UPDATED_BYTE_ANY_EVA_CONTENT_TYPE)
            .byteAnyRequiredEva(UPDATED_BYTE_ANY_REQUIRED_EVA)
            .byteAnyRequiredEvaContentType(UPDATED_BYTE_ANY_REQUIRED_EVA_CONTENT_TYPE)
            .byteAnyMinbytesEva(UPDATED_BYTE_ANY_MINBYTES_EVA)
            .byteAnyMinbytesEvaContentType(UPDATED_BYTE_ANY_MINBYTES_EVA_CONTENT_TYPE)
            .byteAnyMaxbytesEva(UPDATED_BYTE_ANY_MAXBYTES_EVA)
            .byteAnyMaxbytesEvaContentType(UPDATED_BYTE_ANY_MAXBYTES_EVA_CONTENT_TYPE)
            .byteTextEva(UPDATED_BYTE_TEXT_EVA)
            .byteTextRequiredEva(UPDATED_BYTE_TEXT_REQUIRED_EVA);
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(updatedFieldTestMapstructAndServiceClassEntity);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fieldTestMapstructAndServiceClassEntityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestMapstructAndServiceClassEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFieldTestMapstructAndServiceClassEntityToMatchAllProperties(updatedFieldTestMapstructAndServiceClassEntity);
    }

    @Test
    void putNonExistingFieldTestMapstructAndServiceClassEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestMapstructAndServiceClassEntity.setId(longCount.incrementAndGet());

        // Create the FieldTestMapstructAndServiceClassEntity
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fieldTestMapstructAndServiceClassEntityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestMapstructAndServiceClassEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFieldTestMapstructAndServiceClassEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestMapstructAndServiceClassEntity.setId(longCount.incrementAndGet());

        // Create the FieldTestMapstructAndServiceClassEntity
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestMapstructAndServiceClassEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFieldTestMapstructAndServiceClassEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestMapstructAndServiceClassEntity.setId(longCount.incrementAndGet());

        // Create the FieldTestMapstructAndServiceClassEntity
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FieldTestMapstructAndServiceClassEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFieldTestMapstructAndServiceClassEntityWithPatch() throws Exception {
        // Initialize the database
        insertedFieldTestMapstructAndServiceClassEntity = fieldTestMapstructAndServiceClassEntityRepository
            .save(fieldTestMapstructAndServiceClassEntity)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestMapstructAndServiceClassEntity using partial update
        FieldTestMapstructAndServiceClassEntity partialUpdatedFieldTestMapstructAndServiceClassEntity =
            new FieldTestMapstructAndServiceClassEntity();
        partialUpdatedFieldTestMapstructAndServiceClassEntity.setId(fieldTestMapstructAndServiceClassEntity.getId());

        partialUpdatedFieldTestMapstructAndServiceClassEntity
            .stringEva(UPDATED_STRING_EVA)
            .stringRequiredEva(UPDATED_STRING_REQUIRED_EVA)
            .stringMinlengthEva(UPDATED_STRING_MINLENGTH_EVA)
            .stringMaxlengthEva(UPDATED_STRING_MAXLENGTH_EVA)
            .integerMinEva(UPDATED_INTEGER_MIN_EVA)
            .longEva(UPDATED_LONG_EVA)
            .longMaxEva(UPDATED_LONG_MAX_EVA)
            .floatEva(UPDATED_FLOAT_EVA)
            .floatMinEva(UPDATED_FLOAT_MIN_EVA)
            .doubleRequiredEva(UPDATED_DOUBLE_REQUIRED_EVA)
            .doubleMinEva(UPDATED_DOUBLE_MIN_EVA)
            .doubleMaxEva(UPDATED_DOUBLE_MAX_EVA)
            .bigDecimalRequiredEva(UPDATED_BIG_DECIMAL_REQUIRED_EVA)
            .bigDecimalMinEva(UPDATED_BIG_DECIMAL_MIN_EVA)
            .bigDecimalMaxEva(UPDATED_BIG_DECIMAL_MAX_EVA)
            .localDateRequiredEva(UPDATED_LOCAL_DATE_REQUIRED_EVA)
            .instantEva(UPDATED_INSTANT_EVA)
            .zonedDateTimeEva(UPDATED_ZONED_DATE_TIME_EVA)
            .durationEva(UPDATED_DURATION_EVA)
            .durationRequiredEva(UPDATED_DURATION_REQUIRED_EVA)
            .byteImageEva(UPDATED_BYTE_IMAGE_EVA)
            .byteImageEvaContentType(UPDATED_BYTE_IMAGE_EVA_CONTENT_TYPE)
            .byteImageMaxbytesEva(UPDATED_BYTE_IMAGE_MAXBYTES_EVA)
            .byteImageMaxbytesEvaContentType(UPDATED_BYTE_IMAGE_MAXBYTES_EVA_CONTENT_TYPE)
            .byteAnyEva(UPDATED_BYTE_ANY_EVA)
            .byteAnyEvaContentType(UPDATED_BYTE_ANY_EVA_CONTENT_TYPE)
            .byteAnyRequiredEva(UPDATED_BYTE_ANY_REQUIRED_EVA)
            .byteAnyRequiredEvaContentType(UPDATED_BYTE_ANY_REQUIRED_EVA_CONTENT_TYPE)
            .byteAnyMinbytesEva(UPDATED_BYTE_ANY_MINBYTES_EVA)
            .byteAnyMinbytesEvaContentType(UPDATED_BYTE_ANY_MINBYTES_EVA_CONTENT_TYPE)
            .byteTextEva(UPDATED_BYTE_TEXT_EVA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFieldTestMapstructAndServiceClassEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFieldTestMapstructAndServiceClassEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestMapstructAndServiceClassEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFieldTestMapstructAndServiceClassEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFieldTestMapstructAndServiceClassEntity, fieldTestMapstructAndServiceClassEntity),
            getPersistedFieldTestMapstructAndServiceClassEntity(fieldTestMapstructAndServiceClassEntity)
        );
    }

    @Test
    void fullUpdateFieldTestMapstructAndServiceClassEntityWithPatch() throws Exception {
        // Initialize the database
        insertedFieldTestMapstructAndServiceClassEntity = fieldTestMapstructAndServiceClassEntityRepository
            .save(fieldTestMapstructAndServiceClassEntity)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestMapstructAndServiceClassEntity using partial update
        FieldTestMapstructAndServiceClassEntity partialUpdatedFieldTestMapstructAndServiceClassEntity =
            new FieldTestMapstructAndServiceClassEntity();
        partialUpdatedFieldTestMapstructAndServiceClassEntity.setId(fieldTestMapstructAndServiceClassEntity.getId());

        partialUpdatedFieldTestMapstructAndServiceClassEntity
            .stringEva(UPDATED_STRING_EVA)
            .stringRequiredEva(UPDATED_STRING_REQUIRED_EVA)
            .stringMinlengthEva(UPDATED_STRING_MINLENGTH_EVA)
            .stringMaxlengthEva(UPDATED_STRING_MAXLENGTH_EVA)
            .stringPatternEva(UPDATED_STRING_PATTERN_EVA)
            .integerEva(UPDATED_INTEGER_EVA)
            .integerRequiredEva(UPDATED_INTEGER_REQUIRED_EVA)
            .integerMinEva(UPDATED_INTEGER_MIN_EVA)
            .integerMaxEva(UPDATED_INTEGER_MAX_EVA)
            .longEva(UPDATED_LONG_EVA)
            .longRequiredEva(UPDATED_LONG_REQUIRED_EVA)
            .longMinEva(UPDATED_LONG_MIN_EVA)
            .longMaxEva(UPDATED_LONG_MAX_EVA)
            .floatEva(UPDATED_FLOAT_EVA)
            .floatRequiredEva(UPDATED_FLOAT_REQUIRED_EVA)
            .floatMinEva(UPDATED_FLOAT_MIN_EVA)
            .floatMaxEva(UPDATED_FLOAT_MAX_EVA)
            .doubleRequiredEva(UPDATED_DOUBLE_REQUIRED_EVA)
            .doubleMinEva(UPDATED_DOUBLE_MIN_EVA)
            .doubleMaxEva(UPDATED_DOUBLE_MAX_EVA)
            .bigDecimalRequiredEva(UPDATED_BIG_DECIMAL_REQUIRED_EVA)
            .bigDecimalMinEva(UPDATED_BIG_DECIMAL_MIN_EVA)
            .bigDecimalMaxEva(UPDATED_BIG_DECIMAL_MAX_EVA)
            .localDateEva(UPDATED_LOCAL_DATE_EVA)
            .localDateRequiredEva(UPDATED_LOCAL_DATE_REQUIRED_EVA)
            .instantEva(UPDATED_INSTANT_EVA)
            .instanteRequiredEva(UPDATED_INSTANTE_REQUIRED_EVA)
            .zonedDateTimeEva(UPDATED_ZONED_DATE_TIME_EVA)
            .zonedDateTimeRequiredEva(UPDATED_ZONED_DATE_TIME_REQUIRED_EVA)
            .localTimeEva(UPDATED_LOCAL_TIME_EVA)
            .localTimeRequiredEva(UPDATED_LOCAL_TIME_REQUIRED_EVA)
            .durationEva(UPDATED_DURATION_EVA)
            .durationRequiredEva(UPDATED_DURATION_REQUIRED_EVA)
            .booleanEva(UPDATED_BOOLEAN_EVA)
            .booleanRequiredEva(UPDATED_BOOLEAN_REQUIRED_EVA)
            .enumEva(UPDATED_ENUM_EVA)
            .enumRequiredEva(UPDATED_ENUM_REQUIRED_EVA)
            .uuidEva(UPDATED_UUID_EVA)
            .uuidRequiredEva(UPDATED_UUID_REQUIRED_EVA)
            .byteImageEva(UPDATED_BYTE_IMAGE_EVA)
            .byteImageEvaContentType(UPDATED_BYTE_IMAGE_EVA_CONTENT_TYPE)
            .byteImageRequiredEva(UPDATED_BYTE_IMAGE_REQUIRED_EVA)
            .byteImageRequiredEvaContentType(UPDATED_BYTE_IMAGE_REQUIRED_EVA_CONTENT_TYPE)
            .byteImageMinbytesEva(UPDATED_BYTE_IMAGE_MINBYTES_EVA)
            .byteImageMinbytesEvaContentType(UPDATED_BYTE_IMAGE_MINBYTES_EVA_CONTENT_TYPE)
            .byteImageMaxbytesEva(UPDATED_BYTE_IMAGE_MAXBYTES_EVA)
            .byteImageMaxbytesEvaContentType(UPDATED_BYTE_IMAGE_MAXBYTES_EVA_CONTENT_TYPE)
            .byteAnyEva(UPDATED_BYTE_ANY_EVA)
            .byteAnyEvaContentType(UPDATED_BYTE_ANY_EVA_CONTENT_TYPE)
            .byteAnyRequiredEva(UPDATED_BYTE_ANY_REQUIRED_EVA)
            .byteAnyRequiredEvaContentType(UPDATED_BYTE_ANY_REQUIRED_EVA_CONTENT_TYPE)
            .byteAnyMinbytesEva(UPDATED_BYTE_ANY_MINBYTES_EVA)
            .byteAnyMinbytesEvaContentType(UPDATED_BYTE_ANY_MINBYTES_EVA_CONTENT_TYPE)
            .byteAnyMaxbytesEva(UPDATED_BYTE_ANY_MAXBYTES_EVA)
            .byteAnyMaxbytesEvaContentType(UPDATED_BYTE_ANY_MAXBYTES_EVA_CONTENT_TYPE)
            .byteTextEva(UPDATED_BYTE_TEXT_EVA)
            .byteTextRequiredEva(UPDATED_BYTE_TEXT_REQUIRED_EVA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFieldTestMapstructAndServiceClassEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFieldTestMapstructAndServiceClassEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestMapstructAndServiceClassEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFieldTestMapstructAndServiceClassEntityUpdatableFieldsEquals(
            partialUpdatedFieldTestMapstructAndServiceClassEntity,
            getPersistedFieldTestMapstructAndServiceClassEntity(partialUpdatedFieldTestMapstructAndServiceClassEntity)
        );
    }

    @Test
    void patchNonExistingFieldTestMapstructAndServiceClassEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestMapstructAndServiceClassEntity.setId(longCount.incrementAndGet());

        // Create the FieldTestMapstructAndServiceClassEntity
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, fieldTestMapstructAndServiceClassEntityDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestMapstructAndServiceClassEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFieldTestMapstructAndServiceClassEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestMapstructAndServiceClassEntity.setId(longCount.incrementAndGet());

        // Create the FieldTestMapstructAndServiceClassEntity
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestMapstructAndServiceClassEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFieldTestMapstructAndServiceClassEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestMapstructAndServiceClassEntity.setId(longCount.incrementAndGet());

        // Create the FieldTestMapstructAndServiceClassEntity
        FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityMapper.toDto(fieldTestMapstructAndServiceClassEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestMapstructAndServiceClassEntityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FieldTestMapstructAndServiceClassEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFieldTestMapstructAndServiceClassEntity() {
        // Initialize the database
        insertedFieldTestMapstructAndServiceClassEntity = fieldTestMapstructAndServiceClassEntityRepository
            .save(fieldTestMapstructAndServiceClassEntity)
            .block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fieldTestMapstructAndServiceClassEntity
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, fieldTestMapstructAndServiceClassEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fieldTestMapstructAndServiceClassEntityRepository.count().block();
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

    protected FieldTestMapstructAndServiceClassEntity getPersistedFieldTestMapstructAndServiceClassEntity(
        FieldTestMapstructAndServiceClassEntity fieldTestMapstructAndServiceClassEntity
    ) {
        return fieldTestMapstructAndServiceClassEntityRepository.findById(fieldTestMapstructAndServiceClassEntity.getId()).block();
    }

    protected void assertPersistedFieldTestMapstructAndServiceClassEntityToMatchAllProperties(
        FieldTestMapstructAndServiceClassEntity expectedFieldTestMapstructAndServiceClassEntity
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFieldTestMapstructAndServiceClassEntityAllPropertiesEquals(expectedFieldTestMapstructAndServiceClassEntity, getPersistedFieldTestMapstructAndServiceClassEntity(expectedFieldTestMapstructAndServiceClassEntity));
        assertFieldTestMapstructAndServiceClassEntityUpdatableFieldsEquals(
            expectedFieldTestMapstructAndServiceClassEntity,
            getPersistedFieldTestMapstructAndServiceClassEntity(expectedFieldTestMapstructAndServiceClassEntity)
        );
    }

    protected void assertPersistedFieldTestMapstructAndServiceClassEntityToMatchUpdatableProperties(
        FieldTestMapstructAndServiceClassEntity expectedFieldTestMapstructAndServiceClassEntity
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFieldTestMapstructAndServiceClassEntityAllUpdatablePropertiesEquals(expectedFieldTestMapstructAndServiceClassEntity, getPersistedFieldTestMapstructAndServiceClassEntity(expectedFieldTestMapstructAndServiceClassEntity));
        assertFieldTestMapstructAndServiceClassEntityUpdatableFieldsEquals(
            expectedFieldTestMapstructAndServiceClassEntity,
            getPersistedFieldTestMapstructAndServiceClassEntity(expectedFieldTestMapstructAndServiceClassEntity)
        );
    }
}
