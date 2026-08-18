package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static tech.jhipster.sample.domain.EntityWithServiceImplAndPaginationAsserts.*;
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
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import tech.jhipster.sample.IntegrationTest;
import tech.jhipster.sample.domain.EntityWithServiceImplAndPagination;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.EntityWithServiceImplAndPaginationRepository;

/**
 * Integration tests for the {@link EntityWithServiceImplAndPaginationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EntityWithServiceImplAndPaginationResourceIT {

    private static final String DEFAULT_HUGO = "AAAAAAAAAA";
    private static final String UPDATED_HUGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entity-with-service-impl-and-paginations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityWithServiceImplAndPaginationRepository entityWithServiceImplAndPaginationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EntityWithServiceImplAndPagination entityWithServiceImplAndPagination;

    private EntityWithServiceImplAndPagination insertedEntityWithServiceImplAndPagination;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithServiceImplAndPagination createEntity() {
        return new EntityWithServiceImplAndPagination().hugo(DEFAULT_HUGO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithServiceImplAndPagination createUpdatedEntity() {
        return new EntityWithServiceImplAndPagination().hugo(UPDATED_HUGO);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EntityWithServiceImplAndPagination.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        entityWithServiceImplAndPagination = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEntityWithServiceImplAndPagination != null) {
            entityWithServiceImplAndPaginationRepository.delete(insertedEntityWithServiceImplAndPagination).block();
            insertedEntityWithServiceImplAndPagination = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEntityWithServiceImplAndPagination() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EntityWithServiceImplAndPagination
        var returnedEntityWithServiceImplAndPagination = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndPagination))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EntityWithServiceImplAndPagination.class)
            .returnResult()
            .getResponseBody();

        // Validate the EntityWithServiceImplAndPagination in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEntityWithServiceImplAndPaginationUpdatableFieldsEquals(
            returnedEntityWithServiceImplAndPagination,
            getPersistedEntityWithServiceImplAndPagination(returnedEntityWithServiceImplAndPagination)
        );

        insertedEntityWithServiceImplAndPagination = returnedEntityWithServiceImplAndPagination;
    }

    @Test
    void createEntityWithServiceImplAndPaginationWithExistingId() throws Exception {
        // Create the EntityWithServiceImplAndPagination with an existing ID
        entityWithServiceImplAndPagination.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndPagination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEntityWithServiceImplAndPaginations() {
        // Initialize the database
        insertedEntityWithServiceImplAndPagination = entityWithServiceImplAndPaginationRepository
            .save(entityWithServiceImplAndPagination)
            .block();

        // Get all the entityWithServiceImplAndPaginationList
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
            .value(hasItem(entityWithServiceImplAndPagination.getId().intValue()))
            .jsonPath("$.[*].hugo")
            .value(hasItem(DEFAULT_HUGO));
    }

    @Test
    void getEntityWithServiceImplAndPagination() {
        // Initialize the database
        insertedEntityWithServiceImplAndPagination = entityWithServiceImplAndPaginationRepository
            .save(entityWithServiceImplAndPagination)
            .block();

        // Get the entityWithServiceImplAndPagination
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplAndPagination.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(entityWithServiceImplAndPagination.getId().intValue()))
            .jsonPath("$.hugo")
            .value(is(DEFAULT_HUGO));
    }

    @Test
    void getNonExistingEntityWithServiceImplAndPagination() {
        // Get the entityWithServiceImplAndPagination
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEntityWithServiceImplAndPagination() throws Exception {
        // Initialize the database
        insertedEntityWithServiceImplAndPagination = entityWithServiceImplAndPaginationRepository
            .save(entityWithServiceImplAndPagination)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceImplAndPagination
        EntityWithServiceImplAndPagination updatedEntityWithServiceImplAndPagination = entityWithServiceImplAndPaginationRepository
            .findById(entityWithServiceImplAndPagination.getId())
            .block();
        updatedEntityWithServiceImplAndPagination.hugo(UPDATED_HUGO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEntityWithServiceImplAndPagination.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedEntityWithServiceImplAndPagination))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceImplAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntityWithServiceImplAndPaginationToMatchAllProperties(updatedEntityWithServiceImplAndPagination);
    }

    @Test
    void putNonExistingEntityWithServiceImplAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndPagination.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplAndPagination.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndPagination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEntityWithServiceImplAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndPagination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndPagination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEntityWithServiceImplAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndPagination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndPagination))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithServiceImplAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEntityWithServiceImplAndPaginationWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithServiceImplAndPagination = entityWithServiceImplAndPaginationRepository
            .save(entityWithServiceImplAndPagination)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceImplAndPagination using partial update
        EntityWithServiceImplAndPagination partialUpdatedEntityWithServiceImplAndPagination = new EntityWithServiceImplAndPagination();
        partialUpdatedEntityWithServiceImplAndPagination.setId(entityWithServiceImplAndPagination.getId());

        partialUpdatedEntityWithServiceImplAndPagination.hugo(UPDATED_HUGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithServiceImplAndPagination.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithServiceImplAndPagination))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceImplAndPagination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithServiceImplAndPaginationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntityWithServiceImplAndPagination, entityWithServiceImplAndPagination),
            getPersistedEntityWithServiceImplAndPagination(entityWithServiceImplAndPagination)
        );
    }

    @Test
    void fullUpdateEntityWithServiceImplAndPaginationWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithServiceImplAndPagination = entityWithServiceImplAndPaginationRepository
            .save(entityWithServiceImplAndPagination)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceImplAndPagination using partial update
        EntityWithServiceImplAndPagination partialUpdatedEntityWithServiceImplAndPagination = new EntityWithServiceImplAndPagination();
        partialUpdatedEntityWithServiceImplAndPagination.setId(entityWithServiceImplAndPagination.getId());

        partialUpdatedEntityWithServiceImplAndPagination.hugo(UPDATED_HUGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithServiceImplAndPagination.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithServiceImplAndPagination))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceImplAndPagination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithServiceImplAndPaginationUpdatableFieldsEquals(
            partialUpdatedEntityWithServiceImplAndPagination,
            getPersistedEntityWithServiceImplAndPagination(partialUpdatedEntityWithServiceImplAndPagination)
        );
    }

    @Test
    void patchNonExistingEntityWithServiceImplAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndPagination.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplAndPagination.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndPagination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEntityWithServiceImplAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndPagination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndPagination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEntityWithServiceImplAndPagination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndPagination.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndPagination))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithServiceImplAndPagination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEntityWithServiceImplAndPagination() {
        // Initialize the database
        insertedEntityWithServiceImplAndPagination = entityWithServiceImplAndPaginationRepository
            .save(entityWithServiceImplAndPagination)
            .block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entityWithServiceImplAndPagination
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplAndPagination.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entityWithServiceImplAndPaginationRepository.count().block();
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

    protected EntityWithServiceImplAndPagination getPersistedEntityWithServiceImplAndPagination(
        EntityWithServiceImplAndPagination entityWithServiceImplAndPagination
    ) {
        return entityWithServiceImplAndPaginationRepository.findById(entityWithServiceImplAndPagination.getId()).block();
    }

    protected void assertPersistedEntityWithServiceImplAndPaginationToMatchAllProperties(
        EntityWithServiceImplAndPagination expectedEntityWithServiceImplAndPagination
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithServiceImplAndPaginationAllPropertiesEquals(expectedEntityWithServiceImplAndPagination, getPersistedEntityWithServiceImplAndPagination(expectedEntityWithServiceImplAndPagination));
        assertEntityWithServiceImplAndPaginationUpdatableFieldsEquals(
            expectedEntityWithServiceImplAndPagination,
            getPersistedEntityWithServiceImplAndPagination(expectedEntityWithServiceImplAndPagination)
        );
    }

    protected void assertPersistedEntityWithServiceImplAndPaginationToMatchUpdatableProperties(
        EntityWithServiceImplAndPagination expectedEntityWithServiceImplAndPagination
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithServiceImplAndPaginationAllUpdatablePropertiesEquals(expectedEntityWithServiceImplAndPagination, getPersistedEntityWithServiceImplAndPagination(expectedEntityWithServiceImplAndPagination));
        assertEntityWithServiceImplAndPaginationUpdatableFieldsEquals(
            expectedEntityWithServiceImplAndPagination,
            getPersistedEntityWithServiceImplAndPagination(expectedEntityWithServiceImplAndPagination)
        );
    }
}
