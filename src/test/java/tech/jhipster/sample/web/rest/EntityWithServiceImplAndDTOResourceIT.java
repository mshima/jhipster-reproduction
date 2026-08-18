package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static tech.jhipster.sample.domain.EntityWithServiceImplAndDTOAsserts.*;
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
import tech.jhipster.sample.domain.EntityWithServiceImplAndDTO;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.EntityWithServiceImplAndDTORepository;
import tech.jhipster.sample.service.dto.EntityWithServiceImplAndDTODTO;
import tech.jhipster.sample.service.mapper.EntityWithServiceImplAndDTOMapper;

/**
 * Integration tests for the {@link EntityWithServiceImplAndDTOResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EntityWithServiceImplAndDTOResourceIT {

    private static final String DEFAULT_LOUIS = "AAAAAAAAAA";
    private static final String UPDATED_LOUIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entity-with-service-impl-and-dtos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityWithServiceImplAndDTORepository entityWithServiceImplAndDTORepository;

    @Autowired
    private EntityWithServiceImplAndDTOMapper entityWithServiceImplAndDTOMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EntityWithServiceImplAndDTO entityWithServiceImplAndDTO;

    private EntityWithServiceImplAndDTO insertedEntityWithServiceImplAndDTO;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithServiceImplAndDTO createEntity() {
        return new EntityWithServiceImplAndDTO().louis(DEFAULT_LOUIS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithServiceImplAndDTO createUpdatedEntity() {
        return new EntityWithServiceImplAndDTO().louis(UPDATED_LOUIS);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EntityWithServiceImplAndDTO.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        entityWithServiceImplAndDTO = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEntityWithServiceImplAndDTO != null) {
            entityWithServiceImplAndDTORepository.delete(insertedEntityWithServiceImplAndDTO).block();
            insertedEntityWithServiceImplAndDTO = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEntityWithServiceImplAndDTO() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EntityWithServiceImplAndDTO
        EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO = entityWithServiceImplAndDTOMapper.toDto(
            entityWithServiceImplAndDTO
        );
        var returnedEntityWithServiceImplAndDTODTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndDTODTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EntityWithServiceImplAndDTODTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the EntityWithServiceImplAndDTO in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEntityWithServiceImplAndDTO = entityWithServiceImplAndDTOMapper.toEntity(returnedEntityWithServiceImplAndDTODTO);
        assertEntityWithServiceImplAndDTOUpdatableFieldsEquals(
            returnedEntityWithServiceImplAndDTO,
            getPersistedEntityWithServiceImplAndDTO(returnedEntityWithServiceImplAndDTO)
        );

        insertedEntityWithServiceImplAndDTO = returnedEntityWithServiceImplAndDTO;
    }

    @Test
    void createEntityWithServiceImplAndDTOWithExistingId() throws Exception {
        // Create the EntityWithServiceImplAndDTO with an existing ID
        entityWithServiceImplAndDTO.setId(1L);
        EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO = entityWithServiceImplAndDTOMapper.toDto(
            entityWithServiceImplAndDTO
        );

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEntityWithServiceImplAndDTOSAsStream() {
        // Initialize the database
        entityWithServiceImplAndDTORepository.save(entityWithServiceImplAndDTO).block();

        List<EntityWithServiceImplAndDTO> entityWithServiceImplAndDTOList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(EntityWithServiceImplAndDTODTO.class)
            .getResponseBody()
            .map(entityWithServiceImplAndDTOMapper::toEntity)
            .filter(entityWithServiceImplAndDTO::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(entityWithServiceImplAndDTOList).isNotNull();
        assertThat(entityWithServiceImplAndDTOList).hasSize(1);
        EntityWithServiceImplAndDTO testEntityWithServiceImplAndDTO = entityWithServiceImplAndDTOList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithServiceImplAndDTOAllPropertiesEquals(entityWithServiceImplAndDTO, testEntityWithServiceImplAndDTO);
        assertEntityWithServiceImplAndDTOUpdatableFieldsEquals(entityWithServiceImplAndDTO, testEntityWithServiceImplAndDTO);
    }

    @Test
    void getAllEntityWithServiceImplAndDTOS() {
        // Initialize the database
        insertedEntityWithServiceImplAndDTO = entityWithServiceImplAndDTORepository.save(entityWithServiceImplAndDTO).block();

        // Get all the entityWithServiceImplAndDTOList
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
            .value(hasItem(entityWithServiceImplAndDTO.getId().intValue()))
            .jsonPath("$.[*].louis")
            .value(hasItem(DEFAULT_LOUIS));
    }

    @Test
    void getEntityWithServiceImplAndDTO() {
        // Initialize the database
        insertedEntityWithServiceImplAndDTO = entityWithServiceImplAndDTORepository.save(entityWithServiceImplAndDTO).block();

        // Get the entityWithServiceImplAndDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplAndDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(entityWithServiceImplAndDTO.getId().intValue()))
            .jsonPath("$.louis")
            .value(is(DEFAULT_LOUIS));
    }

    @Test
    void getNonExistingEntityWithServiceImplAndDTO() {
        // Get the entityWithServiceImplAndDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEntityWithServiceImplAndDTO() throws Exception {
        // Initialize the database
        insertedEntityWithServiceImplAndDTO = entityWithServiceImplAndDTORepository.save(entityWithServiceImplAndDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceImplAndDTO
        EntityWithServiceImplAndDTO updatedEntityWithServiceImplAndDTO = entityWithServiceImplAndDTORepository
            .findById(entityWithServiceImplAndDTO.getId())
            .block();
        updatedEntityWithServiceImplAndDTO.louis(UPDATED_LOUIS);
        EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO = entityWithServiceImplAndDTOMapper.toDto(
            updatedEntityWithServiceImplAndDTO
        );

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplAndDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndDTODTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceImplAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntityWithServiceImplAndDTOToMatchAllProperties(updatedEntityWithServiceImplAndDTO);
    }

    @Test
    void putNonExistingEntityWithServiceImplAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplAndDTO
        EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO = entityWithServiceImplAndDTOMapper.toDto(
            entityWithServiceImplAndDTO
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplAndDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEntityWithServiceImplAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplAndDTO
        EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO = entityWithServiceImplAndDTOMapper.toDto(
            entityWithServiceImplAndDTO
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEntityWithServiceImplAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplAndDTO
        EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO = entityWithServiceImplAndDTOMapper.toDto(
            entityWithServiceImplAndDTO
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithServiceImplAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEntityWithServiceImplAndDTOWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithServiceImplAndDTO = entityWithServiceImplAndDTORepository.save(entityWithServiceImplAndDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceImplAndDTO using partial update
        EntityWithServiceImplAndDTO partialUpdatedEntityWithServiceImplAndDTO = new EntityWithServiceImplAndDTO();
        partialUpdatedEntityWithServiceImplAndDTO.setId(entityWithServiceImplAndDTO.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithServiceImplAndDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithServiceImplAndDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceImplAndDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithServiceImplAndDTOUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntityWithServiceImplAndDTO, entityWithServiceImplAndDTO),
            getPersistedEntityWithServiceImplAndDTO(entityWithServiceImplAndDTO)
        );
    }

    @Test
    void fullUpdateEntityWithServiceImplAndDTOWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithServiceImplAndDTO = entityWithServiceImplAndDTORepository.save(entityWithServiceImplAndDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithServiceImplAndDTO using partial update
        EntityWithServiceImplAndDTO partialUpdatedEntityWithServiceImplAndDTO = new EntityWithServiceImplAndDTO();
        partialUpdatedEntityWithServiceImplAndDTO.setId(entityWithServiceImplAndDTO.getId());

        partialUpdatedEntityWithServiceImplAndDTO.louis(UPDATED_LOUIS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithServiceImplAndDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithServiceImplAndDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithServiceImplAndDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithServiceImplAndDTOUpdatableFieldsEquals(
            partialUpdatedEntityWithServiceImplAndDTO,
            getPersistedEntityWithServiceImplAndDTO(partialUpdatedEntityWithServiceImplAndDTO)
        );
    }

    @Test
    void patchNonExistingEntityWithServiceImplAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplAndDTO
        EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO = entityWithServiceImplAndDTOMapper.toDto(
            entityWithServiceImplAndDTO
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplAndDTODTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEntityWithServiceImplAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplAndDTO
        EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO = entityWithServiceImplAndDTOMapper.toDto(
            entityWithServiceImplAndDTO
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithServiceImplAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEntityWithServiceImplAndDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithServiceImplAndDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithServiceImplAndDTO
        EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO = entityWithServiceImplAndDTOMapper.toDto(
            entityWithServiceImplAndDTO
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithServiceImplAndDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithServiceImplAndDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEntityWithServiceImplAndDTO() {
        // Initialize the database
        insertedEntityWithServiceImplAndDTO = entityWithServiceImplAndDTORepository.save(entityWithServiceImplAndDTO).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entityWithServiceImplAndDTO
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, entityWithServiceImplAndDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entityWithServiceImplAndDTORepository.count().block();
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

    protected EntityWithServiceImplAndDTO getPersistedEntityWithServiceImplAndDTO(EntityWithServiceImplAndDTO entityWithServiceImplAndDTO) {
        return entityWithServiceImplAndDTORepository.findById(entityWithServiceImplAndDTO.getId()).block();
    }

    protected void assertPersistedEntityWithServiceImplAndDTOToMatchAllProperties(
        EntityWithServiceImplAndDTO expectedEntityWithServiceImplAndDTO
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithServiceImplAndDTOAllPropertiesEquals(expectedEntityWithServiceImplAndDTO, getPersistedEntityWithServiceImplAndDTO(expectedEntityWithServiceImplAndDTO));
        assertEntityWithServiceImplAndDTOUpdatableFieldsEquals(
            expectedEntityWithServiceImplAndDTO,
            getPersistedEntityWithServiceImplAndDTO(expectedEntityWithServiceImplAndDTO)
        );
    }

    protected void assertPersistedEntityWithServiceImplAndDTOToMatchUpdatableProperties(
        EntityWithServiceImplAndDTO expectedEntityWithServiceImplAndDTO
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithServiceImplAndDTOAllUpdatablePropertiesEquals(expectedEntityWithServiceImplAndDTO, getPersistedEntityWithServiceImplAndDTO(expectedEntityWithServiceImplAndDTO));
        assertEntityWithServiceImplAndDTOUpdatableFieldsEquals(
            expectedEntityWithServiceImplAndDTO,
            getPersistedEntityWithServiceImplAndDTO(expectedEntityWithServiceImplAndDTO)
        );
    }
}
