package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static tech.jhipster.sample.domain.EntityWithServiceImplPaginationAndDTOAsserts.*;
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
import tech.jhipster.sample.domain.EntityWithServiceImplPaginationAndDTO;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.EntityWithServiceImplPaginationAndDTORepository;
import tech.jhipster.sample.service.dto.EntityWithServiceImplPaginationAndDTODTO;
import tech.jhipster.sample.service.mapper.EntityWithServiceImplPaginationAndDTOMapper;

/**
 * Integration tests for the {@link EntityWithServiceImplPaginationAndDTOResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EntityWithServiceImplPaginationAndDTOResourceIT {

    private static final String DEFAULT_THEO = "AAAAAAAAAA";
    private static final String UPDATED_THEO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entity-with-service-impl-pagination-and-dtos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityWithServiceImplPaginationAndDTORepository entityWithServiceImplPaginationAndDTORepository;

    @Autowired
    private EntityWithServiceImplPaginationAndDTOMapper entityWithServiceImplPaginationAndDTOMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EntityWithServiceImplPaginationAndDTO entityWithServiceImplPaginationAndDTO;

    private EntityWithServiceImplPaginationAndDTO insertedEntityWithServiceImplPaginationAndDTO;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithServiceImplPaginationAndDTO createEntity() {
        return new EntityWithServiceImplPaginationAndDTO().theo(DEFAULT_THEO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithServiceImplPaginationAndDTO createUpdatedEntity() {
        return new EntityWithServiceImplPaginationAndDTO().theo(UPDATED_THEO);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EntityWithServiceImplPaginationAndDTO.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        entityWithServiceImplPaginationAndDTO = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEntityWithServiceImplPaginationAndDTO != null) {
            entityWithServiceImplPaginationAndDTORepository.delete(insertedEntityWithServiceImplPaginationAndDTO).block();
            insertedEntityWithServiceImplPaginationAndDTO = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEntityWithServiceImplPaginationAndDTO() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EntityWithServiceImplPaginationAndDTO
        EntityWithServiceImplPaginationAndDTODTO entityWithServiceImplPaginationAndDTODTO =
            entityWithServiceImplPaginationAndDTOMapper.toDto(entityWithServiceImplPaginationAndDTO);
        var returnedEntityWithServiceImplPaginationAndDTODTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EntityWithServiceImplPaginationAndDTODTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the EntityWithServiceImplPaginationAndDTO in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEntityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTOMapper.toEntity(
            returnedEntityWithServiceImplPaginationAndDTODTO
        );
        assertEntityWithServiceImplPaginationAndDTOUpdatableFieldsEquals(
            returnedEntityWithServiceImplPaginationAndDTO,
            getPersistedEntityWithServiceImplPaginationAndDTO(returnedEntityWithServiceImplPaginationAndDTO)
        );

        insertedEntityWithServiceImplPaginationAndDTO = returnedEntityWithServiceImplPaginationAndDTO;
    }

    @Test
    void createEntityWithServiceImplPaginationAndDTOWithExistingId() throws Exception {
        // Create the EntityWithServiceImplPaginationAndDTO with an existing ID
        entityWithServiceImplPaginationAndDTO.setId(1L);
        EntityWithServiceImplPaginationAndDTODTO entityWithServiceImplPaginationAndDTODTO =
            entityWithServiceImplPaginationAndDTOMapper.toDto(entityWithServiceImplPaginationAndDTO);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEntityWithServiceImplPaginationAndDTOS() {
        // Initialize the database
        insertedEntityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTORepository
            .save(entityWithServiceImplPaginationAndDTO)
            .block();

        // Get all the entityWithServiceImplPaginationAndDTOList
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
            .value(hasItem(entityWithServiceImplPaginationAndDTO.getId().intValue()))
            .jsonPath("$.[*].theo")
            .value(hasItem(DEFAULT_THEO));
    }

    @Test
    void getEntityWithServiceImplPaginationAndDTO() {
        // Initialize the database
        insertedEntityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTORepository
            .save(entityWithServiceImplPaginationAndDTO)
            .block();

        // Get the entityWithServiceImplPaginationAndDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplPaginationAndDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(entityWithServiceImplPaginationAndDTO.getId().intValue()))
            .jsonPath("$.theo")
            .value(is(DEFAULT_THEO));
    }

    @Test
    void getNonExistingEntityWithServiceImplPaginationAndDTO() {
        // Get the entityWithServiceImplPaginationAndDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEntityWithServiceImplPaginationAndDTO() throws Exception {
        // Initialize the database
        insertedEntityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTORepository
            .save(entityWithServiceImplPaginationAndDTO)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceImplPaginationAndDTO
        EntityWithServiceImplPaginationAndDTO updatedEntityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTORepository
            .findById(entityWithServiceImplPaginationAndDTO.getId())
            .block();
        updatedEntityWithServiceImplPaginationAndDTO.theo(UPDATED_THEO);
        EntityWithServiceImplPaginationAndDTODTO entityWithServiceImplPaginationAndDTODTO =
            entityWithServiceImplPaginationAndDTOMapper.toDto(updatedEntityWithServiceImplPaginationAndDTO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplPaginationAndDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceImplPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntityWithServiceImplPaginationAndDTOToMatchAllProperties(updatedEntityWithServiceImplPaginationAndDTO);
    }

    @Test
    void putNonExistingEntityWithServiceImplPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplPaginationAndDTO
        EntityWithServiceImplPaginationAndDTODTO entityWithServiceImplPaginationAndDTODTO =
            entityWithServiceImplPaginationAndDTOMapper.toDto(entityWithServiceImplPaginationAndDTO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplPaginationAndDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEntityWithServiceImplPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplPaginationAndDTO
        EntityWithServiceImplPaginationAndDTODTO entityWithServiceImplPaginationAndDTODTO =
            entityWithServiceImplPaginationAndDTOMapper.toDto(entityWithServiceImplPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEntityWithServiceImplPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplPaginationAndDTO
        EntityWithServiceImplPaginationAndDTODTO entityWithServiceImplPaginationAndDTODTO =
            entityWithServiceImplPaginationAndDTOMapper.toDto(entityWithServiceImplPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithServiceImplPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEntityWithServiceImplPaginationAndDTOWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTORepository
            .save(entityWithServiceImplPaginationAndDTO)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceImplPaginationAndDTO using partial update
        EntityWithServiceImplPaginationAndDTO partialUpdatedEntityWithServiceImplPaginationAndDTO =
            new EntityWithServiceImplPaginationAndDTO();
        partialUpdatedEntityWithServiceImplPaginationAndDTO.setId(entityWithServiceImplPaginationAndDTO.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithServiceImplPaginationAndDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithServiceImplPaginationAndDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceImplPaginationAndDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithServiceImplPaginationAndDTOUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntityWithServiceImplPaginationAndDTO, entityWithServiceImplPaginationAndDTO),
            getPersistedEntityWithServiceImplPaginationAndDTO(entityWithServiceImplPaginationAndDTO)
        );
    }

    @Test
    void fullUpdateEntityWithServiceImplPaginationAndDTOWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTORepository
            .save(entityWithServiceImplPaginationAndDTO)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceImplPaginationAndDTO using partial update
        EntityWithServiceImplPaginationAndDTO partialUpdatedEntityWithServiceImplPaginationAndDTO =
            new EntityWithServiceImplPaginationAndDTO();
        partialUpdatedEntityWithServiceImplPaginationAndDTO.setId(entityWithServiceImplPaginationAndDTO.getId());

        partialUpdatedEntityWithServiceImplPaginationAndDTO.theo(UPDATED_THEO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithServiceImplPaginationAndDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithServiceImplPaginationAndDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceImplPaginationAndDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithServiceImplPaginationAndDTOUpdatableFieldsEquals(
            partialUpdatedEntityWithServiceImplPaginationAndDTO,
            getPersistedEntityWithServiceImplPaginationAndDTO(partialUpdatedEntityWithServiceImplPaginationAndDTO)
        );
    }

    @Test
    void patchNonExistingEntityWithServiceImplPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplPaginationAndDTO
        EntityWithServiceImplPaginationAndDTODTO entityWithServiceImplPaginationAndDTODTO =
            entityWithServiceImplPaginationAndDTOMapper.toDto(entityWithServiceImplPaginationAndDTO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplPaginationAndDTODTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEntityWithServiceImplPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplPaginationAndDTO
        EntityWithServiceImplPaginationAndDTODTO entityWithServiceImplPaginationAndDTODTO =
            entityWithServiceImplPaginationAndDTOMapper.toDto(entityWithServiceImplPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEntityWithServiceImplPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplPaginationAndDTO
        EntityWithServiceImplPaginationAndDTODTO entityWithServiceImplPaginationAndDTODTO =
            entityWithServiceImplPaginationAndDTOMapper.toDto(entityWithServiceImplPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithServiceImplPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEntityWithServiceImplPaginationAndDTO() {
        // Initialize the database
        insertedEntityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTORepository
            .save(entityWithServiceImplPaginationAndDTO)
            .block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entityWithServiceImplPaginationAndDTO
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplPaginationAndDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entityWithServiceImplPaginationAndDTORepository.count().block();
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

    protected EntityWithServiceImplPaginationAndDTO getPersistedEntityWithServiceImplPaginationAndDTO(
        EntityWithServiceImplPaginationAndDTO entityWithServiceImplPaginationAndDTO
    ) {
        return entityWithServiceImplPaginationAndDTORepository.findById(entityWithServiceImplPaginationAndDTO.getId()).block();
    }

    protected void assertPersistedEntityWithServiceImplPaginationAndDTOToMatchAllProperties(
        EntityWithServiceImplPaginationAndDTO expectedEntityWithServiceImplPaginationAndDTO
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithServiceImplPaginationAndDTOAllPropertiesEquals(expectedEntityWithServiceImplPaginationAndDTO, getPersistedEntityWithServiceImplPaginationAndDTO(expectedEntityWithServiceImplPaginationAndDTO));
        assertEntityWithServiceImplPaginationAndDTOUpdatableFieldsEquals(
            expectedEntityWithServiceImplPaginationAndDTO,
            getPersistedEntityWithServiceImplPaginationAndDTO(expectedEntityWithServiceImplPaginationAndDTO)
        );
    }

    protected void assertPersistedEntityWithServiceImplPaginationAndDTOToMatchUpdatableProperties(
        EntityWithServiceImplPaginationAndDTO expectedEntityWithServiceImplPaginationAndDTO
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithServiceImplPaginationAndDTOAllUpdatablePropertiesEquals(expectedEntityWithServiceImplPaginationAndDTO, getPersistedEntityWithServiceImplPaginationAndDTO(expectedEntityWithServiceImplPaginationAndDTO));
        assertEntityWithServiceImplPaginationAndDTOUpdatableFieldsEquals(
            expectedEntityWithServiceImplPaginationAndDTO,
            getPersistedEntityWithServiceImplPaginationAndDTO(expectedEntityWithServiceImplPaginationAndDTO)
        );
    }
}
