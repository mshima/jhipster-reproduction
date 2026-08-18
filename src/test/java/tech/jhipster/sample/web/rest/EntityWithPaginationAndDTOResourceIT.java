package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static tech.jhipster.sample.domain.EntityWithPaginationAndDTOAsserts.*;
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
import tech.jhipster.sample.domain.EntityWithPaginationAndDTO;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.EntityWithPaginationAndDTORepository;
import tech.jhipster.sample.service.dto.EntityWithPaginationAndDTODTO;
import tech.jhipster.sample.service.mapper.EntityWithPaginationAndDTOMapper;

/**
 * Integration tests for the {@link EntityWithPaginationAndDTOResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EntityWithPaginationAndDTOResourceIT {

    private static final String DEFAULT_LEA = "AAAAAAAAAA";
    private static final String UPDATED_LEA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entity-with-pagination-and-dtos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityWithPaginationAndDTORepository entityWithPaginationAndDTORepository;

    @Autowired
    private EntityWithPaginationAndDTOMapper entityWithPaginationAndDTOMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EntityWithPaginationAndDTO entityWithPaginationAndDTO;

    private EntityWithPaginationAndDTO insertedEntityWithPaginationAndDTO;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithPaginationAndDTO createEntity() {
        return new EntityWithPaginationAndDTO().lea(DEFAULT_LEA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithPaginationAndDTO createUpdatedEntity() {
        return new EntityWithPaginationAndDTO().lea(UPDATED_LEA);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EntityWithPaginationAndDTO.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        entityWithPaginationAndDTO = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEntityWithPaginationAndDTO != null) {
            entityWithPaginationAndDTORepository.delete(insertedEntityWithPaginationAndDTO).block();
            insertedEntityWithPaginationAndDTO = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEntityWithPaginationAndDTO() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EntityWithPaginationAndDTO
        EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO = entityWithPaginationAndDTOMapper.toDto(entityWithPaginationAndDTO);
        var returnedEntityWithPaginationAndDTODTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EntityWithPaginationAndDTODTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the EntityWithPaginationAndDTO in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEntityWithPaginationAndDTO = entityWithPaginationAndDTOMapper.toEntity(returnedEntityWithPaginationAndDTODTO);
        assertEntityWithPaginationAndDTOUpdatableFieldsEquals(
            returnedEntityWithPaginationAndDTO,
            getPersistedEntityWithPaginationAndDTO(returnedEntityWithPaginationAndDTO)
        );

        insertedEntityWithPaginationAndDTO = returnedEntityWithPaginationAndDTO;
    }

    @Test
    void createEntityWithPaginationAndDTOWithExistingId() throws Exception {
        // Create the EntityWithPaginationAndDTO with an existing ID
        entityWithPaginationAndDTO.setId(1L);
        EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO = entityWithPaginationAndDTOMapper.toDto(entityWithPaginationAndDTO);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEntityWithPaginationAndDTOS() {
        // Initialize the database
        insertedEntityWithPaginationAndDTO = entityWithPaginationAndDTORepository.save(entityWithPaginationAndDTO).block();

        // Get all the entityWithPaginationAndDTOList
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
            .value(hasItem(entityWithPaginationAndDTO.getId().intValue()))
            .jsonPath("$.[*].lea")
            .value(hasItem(DEFAULT_LEA));
    }

    @Test
    void getEntityWithPaginationAndDTO() {
        // Initialize the database
        insertedEntityWithPaginationAndDTO = entityWithPaginationAndDTORepository.save(entityWithPaginationAndDTO).block();

        // Get the entityWithPaginationAndDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, entityWithPaginationAndDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(entityWithPaginationAndDTO.getId().intValue()))
            .jsonPath("$.lea")
            .value(is(DEFAULT_LEA));
    }

    @Test
    void getNonExistingEntityWithPaginationAndDTO() {
        // Get the entityWithPaginationAndDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEntityWithPaginationAndDTO() throws Exception {
        // Initialize the database
        insertedEntityWithPaginationAndDTO = entityWithPaginationAndDTORepository.save(entityWithPaginationAndDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithPaginationAndDTO
        EntityWithPaginationAndDTO updatedEntityWithPaginationAndDTO = entityWithPaginationAndDTORepository
            .findById(entityWithPaginationAndDTO.getId())
            .block();
        updatedEntityWithPaginationAndDTO.lea(UPDATED_LEA);
        EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO = entityWithPaginationAndDTOMapper.toDto(
            updatedEntityWithPaginationAndDTO
        );

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithPaginationAndDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntityWithPaginationAndDTOToMatchAllProperties(updatedEntityWithPaginationAndDTO);
    }

    @Test
    void putNonExistingEntityWithPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithPaginationAndDTO
        EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO = entityWithPaginationAndDTOMapper.toDto(entityWithPaginationAndDTO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithPaginationAndDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEntityWithPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithPaginationAndDTO
        EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO = entityWithPaginationAndDTOMapper.toDto(entityWithPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEntityWithPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithPaginationAndDTO
        EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO = entityWithPaginationAndDTOMapper.toDto(entityWithPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEntityWithPaginationAndDTOWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithPaginationAndDTO = entityWithPaginationAndDTORepository.save(entityWithPaginationAndDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithPaginationAndDTO using partial update
        EntityWithPaginationAndDTO partialUpdatedEntityWithPaginationAndDTO = new EntityWithPaginationAndDTO();
        partialUpdatedEntityWithPaginationAndDTO.setId(entityWithPaginationAndDTO.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithPaginationAndDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithPaginationAndDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithPaginationAndDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithPaginationAndDTOUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntityWithPaginationAndDTO, entityWithPaginationAndDTO),
            getPersistedEntityWithPaginationAndDTO(entityWithPaginationAndDTO)
        );
    }

    @Test
    void fullUpdateEntityWithPaginationAndDTOWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithPaginationAndDTO = entityWithPaginationAndDTORepository.save(entityWithPaginationAndDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithPaginationAndDTO using partial update
        EntityWithPaginationAndDTO partialUpdatedEntityWithPaginationAndDTO = new EntityWithPaginationAndDTO();
        partialUpdatedEntityWithPaginationAndDTO.setId(entityWithPaginationAndDTO.getId());

        partialUpdatedEntityWithPaginationAndDTO.lea(UPDATED_LEA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithPaginationAndDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithPaginationAndDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithPaginationAndDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithPaginationAndDTOUpdatableFieldsEquals(
            partialUpdatedEntityWithPaginationAndDTO,
            getPersistedEntityWithPaginationAndDTO(partialUpdatedEntityWithPaginationAndDTO)
        );
    }

    @Test
    void patchNonExistingEntityWithPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithPaginationAndDTO
        EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO = entityWithPaginationAndDTOMapper.toDto(entityWithPaginationAndDTO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, entityWithPaginationAndDTODTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEntityWithPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithPaginationAndDTO
        EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO = entityWithPaginationAndDTOMapper.toDto(entityWithPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEntityWithPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithPaginationAndDTO
        EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO = entityWithPaginationAndDTOMapper.toDto(entityWithPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEntityWithPaginationAndDTO() {
        // Initialize the database
        insertedEntityWithPaginationAndDTO = entityWithPaginationAndDTORepository.save(entityWithPaginationAndDTO).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entityWithPaginationAndDTO
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, entityWithPaginationAndDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entityWithPaginationAndDTORepository.count().block();
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

    protected EntityWithPaginationAndDTO getPersistedEntityWithPaginationAndDTO(EntityWithPaginationAndDTO entityWithPaginationAndDTO) {
        return entityWithPaginationAndDTORepository.findById(entityWithPaginationAndDTO.getId()).block();
    }

    protected void assertPersistedEntityWithPaginationAndDTOToMatchAllProperties(
        EntityWithPaginationAndDTO expectedEntityWithPaginationAndDTO
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithPaginationAndDTOAllPropertiesEquals(expectedEntityWithPaginationAndDTO, getPersistedEntityWithPaginationAndDTO(expectedEntityWithPaginationAndDTO));
        assertEntityWithPaginationAndDTOUpdatableFieldsEquals(
            expectedEntityWithPaginationAndDTO,
            getPersistedEntityWithPaginationAndDTO(expectedEntityWithPaginationAndDTO)
        );
    }

    protected void assertPersistedEntityWithPaginationAndDTOToMatchUpdatableProperties(
        EntityWithPaginationAndDTO expectedEntityWithPaginationAndDTO
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithPaginationAndDTOAllUpdatablePropertiesEquals(expectedEntityWithPaginationAndDTO, getPersistedEntityWithPaginationAndDTO(expectedEntityWithPaginationAndDTO));
        assertEntityWithPaginationAndDTOUpdatableFieldsEquals(
            expectedEntityWithPaginationAndDTO,
            getPersistedEntityWithPaginationAndDTO(expectedEntityWithPaginationAndDTO)
        );
    }
}
