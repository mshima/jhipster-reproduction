package com.mycompany.myapp.app.custom.web.rest;

import static com.mycompany.myapp.app.custom.domain.CustomPackageParentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.app.custom.domain.CustomPackageParent;
import com.mycompany.myapp.app.custom.repository.CustomPackageParentRepository;
import com.mycompany.myapp.app.custom.service.dto.CustomPackageParentDTO;
import com.mycompany.myapp.app.custom.service.mapper.CustomPackageParentMapper;
import com.mycompany.myapp.repository.EntityManager;
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

/**
 * Integration tests for the {@link CustomPackageParentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CustomPackageParentResourceIT {

    private static final String DEFAULT_PARENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/custom-package-parents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomPackageParentRepository customPackageParentRepository;

    @Autowired
    private CustomPackageParentMapper customPackageParentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CustomPackageParent customPackageParent;

    private CustomPackageParent insertedCustomPackageParent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomPackageParent createEntity() {
        return new CustomPackageParent().parentName(DEFAULT_PARENT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomPackageParent createUpdatedEntity() {
        return new CustomPackageParent().parentName(UPDATED_PARENT_NAME);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CustomPackageParent.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        customPackageParent = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCustomPackageParent != null) {
            customPackageParentRepository.delete(insertedCustomPackageParent).block();
            insertedCustomPackageParent = null;
        }
        deleteEntities(em);
    }

    @Test
    void createCustomPackageParent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomPackageParent
        CustomPackageParentDTO customPackageParentDTO = customPackageParentMapper.toDto(customPackageParent);
        var returnedCustomPackageParentDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageParentDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CustomPackageParentDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the CustomPackageParent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomPackageParent = customPackageParentMapper.toEntity(returnedCustomPackageParentDTO);
        assertCustomPackageParentUpdatableFieldsEquals(
            returnedCustomPackageParent,
            getPersistedCustomPackageParent(returnedCustomPackageParent)
        );

        insertedCustomPackageParent = returnedCustomPackageParent;
    }

    @Test
    void createCustomPackageParentWithExistingId() throws Exception {
        // Create the CustomPackageParent with an existing ID
        customPackageParent.setId(1L);
        CustomPackageParentDTO customPackageParentDTO = customPackageParentMapper.toDto(customPackageParent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageParentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomPackageParent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCustomPackageParentsAsStream() {
        // Initialize the database
        customPackageParentRepository.save(customPackageParent).block();

        List<CustomPackageParent> customPackageParentList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CustomPackageParentDTO.class)
            .getResponseBody()
            .map(customPackageParentMapper::toEntity)
            .filter(customPackageParent::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(customPackageParentList).isNotNull();
        assertThat(customPackageParentList).hasSize(1);
        CustomPackageParent testCustomPackageParent = customPackageParentList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertCustomPackageParentAllPropertiesEquals(customPackageParent, testCustomPackageParent);
        assertCustomPackageParentUpdatableFieldsEquals(customPackageParent, testCustomPackageParent);
    }

    @Test
    void getAllCustomPackageParents() {
        // Initialize the database
        insertedCustomPackageParent = customPackageParentRepository.save(customPackageParent).block();

        // Get all the customPackageParentList
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
            .value(hasItem(customPackageParent.getId().intValue()))
            .jsonPath("$.[*].parentName")
            .value(hasItem(DEFAULT_PARENT_NAME));
    }

    @Test
    void getCustomPackageParent() {
        // Initialize the database
        insertedCustomPackageParent = customPackageParentRepository.save(customPackageParent).block();

        // Get the customPackageParent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, customPackageParent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(customPackageParent.getId().intValue()))
            .jsonPath("$.parentName")
            .value(is(DEFAULT_PARENT_NAME));
    }

    @Test
    void getNonExistingCustomPackageParent() {
        // Get the customPackageParent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCustomPackageParent() throws Exception {
        // Initialize the database
        insertedCustomPackageParent = customPackageParentRepository.save(customPackageParent).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customPackageParent
        CustomPackageParent updatedCustomPackageParent = customPackageParentRepository.findById(customPackageParent.getId()).block();
        updatedCustomPackageParent.parentName(UPDATED_PARENT_NAME);
        CustomPackageParentDTO customPackageParentDTO = customPackageParentMapper.toDto(updatedCustomPackageParent);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, customPackageParentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageParentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CustomPackageParent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomPackageParentToMatchAllProperties(updatedCustomPackageParent);
    }

    @Test
    void putNonExistingCustomPackageParent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageParent.setId(longCount.incrementAndGet());

        // Create the CustomPackageParent
        CustomPackageParentDTO customPackageParentDTO = customPackageParentMapper.toDto(customPackageParent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, customPackageParentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageParentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomPackageParent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCustomPackageParent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageParent.setId(longCount.incrementAndGet());

        // Create the CustomPackageParent
        CustomPackageParentDTO customPackageParentDTO = customPackageParentMapper.toDto(customPackageParent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageParentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomPackageParent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCustomPackageParent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageParent.setId(longCount.incrementAndGet());

        // Create the CustomPackageParent
        CustomPackageParentDTO customPackageParentDTO = customPackageParentMapper.toDto(customPackageParent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageParentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CustomPackageParent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCustomPackageParentWithPatch() throws Exception {
        // Initialize the database
        insertedCustomPackageParent = customPackageParentRepository.save(customPackageParent).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customPackageParent using partial update
        CustomPackageParent partialUpdatedCustomPackageParent = new CustomPackageParent();
        partialUpdatedCustomPackageParent.setId(customPackageParent.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCustomPackageParent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCustomPackageParent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CustomPackageParent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomPackageParentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomPackageParent, customPackageParent),
            getPersistedCustomPackageParent(customPackageParent)
        );
    }

    @Test
    void fullUpdateCustomPackageParentWithPatch() throws Exception {
        // Initialize the database
        insertedCustomPackageParent = customPackageParentRepository.save(customPackageParent).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customPackageParent using partial update
        CustomPackageParent partialUpdatedCustomPackageParent = new CustomPackageParent();
        partialUpdatedCustomPackageParent.setId(customPackageParent.getId());

        partialUpdatedCustomPackageParent.parentName(UPDATED_PARENT_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCustomPackageParent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCustomPackageParent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CustomPackageParent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomPackageParentUpdatableFieldsEquals(
            partialUpdatedCustomPackageParent,
            getPersistedCustomPackageParent(partialUpdatedCustomPackageParent)
        );
    }

    @Test
    void patchNonExistingCustomPackageParent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageParent.setId(longCount.incrementAndGet());

        // Create the CustomPackageParent
        CustomPackageParentDTO customPackageParentDTO = customPackageParentMapper.toDto(customPackageParent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, customPackageParentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(customPackageParentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomPackageParent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCustomPackageParent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageParent.setId(longCount.incrementAndGet());

        // Create the CustomPackageParent
        CustomPackageParentDTO customPackageParentDTO = customPackageParentMapper.toDto(customPackageParent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(customPackageParentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomPackageParent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCustomPackageParent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageParent.setId(longCount.incrementAndGet());

        // Create the CustomPackageParent
        CustomPackageParentDTO customPackageParentDTO = customPackageParentMapper.toDto(customPackageParent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(customPackageParentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CustomPackageParent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCustomPackageParent() {
        // Initialize the database
        insertedCustomPackageParent = customPackageParentRepository.save(customPackageParent).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customPackageParent
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, customPackageParent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customPackageParentRepository.count().block();
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

    protected CustomPackageParent getPersistedCustomPackageParent(CustomPackageParent customPackageParent) {
        return customPackageParentRepository.findById(customPackageParent.getId()).block();
    }

    protected void assertPersistedCustomPackageParentToMatchAllProperties(CustomPackageParent expectedCustomPackageParent) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCustomPackageParentAllPropertiesEquals(expectedCustomPackageParent, getPersistedCustomPackageParent(expectedCustomPackageParent));
        assertCustomPackageParentUpdatableFieldsEquals(
            expectedCustomPackageParent,
            getPersistedCustomPackageParent(expectedCustomPackageParent)
        );
    }

    protected void assertPersistedCustomPackageParentToMatchUpdatableProperties(CustomPackageParent expectedCustomPackageParent) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCustomPackageParentAllUpdatablePropertiesEquals(expectedCustomPackageParent, getPersistedCustomPackageParent(expectedCustomPackageParent));
        assertCustomPackageParentUpdatableFieldsEquals(
            expectedCustomPackageParent,
            getPersistedCustomPackageParent(expectedCustomPackageParent)
        );
    }
}
