package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static tech.jhipster.sample.domain.FieldTestEnumWithValueAsserts.*;
import static tech.jhipster.sample.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
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
import tech.jhipster.sample.domain.FieldTestEnumWithValue;
import tech.jhipster.sample.domain.enumeration.MyEnumA;
import tech.jhipster.sample.domain.enumeration.MyEnumB;
import tech.jhipster.sample.domain.enumeration.MyEnumC;
import tech.jhipster.sample.domain.enumeration.MyEnumD;
import tech.jhipster.sample.domain.enumeration.MyEnumE;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.FieldTestEnumWithValueRepository;

/**
 * Integration tests for the {@link FieldTestEnumWithValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FieldTestEnumWithValueResourceIT {

    private static final MyEnumA DEFAULT_MY_FIELD_A = MyEnumA.AAA;
    private static final MyEnumA UPDATED_MY_FIELD_A = MyEnumA.BBB;

    private static final MyEnumB DEFAULT_MY_FIELD_B = MyEnumB.AAA;
    private static final MyEnumB UPDATED_MY_FIELD_B = MyEnumB.BBB;

    private static final MyEnumC DEFAULT_MY_FIELD_C = MyEnumC.AAA;
    private static final MyEnumC UPDATED_MY_FIELD_C = MyEnumC.BBB;

    private static final MyEnumD DEFAULT_MY_FIELD_D = MyEnumD.AAA;
    private static final MyEnumD UPDATED_MY_FIELD_D = MyEnumD.BBB;

    private static final MyEnumE DEFAULT_MY_FIELD_E = MyEnumE.AAA;
    private static final MyEnumE UPDATED_MY_FIELD_E = MyEnumE.BBB;

    private static final String ENTITY_API_URL = "/api/field-test-enum-with-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FieldTestEnumWithValueRepository fieldTestEnumWithValueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FieldTestEnumWithValue fieldTestEnumWithValue;

    private FieldTestEnumWithValue insertedFieldTestEnumWithValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldTestEnumWithValue createEntity() {
        return new FieldTestEnumWithValue()
            .myFieldA(DEFAULT_MY_FIELD_A)
            .myFieldB(DEFAULT_MY_FIELD_B)
            .myFieldC(DEFAULT_MY_FIELD_C)
            .myFieldD(DEFAULT_MY_FIELD_D)
            .myFieldE(DEFAULT_MY_FIELD_E);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldTestEnumWithValue createUpdatedEntity() {
        return new FieldTestEnumWithValue()
            .myFieldA(UPDATED_MY_FIELD_A)
            .myFieldB(UPDATED_MY_FIELD_B)
            .myFieldC(UPDATED_MY_FIELD_C)
            .myFieldD(UPDATED_MY_FIELD_D)
            .myFieldE(UPDATED_MY_FIELD_E);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FieldTestEnumWithValue.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        fieldTestEnumWithValue = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFieldTestEnumWithValue != null) {
            fieldTestEnumWithValueRepository.delete(insertedFieldTestEnumWithValue).block();
            insertedFieldTestEnumWithValue = null;
        }
        deleteEntities(em);
    }

    @Test
    void createFieldTestEnumWithValue() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FieldTestEnumWithValue
        var returnedFieldTestEnumWithValue = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestEnumWithValue))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FieldTestEnumWithValue.class)
            .returnResult()
            .getResponseBody();

        // Validate the FieldTestEnumWithValue in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFieldTestEnumWithValueUpdatableFieldsEquals(
            returnedFieldTestEnumWithValue,
            getPersistedFieldTestEnumWithValue(returnedFieldTestEnumWithValue)
        );

        insertedFieldTestEnumWithValue = returnedFieldTestEnumWithValue;
    }

    @Test
    void createFieldTestEnumWithValueWithExistingId() throws Exception {
        // Create the FieldTestEnumWithValue with an existing ID
        fieldTestEnumWithValue.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestEnumWithValue))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestEnumWithValue in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFieldTestEnumWithValuesAsStream() {
        // Initialize the database
        fieldTestEnumWithValueRepository.save(fieldTestEnumWithValue).block();

        List<FieldTestEnumWithValue> fieldTestEnumWithValueList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(FieldTestEnumWithValue.class)
            .getResponseBody()
            .filter(fieldTestEnumWithValue::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(fieldTestEnumWithValueList).isNotNull();
        assertThat(fieldTestEnumWithValueList).hasSize(1);
        FieldTestEnumWithValue testFieldTestEnumWithValue = fieldTestEnumWithValueList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertFieldTestEnumWithValueAllPropertiesEquals(fieldTestEnumWithValue, testFieldTestEnumWithValue);
        assertFieldTestEnumWithValueUpdatableFieldsEquals(fieldTestEnumWithValue, testFieldTestEnumWithValue);
    }

    @Test
    void getAllFieldTestEnumWithValues() {
        // Initialize the database
        insertedFieldTestEnumWithValue = fieldTestEnumWithValueRepository.save(fieldTestEnumWithValue).block();

        // Get all the fieldTestEnumWithValueList
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
            .value(hasItem(fieldTestEnumWithValue.getId().intValue()))
            .jsonPath("$.[*].myFieldA")
            .value(hasItem(DEFAULT_MY_FIELD_A.toString()))
            .jsonPath("$.[*].myFieldB")
            .value(hasItem(DEFAULT_MY_FIELD_B.toString()))
            .jsonPath("$.[*].myFieldC")
            .value(hasItem(DEFAULT_MY_FIELD_C.toString()))
            .jsonPath("$.[*].myFieldD")
            .value(hasItem(DEFAULT_MY_FIELD_D.toString()))
            .jsonPath("$.[*].myFieldE")
            .value(hasItem(DEFAULT_MY_FIELD_E.toString()));
    }

    @Test
    void getFieldTestEnumWithValue() {
        // Initialize the database
        insertedFieldTestEnumWithValue = fieldTestEnumWithValueRepository.save(fieldTestEnumWithValue).block();

        // Get the fieldTestEnumWithValue
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, fieldTestEnumWithValue.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(fieldTestEnumWithValue.getId().intValue()))
            .jsonPath("$.myFieldA")
            .value(is(DEFAULT_MY_FIELD_A.toString()))
            .jsonPath("$.myFieldB")
            .value(is(DEFAULT_MY_FIELD_B.toString()))
            .jsonPath("$.myFieldC")
            .value(is(DEFAULT_MY_FIELD_C.toString()))
            .jsonPath("$.myFieldD")
            .value(is(DEFAULT_MY_FIELD_D.toString()))
            .jsonPath("$.myFieldE")
            .value(is(DEFAULT_MY_FIELD_E.toString()));
    }

    @Test
    void getNonExistingFieldTestEnumWithValue() {
        // Get the fieldTestEnumWithValue
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFieldTestEnumWithValue() throws Exception {
        // Initialize the database
        insertedFieldTestEnumWithValue = fieldTestEnumWithValueRepository.save(fieldTestEnumWithValue).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestEnumWithValue
        FieldTestEnumWithValue updatedFieldTestEnumWithValue = fieldTestEnumWithValueRepository
            .findById(fieldTestEnumWithValue.getId())
            .block();
        updatedFieldTestEnumWithValue
            .myFieldA(UPDATED_MY_FIELD_A)
            .myFieldB(UPDATED_MY_FIELD_B)
            .myFieldC(UPDATED_MY_FIELD_C)
            .myFieldD(UPDATED_MY_FIELD_D)
            .myFieldE(UPDATED_MY_FIELD_E);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFieldTestEnumWithValue.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedFieldTestEnumWithValue))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestEnumWithValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFieldTestEnumWithValueToMatchAllProperties(updatedFieldTestEnumWithValue);
    }

    @Test
    void putNonExistingFieldTestEnumWithValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestEnumWithValue.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fieldTestEnumWithValue.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestEnumWithValue))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestEnumWithValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFieldTestEnumWithValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestEnumWithValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestEnumWithValue))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestEnumWithValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFieldTestEnumWithValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestEnumWithValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fieldTestEnumWithValue))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FieldTestEnumWithValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFieldTestEnumWithValueWithPatch() throws Exception {
        // Initialize the database
        insertedFieldTestEnumWithValue = fieldTestEnumWithValueRepository.save(fieldTestEnumWithValue).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestEnumWithValue using partial update
        FieldTestEnumWithValue partialUpdatedFieldTestEnumWithValue = new FieldTestEnumWithValue();
        partialUpdatedFieldTestEnumWithValue.setId(fieldTestEnumWithValue.getId());

        partialUpdatedFieldTestEnumWithValue
            .myFieldB(UPDATED_MY_FIELD_B)
            .myFieldC(UPDATED_MY_FIELD_C)
            .myFieldD(UPDATED_MY_FIELD_D)
            .myFieldE(UPDATED_MY_FIELD_E);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFieldTestEnumWithValue.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFieldTestEnumWithValue))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestEnumWithValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFieldTestEnumWithValueUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFieldTestEnumWithValue, fieldTestEnumWithValue),
            getPersistedFieldTestEnumWithValue(fieldTestEnumWithValue)
        );
    }

    @Test
    void fullUpdateFieldTestEnumWithValueWithPatch() throws Exception {
        // Initialize the database
        insertedFieldTestEnumWithValue = fieldTestEnumWithValueRepository.save(fieldTestEnumWithValue).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldTestEnumWithValue using partial update
        FieldTestEnumWithValue partialUpdatedFieldTestEnumWithValue = new FieldTestEnumWithValue();
        partialUpdatedFieldTestEnumWithValue.setId(fieldTestEnumWithValue.getId());

        partialUpdatedFieldTestEnumWithValue
            .myFieldA(UPDATED_MY_FIELD_A)
            .myFieldB(UPDATED_MY_FIELD_B)
            .myFieldC(UPDATED_MY_FIELD_C)
            .myFieldD(UPDATED_MY_FIELD_D)
            .myFieldE(UPDATED_MY_FIELD_E);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFieldTestEnumWithValue.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFieldTestEnumWithValue))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FieldTestEnumWithValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFieldTestEnumWithValueUpdatableFieldsEquals(
            partialUpdatedFieldTestEnumWithValue,
            getPersistedFieldTestEnumWithValue(partialUpdatedFieldTestEnumWithValue)
        );
    }

    @Test
    void patchNonExistingFieldTestEnumWithValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestEnumWithValue.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, fieldTestEnumWithValue.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestEnumWithValue))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestEnumWithValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFieldTestEnumWithValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestEnumWithValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestEnumWithValue))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FieldTestEnumWithValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFieldTestEnumWithValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldTestEnumWithValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fieldTestEnumWithValue))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FieldTestEnumWithValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFieldTestEnumWithValue() {
        // Initialize the database
        insertedFieldTestEnumWithValue = fieldTestEnumWithValueRepository.save(fieldTestEnumWithValue).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fieldTestEnumWithValue
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, fieldTestEnumWithValue.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fieldTestEnumWithValueRepository.count().block();
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

    protected FieldTestEnumWithValue getPersistedFieldTestEnumWithValue(FieldTestEnumWithValue fieldTestEnumWithValue) {
        return fieldTestEnumWithValueRepository.findById(fieldTestEnumWithValue.getId()).block();
    }

    protected void assertPersistedFieldTestEnumWithValueToMatchAllProperties(FieldTestEnumWithValue expectedFieldTestEnumWithValue) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFieldTestEnumWithValueAllPropertiesEquals(expectedFieldTestEnumWithValue, getPersistedFieldTestEnumWithValue(expectedFieldTestEnumWithValue));
        assertFieldTestEnumWithValueUpdatableFieldsEquals(
            expectedFieldTestEnumWithValue,
            getPersistedFieldTestEnumWithValue(expectedFieldTestEnumWithValue)
        );
    }

    protected void assertPersistedFieldTestEnumWithValueToMatchUpdatableProperties(FieldTestEnumWithValue expectedFieldTestEnumWithValue) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFieldTestEnumWithValueAllUpdatablePropertiesEquals(expectedFieldTestEnumWithValue, getPersistedFieldTestEnumWithValue(expectedFieldTestEnumWithValue));
        assertFieldTestEnumWithValueUpdatableFieldsEquals(
            expectedFieldTestEnumWithValue,
            getPersistedFieldTestEnumWithValue(expectedFieldTestEnumWithValue)
        );
    }
}
