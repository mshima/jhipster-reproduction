package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static tech.jhipster.sample.domain.BankAccountAsserts.*;
import static tech.jhipster.sample.web.rest.TestUtil.createUpdateProxyForBean;
import static tech.jhipster.sample.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.IntegrationTest;
import tech.jhipster.sample.domain.BankAccount;
import tech.jhipster.sample.domain.Operation;
import tech.jhipster.sample.domain.User;
import tech.jhipster.sample.domain.enumeration.BankAccountType;
import tech.jhipster.sample.repository.BankAccountRepository;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.UserRepository;
import tech.jhipster.sample.repository.UserRepository;
import tech.jhipster.sample.repository.search.BankAccountSearchRepository;
import tech.jhipster.sample.service.BankAccountService;

/**
 * Integration tests for the {@link BankAccountResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BankAccountResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final UUID DEFAULT_GUID = UUID.randomUUID();
    private static final UUID UPDATED_GUID = UUID.randomUUID();

    private static final Integer DEFAULT_BANK_NUMBER = 1;
    private static final Integer UPDATED_BANK_NUMBER = 2;
    private static final Integer SMALLER_BANK_NUMBER = 1 - 1;

    private static final Long DEFAULT_AGENCY_NUMBER = 1L;
    private static final Long UPDATED_AGENCY_NUMBER = 2L;
    private static final Long SMALLER_AGENCY_NUMBER = 1L - 1L;

    private static final Float DEFAULT_LAST_OPERATION_DURATION = 1F;
    private static final Float UPDATED_LAST_OPERATION_DURATION = 2F;
    private static final Float SMALLER_LAST_OPERATION_DURATION = 1F - 1F;

    private static final Double DEFAULT_MEAN_OPERATION_DURATION = 1D;
    private static final Double UPDATED_MEAN_OPERATION_DURATION = 2D;
    private static final Double SMALLER_MEAN_OPERATION_DURATION = 1D - 1D;

    private static final Duration DEFAULT_MEAN_QUEUE_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_MEAN_QUEUE_DURATION = Duration.ofHours(12);
    private static final Duration SMALLER_MEAN_QUEUE_DURATION = Duration.ofHours(5);

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_OPENING_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OPENING_DAY = LocalDate.parse("2020-08-04");
    private static final LocalDate SMALLER_OPENING_DAY = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_LAST_OPERATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_OPERATION_DATE = Instant.ofEpochMilli(1596513272473L);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final BankAccountType DEFAULT_ACCOUNT_TYPE = BankAccountType.CHECKING;
    private static final BankAccountType UPDATED_ACCOUNT_TYPE = BankAccountType.SAVINGS;

    private static final byte[] DEFAULT_ATTACHMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ATTACHMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ATTACHMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTACHMENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bank-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/bank-accounts/_search";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private BankAccountRepository bankAccountRepositoryMock;

    @Mock
    private BankAccountService bankAccountServiceMock;

    @Autowired
    private BankAccountSearchRepository bankAccountSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private BankAccount bankAccount;

    private BankAccount insertedBankAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createEntity() {
        return new BankAccount()
            .name(DEFAULT_NAME)
            .guid(DEFAULT_GUID)
            .bankNumber(DEFAULT_BANK_NUMBER)
            .agencyNumber(DEFAULT_AGENCY_NUMBER)
            .lastOperationDuration(DEFAULT_LAST_OPERATION_DURATION)
            .meanOperationDuration(DEFAULT_MEAN_OPERATION_DURATION)
            .meanQueueDuration(DEFAULT_MEAN_QUEUE_DURATION)
            .balance(DEFAULT_BALANCE)
            .openingDay(DEFAULT_OPENING_DAY)
            .lastOperationDate(DEFAULT_LAST_OPERATION_DATE)
            .active(DEFAULT_ACTIVE)
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .attachment(DEFAULT_ATTACHMENT)
            .attachmentContentType(DEFAULT_ATTACHMENT_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createUpdatedEntity() {
        return new BankAccount()
            .name(UPDATED_NAME)
            .guid(UPDATED_GUID)
            .bankNumber(UPDATED_BANK_NUMBER)
            .agencyNumber(UPDATED_AGENCY_NUMBER)
            .lastOperationDuration(UPDATED_LAST_OPERATION_DURATION)
            .meanOperationDuration(UPDATED_MEAN_OPERATION_DURATION)
            .meanQueueDuration(UPDATED_MEAN_QUEUE_DURATION)
            .balance(UPDATED_BALANCE)
            .openingDay(UPDATED_OPENING_DAY)
            .lastOperationDate(UPDATED_LAST_OPERATION_DATE)
            .active(UPDATED_ACTIVE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(BankAccount.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    void initTest() {
        bankAccount = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBankAccount != null) {
            bankAccountRepository.delete(insertedBankAccount).block();
            bankAccountSearchRepository.delete(insertedBankAccount).block();
            insertedBankAccount = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createBankAccount() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        // Create the BankAccount
        var returnedBankAccount = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(bankAccount))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BankAccount.class)
            .returnResult()
            .getResponseBody();

        // Validate the BankAccount in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBankAccountUpdatableFieldsEquals(returnedBankAccount, getPersistedBankAccount(returnedBankAccount));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedBankAccount = returnedBankAccount;
    }

    @Test
    void createBankAccountWithExistingId() throws Exception {
        // Create the BankAccount with an existing ID
        bankAccount.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(bankAccount))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        // set the field null
        bankAccount.setName(null);

        // Create the BankAccount, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(bankAccount))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkBalanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        // set the field null
        bankAccount.setBalance(null);

        // Create the BankAccount, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(bankAccount))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllBankAccounts() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList
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
            .value(hasItem(bankAccount.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].guid")
            .value(hasItem(DEFAULT_GUID.toString()))
            .jsonPath("$.[*].bankNumber")
            .value(hasItem(DEFAULT_BANK_NUMBER))
            .jsonPath("$.[*].agencyNumber")
            .value(hasItem(DEFAULT_AGENCY_NUMBER.intValue()))
            .jsonPath("$.[*].lastOperationDuration")
            .value(hasItem(DEFAULT_LAST_OPERATION_DURATION.doubleValue()))
            .jsonPath("$.[*].meanOperationDuration")
            .value(hasItem(DEFAULT_MEAN_OPERATION_DURATION))
            .jsonPath("$.[*].meanQueueDuration")
            .value(hasItem(DEFAULT_MEAN_QUEUE_DURATION.toString()))
            .jsonPath("$.[*].balance")
            .value(hasItem(sameNumber(DEFAULT_BALANCE)))
            .jsonPath("$.[*].openingDay")
            .value(hasItem(DEFAULT_OPENING_DAY.toString()))
            .jsonPath("$.[*].lastOperationDate")
            .value(hasItem(DEFAULT_LAST_OPERATION_DATE.toString()))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE))
            .jsonPath("$.[*].accountType")
            .value(hasItem(DEFAULT_ACCOUNT_TYPE.toString()))
            .jsonPath("$.[*].attachmentContentType")
            .value(hasItem(DEFAULT_ATTACHMENT_CONTENT_TYPE))
            .jsonPath("$.[*].attachment")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_ATTACHMENT)))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBankAccountsWithEagerRelationshipsIsEnabled() {
        when(bankAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?eagerload=true")
            .exchange()
            .expectStatus()
            .isOk();

        verify(bankAccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBankAccountsWithEagerRelationshipsIsNotEnabled() {
        when(bankAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?eagerload=false")
            .exchange()
            .expectStatus()
            .isOk();
        verify(bankAccountRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getBankAccount() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get the bankAccount
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, bankAccount.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(bankAccount.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.guid")
            .value(is(DEFAULT_GUID.toString()))
            .jsonPath("$.bankNumber")
            .value(is(DEFAULT_BANK_NUMBER))
            .jsonPath("$.agencyNumber")
            .value(is(DEFAULT_AGENCY_NUMBER.intValue()))
            .jsonPath("$.lastOperationDuration")
            .value(is(DEFAULT_LAST_OPERATION_DURATION.doubleValue()))
            .jsonPath("$.meanOperationDuration")
            .value(is(DEFAULT_MEAN_OPERATION_DURATION))
            .jsonPath("$.meanQueueDuration")
            .value(is(DEFAULT_MEAN_QUEUE_DURATION.toString()))
            .jsonPath("$.balance")
            .value(is(sameNumber(DEFAULT_BALANCE)))
            .jsonPath("$.openingDay")
            .value(is(DEFAULT_OPENING_DAY.toString()))
            .jsonPath("$.lastOperationDate")
            .value(is(DEFAULT_LAST_OPERATION_DATE.toString()))
            .jsonPath("$.active")
            .value(is(DEFAULT_ACTIVE))
            .jsonPath("$.accountType")
            .value(is(DEFAULT_ACCOUNT_TYPE.toString()))
            .jsonPath("$.attachmentContentType")
            .value(is(DEFAULT_ATTACHMENT_CONTENT_TYPE))
            .jsonPath("$.attachment")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_ATTACHMENT)))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getBankAccountsByIdFiltering() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        Long id = bankAccount.getId();

        defaultBankAccountFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBankAccountFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBankAccountFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    void getAllBankAccountsByNameIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where name equals to
        defaultBankAccountFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllBankAccountsByNameIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where name in
        defaultBankAccountFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllBankAccountsByNameIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where name is not null
        defaultBankAccountFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    void getAllBankAccountsByNameContainsSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where name contains
        defaultBankAccountFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllBankAccountsByNameNotContainsSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where name does not contain
        defaultBankAccountFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    void getAllBankAccountsByGuidIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where guid equals to
        defaultBankAccountFiltering("guid.equals=" + DEFAULT_GUID, "guid.equals=" + UPDATED_GUID);
    }

    @Test
    void getAllBankAccountsByGuidIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where guid in
        defaultBankAccountFiltering("guid.in=" + DEFAULT_GUID + "," + UPDATED_GUID, "guid.in=" + UPDATED_GUID);
    }

    @Test
    void getAllBankAccountsByGuidIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where guid is not null
        defaultBankAccountFiltering("guid.specified=true", "guid.specified=false");
    }

    @Test
    void getAllBankAccountsByBankNumberIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where bankNumber equals to
        defaultBankAccountFiltering("bankNumber.equals=" + DEFAULT_BANK_NUMBER, "bankNumber.equals=" + UPDATED_BANK_NUMBER);
    }

    @Test
    void getAllBankAccountsByBankNumberIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where bankNumber in
        defaultBankAccountFiltering(
            "bankNumber.in=" + DEFAULT_BANK_NUMBER + "," + UPDATED_BANK_NUMBER,
            "bankNumber.in=" + UPDATED_BANK_NUMBER
        );
    }

    @Test
    void getAllBankAccountsByBankNumberIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where bankNumber is not null
        defaultBankAccountFiltering("bankNumber.specified=true", "bankNumber.specified=false");
    }

    @Test
    void getAllBankAccountsByBankNumberIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where bankNumber is greater than or equal to
        defaultBankAccountFiltering(
            "bankNumber.greaterThanOrEqual=" + DEFAULT_BANK_NUMBER,
            "bankNumber.greaterThanOrEqual=" + UPDATED_BANK_NUMBER
        );
    }

    @Test
    void getAllBankAccountsByBankNumberIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where bankNumber is less than or equal to
        defaultBankAccountFiltering(
            "bankNumber.lessThanOrEqual=" + DEFAULT_BANK_NUMBER,
            "bankNumber.lessThanOrEqual=" + SMALLER_BANK_NUMBER
        );
    }

    @Test
    void getAllBankAccountsByBankNumberIsLessThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where bankNumber is less than
        defaultBankAccountFiltering("bankNumber.lessThan=" + UPDATED_BANK_NUMBER, "bankNumber.lessThan=" + DEFAULT_BANK_NUMBER);
    }

    @Test
    void getAllBankAccountsByBankNumberIsGreaterThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where bankNumber is greater than
        defaultBankAccountFiltering("bankNumber.greaterThan=" + SMALLER_BANK_NUMBER, "bankNumber.greaterThan=" + DEFAULT_BANK_NUMBER);
    }

    @Test
    void getAllBankAccountsByAgencyNumberIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where agencyNumber equals to
        defaultBankAccountFiltering("agencyNumber.equals=" + DEFAULT_AGENCY_NUMBER, "agencyNumber.equals=" + UPDATED_AGENCY_NUMBER);
    }

    @Test
    void getAllBankAccountsByAgencyNumberIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where agencyNumber in
        defaultBankAccountFiltering(
            "agencyNumber.in=" + DEFAULT_AGENCY_NUMBER + "," + UPDATED_AGENCY_NUMBER,
            "agencyNumber.in=" + UPDATED_AGENCY_NUMBER
        );
    }

    @Test
    void getAllBankAccountsByAgencyNumberIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where agencyNumber is not null
        defaultBankAccountFiltering("agencyNumber.specified=true", "agencyNumber.specified=false");
    }

    @Test
    void getAllBankAccountsByAgencyNumberIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where agencyNumber is greater than or equal to
        defaultBankAccountFiltering(
            "agencyNumber.greaterThanOrEqual=" + DEFAULT_AGENCY_NUMBER,
            "agencyNumber.greaterThanOrEqual=" + UPDATED_AGENCY_NUMBER
        );
    }

    @Test
    void getAllBankAccountsByAgencyNumberIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where agencyNumber is less than or equal to
        defaultBankAccountFiltering(
            "agencyNumber.lessThanOrEqual=" + DEFAULT_AGENCY_NUMBER,
            "agencyNumber.lessThanOrEqual=" + SMALLER_AGENCY_NUMBER
        );
    }

    @Test
    void getAllBankAccountsByAgencyNumberIsLessThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where agencyNumber is less than
        defaultBankAccountFiltering("agencyNumber.lessThan=" + UPDATED_AGENCY_NUMBER, "agencyNumber.lessThan=" + DEFAULT_AGENCY_NUMBER);
    }

    @Test
    void getAllBankAccountsByAgencyNumberIsGreaterThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where agencyNumber is greater than
        defaultBankAccountFiltering(
            "agencyNumber.greaterThan=" + SMALLER_AGENCY_NUMBER,
            "agencyNumber.greaterThan=" + DEFAULT_AGENCY_NUMBER
        );
    }

    @Test
    void getAllBankAccountsByLastOperationDurationIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where lastOperationDuration equals to
        defaultBankAccountFiltering(
            "lastOperationDuration.equals=" + DEFAULT_LAST_OPERATION_DURATION,
            "lastOperationDuration.equals=" + UPDATED_LAST_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByLastOperationDurationIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where lastOperationDuration in
        defaultBankAccountFiltering(
            "lastOperationDuration.in=" + DEFAULT_LAST_OPERATION_DURATION + "," + UPDATED_LAST_OPERATION_DURATION,
            "lastOperationDuration.in=" + UPDATED_LAST_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByLastOperationDurationIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where lastOperationDuration is not null
        defaultBankAccountFiltering("lastOperationDuration.specified=true", "lastOperationDuration.specified=false");
    }

    @Test
    void getAllBankAccountsByLastOperationDurationIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where lastOperationDuration is greater than or equal to
        defaultBankAccountFiltering(
            "lastOperationDuration.greaterThanOrEqual=" + DEFAULT_LAST_OPERATION_DURATION,
            "lastOperationDuration.greaterThanOrEqual=" + UPDATED_LAST_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByLastOperationDurationIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where lastOperationDuration is less than or equal to
        defaultBankAccountFiltering(
            "lastOperationDuration.lessThanOrEqual=" + DEFAULT_LAST_OPERATION_DURATION,
            "lastOperationDuration.lessThanOrEqual=" + SMALLER_LAST_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByLastOperationDurationIsLessThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where lastOperationDuration is less than
        defaultBankAccountFiltering(
            "lastOperationDuration.lessThan=" + UPDATED_LAST_OPERATION_DURATION,
            "lastOperationDuration.lessThan=" + DEFAULT_LAST_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByLastOperationDurationIsGreaterThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where lastOperationDuration is greater than
        defaultBankAccountFiltering(
            "lastOperationDuration.greaterThan=" + SMALLER_LAST_OPERATION_DURATION,
            "lastOperationDuration.greaterThan=" + DEFAULT_LAST_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanOperationDurationIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanOperationDuration equals to
        defaultBankAccountFiltering(
            "meanOperationDuration.equals=" + DEFAULT_MEAN_OPERATION_DURATION,
            "meanOperationDuration.equals=" + UPDATED_MEAN_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanOperationDurationIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanOperationDuration in
        defaultBankAccountFiltering(
            "meanOperationDuration.in=" + DEFAULT_MEAN_OPERATION_DURATION + "," + UPDATED_MEAN_OPERATION_DURATION,
            "meanOperationDuration.in=" + UPDATED_MEAN_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanOperationDurationIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanOperationDuration is not null
        defaultBankAccountFiltering("meanOperationDuration.specified=true", "meanOperationDuration.specified=false");
    }

    @Test
    void getAllBankAccountsByMeanOperationDurationIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanOperationDuration is greater than or equal to
        defaultBankAccountFiltering(
            "meanOperationDuration.greaterThanOrEqual=" + DEFAULT_MEAN_OPERATION_DURATION,
            "meanOperationDuration.greaterThanOrEqual=" + UPDATED_MEAN_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanOperationDurationIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanOperationDuration is less than or equal to
        defaultBankAccountFiltering(
            "meanOperationDuration.lessThanOrEqual=" + DEFAULT_MEAN_OPERATION_DURATION,
            "meanOperationDuration.lessThanOrEqual=" + SMALLER_MEAN_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanOperationDurationIsLessThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanOperationDuration is less than
        defaultBankAccountFiltering(
            "meanOperationDuration.lessThan=" + UPDATED_MEAN_OPERATION_DURATION,
            "meanOperationDuration.lessThan=" + DEFAULT_MEAN_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanOperationDurationIsGreaterThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanOperationDuration is greater than
        defaultBankAccountFiltering(
            "meanOperationDuration.greaterThan=" + SMALLER_MEAN_OPERATION_DURATION,
            "meanOperationDuration.greaterThan=" + DEFAULT_MEAN_OPERATION_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanQueueDurationIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanQueueDuration equals to
        defaultBankAccountFiltering(
            "meanQueueDuration.equals=" + DEFAULT_MEAN_QUEUE_DURATION,
            "meanQueueDuration.equals=" + UPDATED_MEAN_QUEUE_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanQueueDurationIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanQueueDuration in
        defaultBankAccountFiltering(
            "meanQueueDuration.in=" + DEFAULT_MEAN_QUEUE_DURATION + "," + UPDATED_MEAN_QUEUE_DURATION,
            "meanQueueDuration.in=" + UPDATED_MEAN_QUEUE_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanQueueDurationIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanQueueDuration is not null
        defaultBankAccountFiltering("meanQueueDuration.specified=true", "meanQueueDuration.specified=false");
    }

    @Test
    void getAllBankAccountsByMeanQueueDurationIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanQueueDuration is greater than or equal to
        defaultBankAccountFiltering(
            "meanQueueDuration.greaterThanOrEqual=" + DEFAULT_MEAN_QUEUE_DURATION,
            "meanQueueDuration.greaterThanOrEqual=" + UPDATED_MEAN_QUEUE_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanQueueDurationIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanQueueDuration is less than or equal to
        defaultBankAccountFiltering(
            "meanQueueDuration.lessThanOrEqual=" + DEFAULT_MEAN_QUEUE_DURATION,
            "meanQueueDuration.lessThanOrEqual=" + SMALLER_MEAN_QUEUE_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanQueueDurationIsLessThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanQueueDuration is less than
        defaultBankAccountFiltering(
            "meanQueueDuration.lessThan=" + UPDATED_MEAN_QUEUE_DURATION,
            "meanQueueDuration.lessThan=" + DEFAULT_MEAN_QUEUE_DURATION
        );
    }

    @Test
    void getAllBankAccountsByMeanQueueDurationIsGreaterThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where meanQueueDuration is greater than
        defaultBankAccountFiltering(
            "meanQueueDuration.greaterThan=" + SMALLER_MEAN_QUEUE_DURATION,
            "meanQueueDuration.greaterThan=" + DEFAULT_MEAN_QUEUE_DURATION
        );
    }

    @Test
    void getAllBankAccountsByBalanceIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where balance equals to
        defaultBankAccountFiltering("balance.equals=" + DEFAULT_BALANCE, "balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    void getAllBankAccountsByBalanceIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where balance in
        defaultBankAccountFiltering("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE, "balance.in=" + UPDATED_BALANCE);
    }

    @Test
    void getAllBankAccountsByBalanceIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where balance is not null
        defaultBankAccountFiltering("balance.specified=true", "balance.specified=false");
    }

    @Test
    void getAllBankAccountsByBalanceIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where balance is greater than or equal to
        defaultBankAccountFiltering("balance.greaterThanOrEqual=" + DEFAULT_BALANCE, "balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    void getAllBankAccountsByBalanceIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where balance is less than or equal to
        defaultBankAccountFiltering("balance.lessThanOrEqual=" + DEFAULT_BALANCE, "balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    void getAllBankAccountsByBalanceIsLessThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where balance is less than
        defaultBankAccountFiltering("balance.lessThan=" + UPDATED_BALANCE, "balance.lessThan=" + DEFAULT_BALANCE);
    }

    @Test
    void getAllBankAccountsByBalanceIsGreaterThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where balance is greater than
        defaultBankAccountFiltering("balance.greaterThan=" + SMALLER_BALANCE, "balance.greaterThan=" + DEFAULT_BALANCE);
    }

    @Test
    void getAllBankAccountsByOpeningDayIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where openingDay equals to
        defaultBankAccountFiltering("openingDay.equals=" + DEFAULT_OPENING_DAY, "openingDay.equals=" + UPDATED_OPENING_DAY);
    }

    @Test
    void getAllBankAccountsByOpeningDayIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where openingDay in
        defaultBankAccountFiltering(
            "openingDay.in=" + DEFAULT_OPENING_DAY + "," + UPDATED_OPENING_DAY,
            "openingDay.in=" + UPDATED_OPENING_DAY
        );
    }

    @Test
    void getAllBankAccountsByOpeningDayIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where openingDay is not null
        defaultBankAccountFiltering("openingDay.specified=true", "openingDay.specified=false");
    }

    @Test
    void getAllBankAccountsByOpeningDayIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where openingDay is greater than or equal to
        defaultBankAccountFiltering(
            "openingDay.greaterThanOrEqual=" + DEFAULT_OPENING_DAY,
            "openingDay.greaterThanOrEqual=" + UPDATED_OPENING_DAY
        );
    }

    @Test
    void getAllBankAccountsByOpeningDayIsLessThanOrEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where openingDay is less than or equal to
        defaultBankAccountFiltering(
            "openingDay.lessThanOrEqual=" + DEFAULT_OPENING_DAY,
            "openingDay.lessThanOrEqual=" + SMALLER_OPENING_DAY
        );
    }

    @Test
    void getAllBankAccountsByOpeningDayIsLessThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where openingDay is less than
        defaultBankAccountFiltering("openingDay.lessThan=" + UPDATED_OPENING_DAY, "openingDay.lessThan=" + DEFAULT_OPENING_DAY);
    }

    @Test
    void getAllBankAccountsByOpeningDayIsGreaterThanSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where openingDay is greater than
        defaultBankAccountFiltering("openingDay.greaterThan=" + SMALLER_OPENING_DAY, "openingDay.greaterThan=" + DEFAULT_OPENING_DAY);
    }

    @Test
    void getAllBankAccountsByLastOperationDateIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where lastOperationDate equals to
        defaultBankAccountFiltering(
            "lastOperationDate.equals=" + DEFAULT_LAST_OPERATION_DATE,
            "lastOperationDate.equals=" + UPDATED_LAST_OPERATION_DATE
        );
    }

    @Test
    void getAllBankAccountsByLastOperationDateIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where lastOperationDate in
        defaultBankAccountFiltering(
            "lastOperationDate.in=" + DEFAULT_LAST_OPERATION_DATE + "," + UPDATED_LAST_OPERATION_DATE,
            "lastOperationDate.in=" + UPDATED_LAST_OPERATION_DATE
        );
    }

    @Test
    void getAllBankAccountsByLastOperationDateIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where lastOperationDate is not null
        defaultBankAccountFiltering("lastOperationDate.specified=true", "lastOperationDate.specified=false");
    }

    @Test
    void getAllBankAccountsByActiveIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where active equals to
        defaultBankAccountFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    void getAllBankAccountsByActiveIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where active in
        defaultBankAccountFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    void getAllBankAccountsByActiveIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where active is not null
        defaultBankAccountFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    void getAllBankAccountsByAccountTypeIsEqualToSomething() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where accountType equals to
        defaultBankAccountFiltering("accountType.equals=" + DEFAULT_ACCOUNT_TYPE, "accountType.equals=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    void getAllBankAccountsByAccountTypeIsInShouldWork() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where accountType in
        defaultBankAccountFiltering(
            "accountType.in=" + DEFAULT_ACCOUNT_TYPE + "," + UPDATED_ACCOUNT_TYPE,
            "accountType.in=" + UPDATED_ACCOUNT_TYPE
        );
    }

    @Test
    void getAllBankAccountsByAccountTypeIsNullOrNotNull() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        // Get all the bankAccountList where accountType is not null
        defaultBankAccountFiltering("accountType.specified=true", "accountType.specified=false");
    }

    @Test
    void getAllBankAccountsByUserIsEqualToSomething() {
        User user = UserResourceIT.createEntity();
        userRepository.save(user).block();
        Long userId = user.getId();
        bankAccount.setUserId(userId);
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();
        // Get all the bankAccountList where user equals to userId
        defaultBankAccountShouldBeFound("userId.equals=" + userId);

        // Get all the bankAccountList where user equals to (userId + 1)
        defaultBankAccountShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultBankAccountFiltering(String shouldBeFound, String shouldNotBeFound) {
        defaultBankAccountShouldBeFound(shouldBeFound);
        defaultBankAccountShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBankAccountShouldBeFound(String filter) {
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
            .value(hasItem(bankAccount.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))

            .jsonPath("$.[*].guid")
            .value(hasItem(DEFAULT_GUID.toString()))

            .jsonPath("$.[*].bankNumber")
            .value(hasItem(DEFAULT_BANK_NUMBER))

            .jsonPath("$.[*].agencyNumber")
            .value(hasItem(DEFAULT_AGENCY_NUMBER.intValue()))

            .jsonPath("$.[*].lastOperationDuration")
            .value(hasItem(DEFAULT_LAST_OPERATION_DURATION.doubleValue()))

            .jsonPath("$.[*].meanOperationDuration")
            .value(hasItem(DEFAULT_MEAN_OPERATION_DURATION))

            .jsonPath("$.[*].meanQueueDuration")
            .value(hasItem(DEFAULT_MEAN_QUEUE_DURATION.toString()))

            .jsonPath("$.[*].balance")
            .value(hasItem(sameNumber(DEFAULT_BALANCE)))

            .jsonPath("$.[*].openingDay")
            .value(hasItem(DEFAULT_OPENING_DAY.toString()))

            .jsonPath("$.[*].lastOperationDate")
            .value(hasItem(DEFAULT_LAST_OPERATION_DATE.toString()))

            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE))

            .jsonPath("$.[*].accountType")
            .value(hasItem(DEFAULT_ACCOUNT_TYPE.toString()))

            .jsonPath("$.[*].attachmentContentType")
            .value(hasItem(DEFAULT_ATTACHMENT_CONTENT_TYPE))
            .jsonPath("$.[*].attachment")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_ATTACHMENT)))

            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));

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
    private void defaultBankAccountShouldNotBeFound(String filter) {
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
    void getNonExistingBankAccount() {
        // Get the bankAccount
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingBankAccount() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankAccountSearchRepository.save(bankAccount).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());

        // Update the bankAccount
        BankAccount updatedBankAccount = bankAccountRepository.findById(bankAccount.getId()).block();
        updatedBankAccount
            .name(UPDATED_NAME)
            .guid(UPDATED_GUID)
            .bankNumber(UPDATED_BANK_NUMBER)
            .agencyNumber(UPDATED_AGENCY_NUMBER)
            .lastOperationDuration(UPDATED_LAST_OPERATION_DURATION)
            .meanOperationDuration(UPDATED_MEAN_OPERATION_DURATION)
            .meanQueueDuration(UPDATED_MEAN_QUEUE_DURATION)
            .balance(UPDATED_BALANCE)
            .openingDay(UPDATED_OPENING_DAY)
            .lastOperationDate(UPDATED_LAST_OPERATION_DATE)
            .active(UPDATED_ACTIVE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBankAccount.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedBankAccount))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBankAccountToMatchAllProperties(updatedBankAccount);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<BankAccount> bankAccountSearchList = Streamable.of(
                    bankAccountSearchRepository.findAll().collectList().block()
                ).toList();
                BankAccount testBankAccountSearch = bankAccountSearchList.get(searchDatabaseSizeAfter - 1);

                // Test fails because reactive api returns an empty object instead of null
                // assertBankAccountAllPropertiesEquals(testBankAccountSearch, updatedBankAccount);
                assertBankAccountUpdatableFieldsEquals(testBankAccountSearch, updatedBankAccount);
            });
    }

    @Test
    void putNonExistingBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        bankAccount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, bankAccount.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(bankAccount))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        bankAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(bankAccount))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        bankAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(bankAccount))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateBankAccountWithPatch() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankAccount using partial update
        BankAccount partialUpdatedBankAccount = new BankAccount();
        partialUpdatedBankAccount.setId(bankAccount.getId());

        partialUpdatedBankAccount
            .name(UPDATED_NAME)
            .bankNumber(UPDATED_BANK_NUMBER)
            .lastOperationDuration(UPDATED_LAST_OPERATION_DURATION)
            .meanOperationDuration(UPDATED_MEAN_OPERATION_DURATION)
            .meanQueueDuration(UPDATED_MEAN_QUEUE_DURATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBankAccount.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedBankAccount))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BankAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankAccountUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBankAccount, bankAccount),
            getPersistedBankAccount(bankAccount)
        );
    }

    @Test
    void fullUpdateBankAccountWithPatch() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankAccount using partial update
        BankAccount partialUpdatedBankAccount = new BankAccount();
        partialUpdatedBankAccount.setId(bankAccount.getId());

        partialUpdatedBankAccount
            .name(UPDATED_NAME)
            .guid(UPDATED_GUID)
            .bankNumber(UPDATED_BANK_NUMBER)
            .agencyNumber(UPDATED_AGENCY_NUMBER)
            .lastOperationDuration(UPDATED_LAST_OPERATION_DURATION)
            .meanOperationDuration(UPDATED_MEAN_OPERATION_DURATION)
            .meanQueueDuration(UPDATED_MEAN_QUEUE_DURATION)
            .balance(UPDATED_BALANCE)
            .openingDay(UPDATED_OPENING_DAY)
            .lastOperationDate(UPDATED_LAST_OPERATION_DATE)
            .active(UPDATED_ACTIVE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBankAccount.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedBankAccount))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BankAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankAccountUpdatableFieldsEquals(partialUpdatedBankAccount, getPersistedBankAccount(partialUpdatedBankAccount));
    }

    @Test
    void patchNonExistingBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        bankAccount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, bankAccount.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(bankAccount))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        bankAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(bankAccount))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        bankAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(bankAccount))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteBankAccount() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();
        bankAccountRepository.save(bankAccount).block();
        bankAccountSearchRepository.save(bankAccount).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the bankAccount
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, bankAccount.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bankAccountSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchBankAccount() {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.save(bankAccount).block();
        bankAccountSearchRepository.save(bankAccount).block();

        // Search the bankAccount
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + bankAccount.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(bankAccount.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].guid")
            .value(hasItem(DEFAULT_GUID.toString()))
            .jsonPath("$.[*].bankNumber")
            .value(hasItem(DEFAULT_BANK_NUMBER))
            .jsonPath("$.[*].agencyNumber")
            .value(hasItem(DEFAULT_AGENCY_NUMBER.intValue()))
            .jsonPath("$.[*].lastOperationDuration")
            .value(hasItem(DEFAULT_LAST_OPERATION_DURATION.doubleValue()))
            .jsonPath("$.[*].meanOperationDuration")
            .value(hasItem(DEFAULT_MEAN_OPERATION_DURATION))
            .jsonPath("$.[*].meanQueueDuration")
            .value(hasItem(DEFAULT_MEAN_QUEUE_DURATION.toString()))
            .jsonPath("$.[*].balance")
            .value(hasItem(sameNumber(DEFAULT_BALANCE)))
            .jsonPath("$.[*].openingDay")
            .value(hasItem(DEFAULT_OPENING_DAY.toString()))
            .jsonPath("$.[*].lastOperationDate")
            .value(hasItem(DEFAULT_LAST_OPERATION_DATE.toString()))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE))
            .jsonPath("$.[*].accountType")
            .value(hasItem(DEFAULT_ACCOUNT_TYPE.toString()))
            .jsonPath("$.[*].attachmentContentType")
            .value(hasItem(DEFAULT_ATTACHMENT_CONTENT_TYPE))
            .jsonPath("$.[*].attachment")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_ATTACHMENT)))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION.toString()));
    }

    protected long getRepositoryCount() {
        return bankAccountRepository.count().block();
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

    protected BankAccount getPersistedBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.findById(bankAccount.getId()).block();
    }

    protected void assertPersistedBankAccountToMatchAllProperties(BankAccount expectedBankAccount) {
        // Test fails because reactive api returns an empty object instead of null
        // assertBankAccountAllPropertiesEquals(expectedBankAccount, getPersistedBankAccount(expectedBankAccount));
        assertBankAccountUpdatableFieldsEquals(expectedBankAccount, getPersistedBankAccount(expectedBankAccount));
    }

    protected void assertPersistedBankAccountToMatchUpdatableProperties(BankAccount expectedBankAccount) {
        // Test fails because reactive api returns an empty object instead of null
        // assertBankAccountAllUpdatablePropertiesEquals(expectedBankAccount, getPersistedBankAccount(expectedBankAccount));
        assertBankAccountUpdatableFieldsEquals(expectedBankAccount, getPersistedBankAccount(expectedBankAccount));
    }
}
