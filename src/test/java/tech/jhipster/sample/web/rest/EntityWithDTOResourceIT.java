package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static tech.jhipster.sample.domain.EntityWithDTOAsserts.*;
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
import tech.jhipster.sample.domain.EntityWithDTO;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.EntityWithDTORepository;
import tech.jhipster.sample.service.dto.EntityWithDTODTO;
import tech.jhipster.sample.service.mapper.EntityWithDTOMapper;

/**
 * Integration tests for the {@link EntityWithDTOResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EntityWithDTOResourceIT {

    private static final String DEFAULT_EMMA = "AAAAAAAAAA";
    private static final String UPDATED_EMMA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entity-with-dtos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityWithDTORepository entityWithDTORepository;

    @Autowired
    private EntityWithDTOMapper entityWithDTOMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EntityWithDTO entityWithDTO;

    private EntityWithDTO insertedEntityWithDTO;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithDTO createEntity() {
        return new EntityWithDTO().emma(DEFAULT_EMMA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityWithDTO createUpdatedEntity() {
        return new EntityWithDTO().emma(UPDATED_EMMA);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EntityWithDTO.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        entityWithDTO = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEntityWithDTO != null) {
            entityWithDTORepository.delete(insertedEntityWithDTO).block();
            insertedEntityWithDTO = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEntityWithDTO() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EntityWithDTO
        EntityWithDTODTO entityWithDTODTO = entityWithDTOMapper.toDto(entityWithDTO);
        var returnedEntityWithDTODTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithDTODTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EntityWithDTODTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the EntityWithDTO in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEntityWithDTO = entityWithDTOMapper.toEntity(returnedEntityWithDTODTO);
        assertEntityWithDTOUpdatableFieldsEquals(returnedEntityWithDTO, getPersistedEntityWithDTO(returnedEntityWithDTO));

        insertedEntityWithDTO = returnedEntityWithDTO;
    }

    @Test
    void createEntityWithDTOWithExistingId() throws Exception {
        // Create the EntityWithDTO with an existing ID
        entityWithDTO.setId(1L);
        EntityWithDTODTO entityWithDTODTO = entityWithDTOMapper.toDto(entityWithDTO);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEntityWithDTOSAsStream() {
        // Initialize the database
        entityWithDTORepository.save(entityWithDTO).block();

        List<EntityWithDTO> entityWithDTOList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(EntityWithDTODTO.class)
            .getResponseBody()
            .map(entityWithDTOMapper::toEntity)
            .filter(entityWithDTO::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(entityWithDTOList).isNotNull();
        assertThat(entityWithDTOList).hasSize(1);
        EntityWithDTO testEntityWithDTO = entityWithDTOList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithDTOAllPropertiesEquals(entityWithDTO, testEntityWithDTO);
        assertEntityWithDTOUpdatableFieldsEquals(entityWithDTO, testEntityWithDTO);
    }

    @Test
    void getAllEntityWithDTOS() {
        // Initialize the database
        insertedEntityWithDTO = entityWithDTORepository.save(entityWithDTO).block();

        // Get all the entityWithDTOList
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
            .value(hasItem(entityWithDTO.getId().intValue()))
            .jsonPath("$.[*].emma")
            .value(hasItem(DEFAULT_EMMA));
    }

    @Test
    void getEntityWithDTO() {
        // Initialize the database
        insertedEntityWithDTO = entityWithDTORepository.save(entityWithDTO).block();

        // Get the entityWithDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, entityWithDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(entityWithDTO.getId().intValue()))
            .jsonPath("$.emma")
            .value(is(DEFAULT_EMMA));
    }

    @Test
    void getNonExistingEntityWithDTO() {
        // Get the entityWithDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEntityWithDTO() throws Exception {
        // Initialize the database
        insertedEntityWithDTO = entityWithDTORepository.save(entityWithDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithDTO
        EntityWithDTO updatedEntityWithDTO = entityWithDTORepository.findById(entityWithDTO.getId()).block();
        updatedEntityWithDTO.emma(UPDATED_EMMA);
        EntityWithDTODTO entityWithDTODTO = entityWithDTOMapper.toDto(updatedEntityWithDTO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithDTODTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntityWithDTOToMatchAllProperties(updatedEntityWithDTO);
    }

    @Test
    void putNonExistingEntityWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithDTO
        EntityWithDTODTO entityWithDTODTO = entityWithDTOMapper.toDto(entityWithDTO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entityWithDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEntityWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithDTO
        EntityWithDTODTO entityWithDTODTO = entityWithDTOMapper.toDto(entityWithDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEntityWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithDTO
        EntityWithDTODTO entityWithDTODTO = entityWithDTOMapper.toDto(entityWithDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entityWithDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEntityWithDTOWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithDTO = entityWithDTORepository.save(entityWithDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithDTO using partial update
        EntityWithDTO partialUpdatedEntityWithDTO = new EntityWithDTO();
        partialUpdatedEntityWithDTO.setId(entityWithDTO.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithDTOUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntityWithDTO, entityWithDTO),
            getPersistedEntityWithDTO(entityWithDTO)
        );
    }

    @Test
    void fullUpdateEntityWithDTOWithPatch() throws Exception {
        // Initialize the database
        insertedEntityWithDTO = entityWithDTORepository.save(entityWithDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityWithDTO using partial update
        EntityWithDTO partialUpdatedEntityWithDTO = new EntityWithDTO();
        partialUpdatedEntityWithDTO.setId(entityWithDTO.getId());

        partialUpdatedEntityWithDTO.emma(UPDATED_EMMA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntityWithDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntityWithDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntityWithDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityWithDTOUpdatableFieldsEquals(partialUpdatedEntityWithDTO, getPersistedEntityWithDTO(partialUpdatedEntityWithDTO));
    }

    @Test
    void patchNonExistingEntityWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithDTO
        EntityWithDTODTO entityWithDTODTO = entityWithDTOMapper.toDto(entityWithDTO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, entityWithDTODTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEntityWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithDTO
        EntityWithDTODTO entityWithDTODTO = entityWithDTOMapper.toDto(entityWithDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntityWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEntityWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityWithDTO.setId(longCount.incrementAndGet());

        // Create the EntityWithDTO
        EntityWithDTODTO entityWithDTODTO = entityWithDTOMapper.toDto(entityWithDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entityWithDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntityWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEntityWithDTO() {
        // Initialize the database
        insertedEntityWithDTO = entityWithDTORepository.save(entityWithDTO).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entityWithDTO
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, entityWithDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entityWithDTORepository.count().block();
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

    protected EntityWithDTO getPersistedEntityWithDTO(EntityWithDTO entityWithDTO) {
        return entityWithDTORepository.findById(entityWithDTO.getId()).block();
    }

    protected void assertPersistedEntityWithDTOToMatchAllProperties(EntityWithDTO expectedEntityWithDTO) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithDTOAllPropertiesEquals(expectedEntityWithDTO, getPersistedEntityWithDTO(expectedEntityWithDTO));
        assertEntityWithDTOUpdatableFieldsEquals(expectedEntityWithDTO, getPersistedEntityWithDTO(expectedEntityWithDTO));
    }

    protected void assertPersistedEntityWithDTOToMatchUpdatableProperties(EntityWithDTO expectedEntityWithDTO) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntityWithDTOAllUpdatablePropertiesEquals(expectedEntityWithDTO, getPersistedEntityWithDTO(expectedEntityWithDTO));
        assertEntityWithDTOUpdatableFieldsEquals(expectedEntityWithDTO, getPersistedEntityWithDTO(expectedEntityWithDTO));
    }
}
