package com.mycompany.myapp.app.child.web.rest;

import static com.mycompany.myapp.app.child.domain.CustomPackageChildAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.app.child.domain.CustomPackageChild;
import com.mycompany.myapp.app.child.repository.CustomPackageChildRepository;
import com.mycompany.myapp.app.child.service.dto.CustomPackageChildDTO;
import com.mycompany.myapp.app.child.service.mapper.CustomPackageChildMapper;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.UserRepository;
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
 * Integration tests for the {@link CustomPackageChildResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CustomPackageChildResourceIT {

    private static final String DEFAULT_CHILD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHILD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/custom-package-children";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomPackageChildRepository customPackageChildRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomPackageChildMapper customPackageChildMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CustomPackageChild customPackageChild;

    private CustomPackageChild insertedCustomPackageChild;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomPackageChild createEntity() {
        return new CustomPackageChild().childName(DEFAULT_CHILD_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomPackageChild createUpdatedEntity() {
        return new CustomPackageChild().childName(UPDATED_CHILD_NAME);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CustomPackageChild.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        customPackageChild = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCustomPackageChild != null) {
            customPackageChildRepository.delete(insertedCustomPackageChild).block();
            insertedCustomPackageChild = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createCustomPackageChild() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomPackageChild
        CustomPackageChildDTO customPackageChildDTO = customPackageChildMapper.toDto(customPackageChild);
        var returnedCustomPackageChildDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageChildDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CustomPackageChildDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the CustomPackageChild in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomPackageChild = customPackageChildMapper.toEntity(returnedCustomPackageChildDTO);
        assertCustomPackageChildUpdatableFieldsEquals(
            returnedCustomPackageChild,
            getPersistedCustomPackageChild(returnedCustomPackageChild)
        );

        insertedCustomPackageChild = returnedCustomPackageChild;
    }

    @Test
    void createCustomPackageChildWithExistingId() throws Exception {
        // Create the CustomPackageChild with an existing ID
        customPackageChild.setId(1L);
        CustomPackageChildDTO customPackageChildDTO = customPackageChildMapper.toDto(customPackageChild);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageChildDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomPackageChild in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCustomPackageChildrenAsStream() {
        // Initialize the database
        customPackageChildRepository.save(customPackageChild).block();

        List<CustomPackageChild> customPackageChildList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CustomPackageChildDTO.class)
            .getResponseBody()
            .map(customPackageChildMapper::toEntity)
            .filter(customPackageChild::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(customPackageChildList).isNotNull();
        assertThat(customPackageChildList).hasSize(1);
        CustomPackageChild testCustomPackageChild = customPackageChildList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertCustomPackageChildAllPropertiesEquals(customPackageChild, testCustomPackageChild);
        assertCustomPackageChildUpdatableFieldsEquals(customPackageChild, testCustomPackageChild);
    }

    @Test
    void getAllCustomPackageChildren() {
        // Initialize the database
        insertedCustomPackageChild = customPackageChildRepository.save(customPackageChild).block();

        // Get all the customPackageChildList
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
            .value(hasItem(customPackageChild.getId().intValue()))
            .jsonPath("$.[*].childName")
            .value(hasItem(DEFAULT_CHILD_NAME));
    }

    @Test
    void getCustomPackageChild() {
        // Initialize the database
        insertedCustomPackageChild = customPackageChildRepository.save(customPackageChild).block();

        // Get the customPackageChild
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, customPackageChild.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(customPackageChild.getId().intValue()))
            .jsonPath("$.childName")
            .value(is(DEFAULT_CHILD_NAME));
    }

    @Test
    void getNonExistingCustomPackageChild() {
        // Get the customPackageChild
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCustomPackageChild() throws Exception {
        // Initialize the database
        insertedCustomPackageChild = customPackageChildRepository.save(customPackageChild).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customPackageChild
        CustomPackageChild updatedCustomPackageChild = customPackageChildRepository.findById(customPackageChild.getId()).block();
        updatedCustomPackageChild.childName(UPDATED_CHILD_NAME);
        CustomPackageChildDTO customPackageChildDTO = customPackageChildMapper.toDto(updatedCustomPackageChild);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, customPackageChildDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageChildDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CustomPackageChild in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomPackageChildToMatchAllProperties(updatedCustomPackageChild);
    }

    @Test
    void putNonExistingCustomPackageChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageChild.setId(longCount.incrementAndGet());

        // Create the CustomPackageChild
        CustomPackageChildDTO customPackageChildDTO = customPackageChildMapper.toDto(customPackageChild);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, customPackageChildDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageChildDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomPackageChild in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCustomPackageChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageChild.setId(longCount.incrementAndGet());

        // Create the CustomPackageChild
        CustomPackageChildDTO customPackageChildDTO = customPackageChildMapper.toDto(customPackageChild);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageChildDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomPackageChild in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCustomPackageChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageChild.setId(longCount.incrementAndGet());

        // Create the CustomPackageChild
        CustomPackageChildDTO customPackageChildDTO = customPackageChildMapper.toDto(customPackageChild);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(customPackageChildDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CustomPackageChild in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCustomPackageChildWithPatch() throws Exception {
        // Initialize the database
        insertedCustomPackageChild = customPackageChildRepository.save(customPackageChild).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customPackageChild using partial update
        CustomPackageChild partialUpdatedCustomPackageChild = new CustomPackageChild();
        partialUpdatedCustomPackageChild.setId(customPackageChild.getId());

        partialUpdatedCustomPackageChild.childName(UPDATED_CHILD_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCustomPackageChild.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCustomPackageChild))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CustomPackageChild in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomPackageChildUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomPackageChild, customPackageChild),
            getPersistedCustomPackageChild(customPackageChild)
        );
    }

    @Test
    void fullUpdateCustomPackageChildWithPatch() throws Exception {
        // Initialize the database
        insertedCustomPackageChild = customPackageChildRepository.save(customPackageChild).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customPackageChild using partial update
        CustomPackageChild partialUpdatedCustomPackageChild = new CustomPackageChild();
        partialUpdatedCustomPackageChild.setId(customPackageChild.getId());

        partialUpdatedCustomPackageChild.childName(UPDATED_CHILD_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCustomPackageChild.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCustomPackageChild))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CustomPackageChild in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomPackageChildUpdatableFieldsEquals(
            partialUpdatedCustomPackageChild,
            getPersistedCustomPackageChild(partialUpdatedCustomPackageChild)
        );
    }

    @Test
    void patchNonExistingCustomPackageChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageChild.setId(longCount.incrementAndGet());

        // Create the CustomPackageChild
        CustomPackageChildDTO customPackageChildDTO = customPackageChildMapper.toDto(customPackageChild);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, customPackageChildDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(customPackageChildDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomPackageChild in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCustomPackageChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageChild.setId(longCount.incrementAndGet());

        // Create the CustomPackageChild
        CustomPackageChildDTO customPackageChildDTO = customPackageChildMapper.toDto(customPackageChild);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(customPackageChildDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomPackageChild in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCustomPackageChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customPackageChild.setId(longCount.incrementAndGet());

        // Create the CustomPackageChild
        CustomPackageChildDTO customPackageChildDTO = customPackageChildMapper.toDto(customPackageChild);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(customPackageChildDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CustomPackageChild in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCustomPackageChild() {
        // Initialize the database
        insertedCustomPackageChild = customPackageChildRepository.save(customPackageChild).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customPackageChild
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, customPackageChild.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customPackageChildRepository.count().block();
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

    protected CustomPackageChild getPersistedCustomPackageChild(CustomPackageChild customPackageChild) {
        return customPackageChildRepository.findById(customPackageChild.getId()).block();
    }

    protected void assertPersistedCustomPackageChildToMatchAllProperties(CustomPackageChild expectedCustomPackageChild) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCustomPackageChildAllPropertiesEquals(expectedCustomPackageChild, getPersistedCustomPackageChild(expectedCustomPackageChild));
        assertCustomPackageChildUpdatableFieldsEquals(
            expectedCustomPackageChild,
            getPersistedCustomPackageChild(expectedCustomPackageChild)
        );
    }

    protected void assertPersistedCustomPackageChildToMatchUpdatableProperties(CustomPackageChild expectedCustomPackageChild) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCustomPackageChildAllUpdatablePropertiesEquals(expectedCustomPackageChild, getPersistedCustomPackageChild(expectedCustomPackageChild));
        assertCustomPackageChildUpdatableFieldsEquals(
            expectedCustomPackageChild,
            getPersistedCustomPackageChild(expectedCustomPackageChild)
        );
    }
}
