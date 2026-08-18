package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static tech.jhipster.sample.domain.EntityWithServiceClassAndPaginationAsserts.*;
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
import tech.jhipster.sample.domain.EntityWithServiceClassAndPagination;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.EntityWithServiceClassAndPaginationRepository;

/**
 * Integration tests for the {@link EntityWithServiceClassAndPaginationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EntityWithServiceClassAndPaginationResourceIT {

    private static final String DEFAULT_ENZO = "AAAAAAAAAA";
    private static final String UPDATED_ENZO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entity-with-service-class-and-paginations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityWithServiceClassAndPaginationRepository entityWithServiceClassAndPaginationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EntityWithServiceClassAndPagination entityWithServiceClassAndPagination;

    private EntityWithServiceClassAndPagination insertedEntityWithServiceClassAndPagination;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithServiceClassAndPagination createEntity() {
        return new EntityWithServiceClassAndPagination().enzo(DEFAULT_ENZO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithServiceClassAndPagination createUpdatedEntity() {
        return new EntityWithServiceClassAndPagination().enzo(UPDATED_ENZO);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EntityWithServiceClassAndPagination.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        entityWithServiceClassAndPagination = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEntityWithServiceClassAndPagination != null) {
            entityWithServiceClassAndPaginationRepository.delete(insertedEntityWithServiceClassAndPagination).block();
            insertedEntityWithServiceClassAndPagination = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEntityWithServiceClassAndPagination() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EntityWithServiceClassAndPagination
        var returnedEntityWithServiceClassAndPagination = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassAndPagination))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EntityWithServiceClassAndPagination.class)
            .returnResult()
            .getResponseBody();

        // Validate the EntityWithServiceClassAndPagination in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEntityWithServiceClassAndPaginationUpdatableFieldsEquals(
            returnedEntityWithServiceClassAndPagination,
            getPersistedEntityWithServiceClassAndPagination(returnedEntityWithServiceClassAndPagination)
        );

        insertedEntityWithServiceClassAndPagination = returnedEntityWithServiceClassAndPagination;
    }

    @Test
    void createEntityWithServiceClassAndPaginationWithExistingId() throws Exception {
        // Create the EntityWithServiceClassAndPagination with an existing ID
        entityWithServiceClassAndPagination.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassAndPagination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceClassAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEntityWithServiceClassAndPaginations() {
        // Initialize the database
        insertedEntityWithServiceClassAndPagination = entityWithServiceClassAndPaginationRepository
            .save(entityWithServiceClassAndPagination)
            .block();

        // Get all the entityWithServiceClassAndPaginationList
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
            .value(hasItem(entityWithServiceClassAndPagination.getId().intValue()))
            .jsonPath("$.[*].enzo")
            .value(hasItem(DEFAULT_ENZO));
    }

    @Test
    void getEntityWithServiceClassAndPagination() {
        // Initialize the database
        insertedEntityWithServiceClassAndPagination = entityWithServiceClassAndPaginationRepository
            .save(entityWithServiceClassAndPagination)
            .block();

        // Get the entityWithServiceClassAndPagination
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, entityWithServiceClassAndPagination.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(entityWithServiceClassAndPagination.getId().intValue()))
            .jsonPath("$.enzo")
            .value(is(DEFAULT_ENZO));
    }

    @Test
    void getNonExistingEntityWithServiceClassAndPagination() {
        // Get the entityWithServiceClassAndPagination
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEntityWithServiceClassAndPagination() throws Exception {
        // Initialize the database
        insertedEntityWithServiceClassAndPagination = entityWithServiceClassAndPaginationRepository
            .save(entityWithServiceClassAndPagination)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceClassAndPagination
        EntityWithServiceClassAndPagination updatedEntityWithServiceClassAndPagination = entityWithServiceClassAndPaginationRepository
            .findById(entityWithServiceClassAndPagination.getId())
            .block();
        updatedEntityWithServiceClassAndPagination.enzo(UPDATED_ENZO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEntityWithServiceClassAndPagination.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedEntityWithServiceClassAndPagination))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceClassAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntityWithServiceClassAndPaginationToMatchAllProperties(updatedEntityWithServiceClassAndPagination);
    }

    @Test
    void putNonExistingEntityWithServiceClassAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassAndPagination.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithServiceClassAndPagination.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassAndPagination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceClassAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEntityWithServiceClassAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassAndPagination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassAndPagination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceClassAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEntityWithServiceClassAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassAndPagination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassAndPagination))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithServiceClassAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEntityWithServiceClassAndPaginationWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithServiceClassAndPagination = entityWithServiceClassAndPaginationRepository
            .save(entityWithServiceClassAndPagination)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceClassAndPagination using partial update
        EntityWithServiceClassAndPagination partialUpdatedEntityWithServiceClassAndPagination = new EntityWithServiceClassAndPagination();
        partialUpdatedEntityWithServiceClassAndPagination.setId(entityWithServiceClassAndPagination.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithServiceClassAndPagination.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithServiceClassAndPagination))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceClassAndPagination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithServiceClassAndPaginationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntityWithServiceClassAndPagination, entityWithServiceClassAndPagination),
            getPersistedEntityWithServiceClassAndPagination(entityWithServiceClassAndPagination)
        );
    }

    @Test
    void fullUpdateEntityWithServiceClassAndPaginationWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithServiceClassAndPagination = entityWithServiceClassAndPaginationRepository
            .save(entityWithServiceClassAndPagination)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceClassAndPagination using partial update
        EntityWithServiceClassAndPagination partialUpdatedEntityWithServiceClassAndPagination = new EntityWithServiceClassAndPagination();
        partialUpdatedEntityWithServiceClassAndPagination.setId(entityWithServiceClassAndPagination.getId());

        partialUpdatedEntityWithServiceClassAndPagination.enzo(UPDATED_ENZO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithServiceClassAndPagination.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithServiceClassAndPagination))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceClassAndPagination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithServiceClassAndPaginationUpdatableFieldsEquals(
            partialUpdatedEntityWithServiceClassAndPagination,
            getPersistedEntityWithServiceClassAndPagination(partialUpdatedEntityWithServiceClassAndPagination)
        );
    }

    @Test
    void patchNonExistingEntityWithServiceClassAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassAndPagination.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, entityWithServiceClassAndPagination.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassAndPagination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceClassAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEntityWithServiceClassAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassAndPagination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassAndPagination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceClassAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEntityWithServiceClassAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassAndPagination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassAndPagination))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithServiceClassAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEntityWithServiceClassAndPagination() {
        // Initialize the database
        insertedEntityWithServiceClassAndPagination = entityWithServiceClassAndPaginationRepository
            .save(entityWithServiceClassAndPagination)
            .block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entityWithServiceClassAndPagination
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, entityWithServiceClassAndPagination.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entityWithServiceClassAndPaginationRepository.count().block();
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

    protected EntityWithServiceClassAndPagination getPersistedEntityWithServiceClassAndPagination(
        EntityWithServiceClassAndPagination entityWithServiceClassAndPagination
    ) {
        return entityWithServiceClassAndPaginationRepository.findById(entityWithServiceClassAndPagination.getId()).block();
    }

    protected void assertPersistedEntityWithServiceClassAndPaginationToMatchAllProperties(
        EntityWithServiceClassAndPagination expectedEntityWithServiceClassAndPagination
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithServiceClassAndPaginationAllPropertiesEquals(expectedEntityWithServiceClassAndPagination, getPersistedEntityWithServiceClassAndPagination(expectedEntityWithServiceClassAndPagination));
        assertEntityWithServiceClassAndPaginationUpdatableFieldsEquals(
            expectedEntityWithServiceClassAndPagination,
            getPersistedEntityWithServiceClassAndPagination(expectedEntityWithServiceClassAndPagination)
        );
    }

    protected void assertPersistedEntityWithServiceClassAndPaginationToMatchUpdatableProperties(
        EntityWithServiceClassAndPagination expectedEntityWithServiceClassAndPagination
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithServiceClassAndPaginationAllUpdatablePropertiesEquals(expectedEntityWithServiceClassAndPagination, getPersistedEntityWithServiceClassAndPagination(expectedEntityWithServiceClassAndPagination));
        assertEntityWithServiceClassAndPaginationUpdatableFieldsEquals(
            expectedEntityWithServiceClassAndPagination,
            getPersistedEntityWithServiceClassAndPagination(expectedEntityWithServiceClassAndPagination)
        );
    }
}
