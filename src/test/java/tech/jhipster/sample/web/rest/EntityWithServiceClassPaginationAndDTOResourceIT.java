package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static tech.jhipster.sample.domain.EntityWithServiceClassPaginationAndDTOAsserts.*;
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
import tech.jhipster.sample.domain.EntityWithServiceClassPaginationAndDTO;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.EntityWithServiceClassPaginationAndDTORepository;
import tech.jhipster.sample.service.dto.EntityWithServiceClassPaginationAndDTODTO;
import tech.jhipster.sample.service.mapper.EntityWithServiceClassPaginationAndDTOMapper;

/**
 * Integration tests for the {@link EntityWithServiceClassPaginationAndDTOResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EntityWithServiceClassPaginationAndDTOResourceIT {

    private static final String DEFAULT_LENA = "AAAAAAAAAA";
    private static final String UPDATED_LENA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entity-with-service-class-pagination-and-dtos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityWithServiceClassPaginationAndDTORepository entityWithServiceClassPaginationAndDTORepository;

    @Autowired
    private EntityWithServiceClassPaginationAndDTOMapper entityWithServiceClassPaginationAndDTOMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EntityWithServiceClassPaginationAndDTO entityWithServiceClassPaginationAndDTO;

    private EntityWithServiceClassPaginationAndDTO insertedEntityWithServiceClassPaginationAndDTO;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithServiceClassPaginationAndDTO createEntity() {
        return new EntityWithServiceClassPaginationAndDTO().lena(DEFAULT_LENA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithServiceClassPaginationAndDTO createUpdatedEntity() {
        return new EntityWithServiceClassPaginationAndDTO().lena(UPDATED_LENA);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EntityWithServiceClassPaginationAndDTO.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        entityWithServiceClassPaginationAndDTO = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEntityWithServiceClassPaginationAndDTO != null) {
            entityWithServiceClassPaginationAndDTORepository.delete(insertedEntityWithServiceClassPaginationAndDTO).block();
            insertedEntityWithServiceClassPaginationAndDTO = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEntityWithServiceClassPaginationAndDTO() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EntityWithServiceClassPaginationAndDTO
        EntityWithServiceClassPaginationAndDTODTO entityWithServiceClassPaginationAndDTODTO =
            entityWithServiceClassPaginationAndDTOMapper.toDto(entityWithServiceClassPaginationAndDTO);
        var returnedEntityWithServiceClassPaginationAndDTODTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EntityWithServiceClassPaginationAndDTODTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the EntityWithServiceClassPaginationAndDTO in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEntityWithServiceClassPaginationAndDTO = entityWithServiceClassPaginationAndDTOMapper.toEntity(
            returnedEntityWithServiceClassPaginationAndDTODTO
        );
        assertEntityWithServiceClassPaginationAndDTOUpdatableFieldsEquals(
            returnedEntityWithServiceClassPaginationAndDTO,
            getPersistedEntityWithServiceClassPaginationAndDTO(returnedEntityWithServiceClassPaginationAndDTO)
        );

        insertedEntityWithServiceClassPaginationAndDTO = returnedEntityWithServiceClassPaginationAndDTO;
    }

    @Test
    void createEntityWithServiceClassPaginationAndDTOWithExistingId() throws Exception {
        // Create the EntityWithServiceClassPaginationAndDTO with an existing ID
        entityWithServiceClassPaginationAndDTO.setId(1L);
        EntityWithServiceClassPaginationAndDTODTO entityWithServiceClassPaginationAndDTODTO =
            entityWithServiceClassPaginationAndDTOMapper.toDto(entityWithServiceClassPaginationAndDTO);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceClassPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEntityWithServiceClassPaginationAndDTOS() {
        // Initialize the database
        insertedEntityWithServiceClassPaginationAndDTO = entityWithServiceClassPaginationAndDTORepository
            .save(entityWithServiceClassPaginationAndDTO)
            .block();

        // Get all the entityWithServiceClassPaginationAndDTOList
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
            .value(hasItem(entityWithServiceClassPaginationAndDTO.getId().intValue()))
            .jsonPath("$.[*].lena")
            .value(hasItem(DEFAULT_LENA));
    }

    @Test
    void getEntityWithServiceClassPaginationAndDTO() {
        // Initialize the database
        insertedEntityWithServiceClassPaginationAndDTO = entityWithServiceClassPaginationAndDTORepository
            .save(entityWithServiceClassPaginationAndDTO)
            .block();

        // Get the entityWithServiceClassPaginationAndDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, entityWithServiceClassPaginationAndDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(entityWithServiceClassPaginationAndDTO.getId().intValue()))
            .jsonPath("$.lena")
            .value(is(DEFAULT_LENA));
    }

    @Test
    void getNonExistingEntityWithServiceClassPaginationAndDTO() {
        // Get the entityWithServiceClassPaginationAndDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEntityWithServiceClassPaginationAndDTO() throws Exception {
        // Initialize the database
        insertedEntityWithServiceClassPaginationAndDTO = entityWithServiceClassPaginationAndDTORepository
            .save(entityWithServiceClassPaginationAndDTO)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceClassPaginationAndDTO
        EntityWithServiceClassPaginationAndDTO updatedEntityWithServiceClassPaginationAndDTO =
            entityWithServiceClassPaginationAndDTORepository.findById(entityWithServiceClassPaginationAndDTO.getId()).block();
        updatedEntityWithServiceClassPaginationAndDTO.lena(UPDATED_LENA);
        EntityWithServiceClassPaginationAndDTODTO entityWithServiceClassPaginationAndDTODTO =
            entityWithServiceClassPaginationAndDTOMapper.toDto(updatedEntityWithServiceClassPaginationAndDTO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithServiceClassPaginationAndDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceClassPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntityWithServiceClassPaginationAndDTOToMatchAllProperties(updatedEntityWithServiceClassPaginationAndDTO);
    }

    @Test
    void putNonExistingEntityWithServiceClassPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceClassPaginationAndDTO
        EntityWithServiceClassPaginationAndDTODTO entityWithServiceClassPaginationAndDTODTO =
            entityWithServiceClassPaginationAndDTOMapper.toDto(entityWithServiceClassPaginationAndDTO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithServiceClassPaginationAndDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceClassPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEntityWithServiceClassPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceClassPaginationAndDTO
        EntityWithServiceClassPaginationAndDTODTO entityWithServiceClassPaginationAndDTODTO =
            entityWithServiceClassPaginationAndDTOMapper.toDto(entityWithServiceClassPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceClassPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEntityWithServiceClassPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceClassPaginationAndDTO
        EntityWithServiceClassPaginationAndDTODTO entityWithServiceClassPaginationAndDTODTO =
            entityWithServiceClassPaginationAndDTOMapper.toDto(entityWithServiceClassPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithServiceClassPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEntityWithServiceClassPaginationAndDTOWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithServiceClassPaginationAndDTO = entityWithServiceClassPaginationAndDTORepository
            .save(entityWithServiceClassPaginationAndDTO)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceClassPaginationAndDTO using partial update
        EntityWithServiceClassPaginationAndDTO partialUpdatedEntityWithServiceClassPaginationAndDTO =
            new EntityWithServiceClassPaginationAndDTO();
        partialUpdatedEntityWithServiceClassPaginationAndDTO.setId(entityWithServiceClassPaginationAndDTO.getId());

        partialUpdatedEntityWithServiceClassPaginationAndDTO.lena(UPDATED_LENA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithServiceClassPaginationAndDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithServiceClassPaginationAndDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceClassPaginationAndDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithServiceClassPaginationAndDTOUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntityWithServiceClassPaginationAndDTO, entityWithServiceClassPaginationAndDTO),
            getPersistedEntityWithServiceClassPaginationAndDTO(entityWithServiceClassPaginationAndDTO)
        );
    }

    @Test
    void fullUpdateEntityWithServiceClassPaginationAndDTOWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithServiceClassPaginationAndDTO = entityWithServiceClassPaginationAndDTORepository
            .save(entityWithServiceClassPaginationAndDTO)
            .block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceClassPaginationAndDTO using partial update
        EntityWithServiceClassPaginationAndDTO partialUpdatedEntityWithServiceClassPaginationAndDTO =
            new EntityWithServiceClassPaginationAndDTO();
        partialUpdatedEntityWithServiceClassPaginationAndDTO.setId(entityWithServiceClassPaginationAndDTO.getId());

        partialUpdatedEntityWithServiceClassPaginationAndDTO.lena(UPDATED_LENA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithServiceClassPaginationAndDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithServiceClassPaginationAndDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceClassPaginationAndDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithServiceClassPaginationAndDTOUpdatableFieldsEquals(
            partialUpdatedEntityWithServiceClassPaginationAndDTO,
            getPersistedEntityWithServiceClassPaginationAndDTO(partialUpdatedEntityWithServiceClassPaginationAndDTO)
        );
    }

    @Test
    void patchNonExistingEntityWithServiceClassPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceClassPaginationAndDTO
        EntityWithServiceClassPaginationAndDTODTO entityWithServiceClassPaginationAndDTODTO =
            entityWithServiceClassPaginationAndDTOMapper.toDto(entityWithServiceClassPaginationAndDTO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, entityWithServiceClassPaginationAndDTODTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceClassPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEntityWithServiceClassPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceClassPaginationAndDTO
        EntityWithServiceClassPaginationAndDTODTO entityWithServiceClassPaginationAndDTODTO =
            entityWithServiceClassPaginationAndDTOMapper.toDto(entityWithServiceClassPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceClassPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEntityWithServiceClassPaginationAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceClassPaginationAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceClassPaginationAndDTO
        EntityWithServiceClassPaginationAndDTODTO entityWithServiceClassPaginationAndDTODTO =
            entityWithServiceClassPaginationAndDTOMapper.toDto(entityWithServiceClassPaginationAndDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceClassPaginationAndDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithServiceClassPaginationAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEntityWithServiceClassPaginationAndDTO() {
        // Initialize the database
        insertedEntityWithServiceClassPaginationAndDTO = entityWithServiceClassPaginationAndDTORepository
            .save(entityWithServiceClassPaginationAndDTO)
            .block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entityWithServiceClassPaginationAndDTO
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, entityWithServiceClassPaginationAndDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entityWithServiceClassPaginationAndDTORepository.count().block();
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

    protected EntityWithServiceClassPaginationAndDTO getPersistedEntityWithServiceClassPaginationAndDTO(
        EntityWithServiceClassPaginationAndDTO entityWithServiceClassPaginationAndDTO
    ) {
        return entityWithServiceClassPaginationAndDTORepository.findById(entityWithServiceClassPaginationAndDTO.getId()).block();
    }

    protected void assertPersistedEntityWithServiceClassPaginationAndDTOToMatchAllProperties(
        EntityWithServiceClassPaginationAndDTO expectedEntityWithServiceClassPaginationAndDTO
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithServiceClassPaginationAndDTOAllPropertiesEquals(expectedEntityWithServiceClassPaginationAndDTO, getPersistedEntityWithServiceClassPaginationAndDTO(expectedEntityWithServiceClassPaginationAndDTO));
        assertEntityWithServiceClassPaginationAndDTOUpdatableFieldsEquals(
            expectedEntityWithServiceClassPaginationAndDTO,
            getPersistedEntityWithServiceClassPaginationAndDTO(expectedEntityWithServiceClassPaginationAndDTO)
        );
    }

    protected void assertPersistedEntityWithServiceClassPaginationAndDTOToMatchUpdatableProperties(
        EntityWithServiceClassPaginationAndDTO expectedEntityWithServiceClassPaginationAndDTO
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithServiceClassPaginationAndDTOAllUpdatablePropertiesEquals(expectedEntityWithServiceClassPaginationAndDTO, getPersistedEntityWithServiceClassPaginationAndDTO(expectedEntityWithServiceClassPaginationAndDTO));
        assertEntityWithServiceClassPaginationAndDTOUpdatableFieldsEquals(
            expectedEntityWithServiceClassPaginationAndDTO,
            getPersistedEntityWithServiceClassPaginationAndDTO(expectedEntityWithServiceClassPaginationAndDTO)
        );
    }
}
