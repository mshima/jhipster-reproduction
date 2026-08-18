package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static tech.jhipster.sample.domain.MapsIdUserProfileWithDTOAsserts.*;
import static tech.jhipster.sample.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.IntegrationTest;
import tech.jhipster.sample.domain.MapsIdUserProfileWithDTO;
import tech.jhipster.sample.domain.User;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.MapsIdUserProfileWithDTORepository;
import tech.jhipster.sample.repository.UserRepository;
import tech.jhipster.sample.service.MapsIdUserProfileWithDTOService;
import tech.jhipster.sample.service.dto.MapsIdUserProfileWithDTODTO;
import tech.jhipster.sample.service.mapper.MapsIdUserProfileWithDTOMapper;

/**
 * Integration tests for the {@link MapsIdUserProfileWithDTOResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MapsIdUserProfileWithDTOResourceIT {

    private static final Instant DEFAULT_DATE_OF_BIRTH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OF_BIRTH = Instant.ofEpochMilli(1596513172471L);

    private static final String ENTITY_API_URL = "/api/maps-id-user-profile-with-dtos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MapsIdUserProfileWithDTORepository mapsIdUserProfileWithDTORepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private MapsIdUserProfileWithDTORepository mapsIdUserProfileWithDTORepositoryMock;

    @Autowired
    private MapsIdUserProfileWithDTOMapper mapsIdUserProfileWithDTOMapper;

    @Mock
    private MapsIdUserProfileWithDTOService mapsIdUserProfileWithDTOServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MapsIdUserProfileWithDTO mapsIdUserProfileWithDTO;

    private MapsIdUserProfileWithDTO insertedMapsIdUserProfileWithDTO;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MapsIdUserProfileWithDTO createEntity(EntityManager em) {
        MapsIdUserProfileWithDTO mapsIdUserProfileWithDTO = new MapsIdUserProfileWithDTO().dateOfBirth(DEFAULT_DATE_OF_BIRTH);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity()).block();
        mapsIdUserProfileWithDTO.setUser(user);
        return mapsIdUserProfileWithDTO;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MapsIdUserProfileWithDTO createUpdatedEntity(EntityManager em) {
        MapsIdUserProfileWithDTO updatedMapsIdUserProfileWithDTO = new MapsIdUserProfileWithDTO().dateOfBirth(UPDATED_DATE_OF_BIRTH);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity()).block();
        updatedMapsIdUserProfileWithDTO.setUser(user);
        return updatedMapsIdUserProfileWithDTO;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MapsIdUserProfileWithDTO.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        UserResourceIT.deleteEntities(em);
    }

    @BeforeEach
    void initTest() {
        mapsIdUserProfileWithDTO = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMapsIdUserProfileWithDTO != null) {
            mapsIdUserProfileWithDTORepository.delete(insertedMapsIdUserProfileWithDTO).block();
            insertedMapsIdUserProfileWithDTO = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createMapsIdUserProfileWithDTO() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MapsIdUserProfileWithDTO
        MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO = mapsIdUserProfileWithDTOMapper.toDto(mapsIdUserProfileWithDTO);
        var returnedMapsIdUserProfileWithDTODTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mapsIdUserProfileWithDTODTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(MapsIdUserProfileWithDTODTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the MapsIdUserProfileWithDTO in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMapsIdUserProfileWithDTO = mapsIdUserProfileWithDTOMapper.toEntity(returnedMapsIdUserProfileWithDTODTO);
        assertMapsIdUserProfileWithDTOUpdatableFieldsEquals(
            returnedMapsIdUserProfileWithDTO,
            getPersistedMapsIdUserProfileWithDTO(returnedMapsIdUserProfileWithDTO)
        );

        assertMapsIdUserProfileWithDTOMapsIdRelationshipPersistedValue(mapsIdUserProfileWithDTO, returnedMapsIdUserProfileWithDTO);

        insertedMapsIdUserProfileWithDTO = returnedMapsIdUserProfileWithDTO;
    }

    @Test
    void createMapsIdUserProfileWithDTOWithExistingId() throws Exception {
        // Create the MapsIdUserProfileWithDTO with an existing ID
        mapsIdUserProfileWithDTO.setId(1L);
        MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO = mapsIdUserProfileWithDTOMapper.toDto(mapsIdUserProfileWithDTO);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mapsIdUserProfileWithDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MapsIdUserProfileWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void updateMapsIdUserProfileWithDTOMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        insertedMapsIdUserProfileWithDTO = mapsIdUserProfileWithDTORepository.save(mapsIdUserProfileWithDTO).block();
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Add a new parent entity
        User user = UserResourceIT.createEntity();

        // Load the mapsIdUserProfileWithDTO
        MapsIdUserProfileWithDTO updatedMapsIdUserProfileWithDTO = mapsIdUserProfileWithDTORepository
            .findById(mapsIdUserProfileWithDTO.getId())
            .block();
        assertThat(updatedMapsIdUserProfileWithDTO).isNotNull();

        // Update the User with new association value
        updatedMapsIdUserProfileWithDTO.setUser(user);
        MapsIdUserProfileWithDTODTO updatedMapsIdUserProfileWithDTODTO = mapsIdUserProfileWithDTOMapper.toDto(
            updatedMapsIdUserProfileWithDTO
        );
        assertThat(updatedMapsIdUserProfileWithDTODTO).isNotNull();

        // Update the entity
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMapsIdUserProfileWithDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedMapsIdUserProfileWithDTODTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MapsIdUserProfileWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);

        /**
         * Validate the id for MapsId, the ids must be same
         * Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
         * Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
         * assertThat(testMapsIdUserProfileWithDTO.getId()).isEqualTo(testMapsIdUserProfileWithDTO.getUser().getId());
         */
    }

    @Test
    void getAllMapsIdUserProfileWithDTOSAsStream() {
        // Initialize the database
        mapsIdUserProfileWithDTORepository.save(mapsIdUserProfileWithDTO).block();

        List<MapsIdUserProfileWithDTO> mapsIdUserProfileWithDTOList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(MapsIdUserProfileWithDTODTO.class)
            .getResponseBody()
            .map(mapsIdUserProfileWithDTOMapper::toEntity)
            .filter(mapsIdUserProfileWithDTO::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(mapsIdUserProfileWithDTOList).isNotNull();
        assertThat(mapsIdUserProfileWithDTOList).hasSize(1);
        MapsIdUserProfileWithDTO testMapsIdUserProfileWithDTO = mapsIdUserProfileWithDTOList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertMapsIdUserProfileWithDTOAllPropertiesEquals(mapsIdUserProfileWithDTO, testMapsIdUserProfileWithDTO);
        assertMapsIdUserProfileWithDTOUpdatableFieldsEquals(mapsIdUserProfileWithDTO, testMapsIdUserProfileWithDTO);
    }

    @Test
    void getAllMapsIdUserProfileWithDTOS() {
        // Initialize the database
        insertedMapsIdUserProfileWithDTO = mapsIdUserProfileWithDTORepository.save(mapsIdUserProfileWithDTO).block();

        // Get all the mapsIdUserProfileWithDTOList
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
            .value(hasItem(mapsIdUserProfileWithDTO.getId().intValue()))
            .jsonPath("$.[*].dateOfBirth")
            .value(hasItem(DEFAULT_DATE_OF_BIRTH.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMapsIdUserProfileWithDTOSWithEagerRelationshipsIsEnabled() {
        when(mapsIdUserProfileWithDTOServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?eagerload=true")
            .exchange()
            .expectStatus()
            .isOk();

        verify(mapsIdUserProfileWithDTOServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMapsIdUserProfileWithDTOSWithEagerRelationshipsIsNotEnabled() {
        when(mapsIdUserProfileWithDTOServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?eagerload=false")
            .exchange()
            .expectStatus()
            .isOk();
        verify(mapsIdUserProfileWithDTORepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getMapsIdUserProfileWithDTO() {
        // Initialize the database
        insertedMapsIdUserProfileWithDTO = mapsIdUserProfileWithDTORepository.save(mapsIdUserProfileWithDTO).block();

        // Get the mapsIdUserProfileWithDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, mapsIdUserProfileWithDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(mapsIdUserProfileWithDTO.getId().intValue()))
            .jsonPath("$.dateOfBirth")
            .value(is(DEFAULT_DATE_OF_BIRTH.toString()));
    }

    @Test
    void getNonExistingMapsIdUserProfileWithDTO() {
        // Get the mapsIdUserProfileWithDTO
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMapsIdUserProfileWithDTO() throws Exception {
        // Initialize the database
        insertedMapsIdUserProfileWithDTO = mapsIdUserProfileWithDTORepository.save(mapsIdUserProfileWithDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mapsIdUserProfileWithDTO
        MapsIdUserProfileWithDTO updatedMapsIdUserProfileWithDTO = mapsIdUserProfileWithDTORepository
            .findById(mapsIdUserProfileWithDTO.getId())
            .block();
        updatedMapsIdUserProfileWithDTO.dateOfBirth(UPDATED_DATE_OF_BIRTH);
        MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO = mapsIdUserProfileWithDTOMapper.toDto(updatedMapsIdUserProfileWithDTO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, mapsIdUserProfileWithDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mapsIdUserProfileWithDTODTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MapsIdUserProfileWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMapsIdUserProfileWithDTOToMatchAllProperties(updatedMapsIdUserProfileWithDTO);
    }

    @Test
    void putNonExistingMapsIdUserProfileWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapsIdUserProfileWithDTO.setId(longCount.incrementAndGet());

        // Create the MapsIdUserProfileWithDTO
        MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO = mapsIdUserProfileWithDTOMapper.toDto(mapsIdUserProfileWithDTO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, mapsIdUserProfileWithDTODTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mapsIdUserProfileWithDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MapsIdUserProfileWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMapsIdUserProfileWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapsIdUserProfileWithDTO.setId(longCount.incrementAndGet());

        // Create the MapsIdUserProfileWithDTO
        MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO = mapsIdUserProfileWithDTOMapper.toDto(mapsIdUserProfileWithDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mapsIdUserProfileWithDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MapsIdUserProfileWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMapsIdUserProfileWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapsIdUserProfileWithDTO.setId(longCount.incrementAndGet());

        // Create the MapsIdUserProfileWithDTO
        MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO = mapsIdUserProfileWithDTOMapper.toDto(mapsIdUserProfileWithDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mapsIdUserProfileWithDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MapsIdUserProfileWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMapsIdUserProfileWithDTOWithPatch() throws Exception {
        // Initialize the database
        insertedMapsIdUserProfileWithDTO = mapsIdUserProfileWithDTORepository.save(mapsIdUserProfileWithDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mapsIdUserProfileWithDTO using partial update
        MapsIdUserProfileWithDTO partialUpdatedMapsIdUserProfileWithDTO = new MapsIdUserProfileWithDTO();
        partialUpdatedMapsIdUserProfileWithDTO.setId(mapsIdUserProfileWithDTO.getId());

        partialUpdatedMapsIdUserProfileWithDTO.dateOfBirth(UPDATED_DATE_OF_BIRTH);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMapsIdUserProfileWithDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMapsIdUserProfileWithDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MapsIdUserProfileWithDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMapsIdUserProfileWithDTOUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMapsIdUserProfileWithDTO, mapsIdUserProfileWithDTO),
            getPersistedMapsIdUserProfileWithDTO(mapsIdUserProfileWithDTO)
        );
    }

    @Test
    void fullUpdateMapsIdUserProfileWithDTOWithPatch() throws Exception {
        // Initialize the database
        insertedMapsIdUserProfileWithDTO = mapsIdUserProfileWithDTORepository.save(mapsIdUserProfileWithDTO).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mapsIdUserProfileWithDTO using partial update
        MapsIdUserProfileWithDTO partialUpdatedMapsIdUserProfileWithDTO = new MapsIdUserProfileWithDTO();
        partialUpdatedMapsIdUserProfileWithDTO.setId(mapsIdUserProfileWithDTO.getId());

        partialUpdatedMapsIdUserProfileWithDTO.dateOfBirth(UPDATED_DATE_OF_BIRTH);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMapsIdUserProfileWithDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMapsIdUserProfileWithDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MapsIdUserProfileWithDTO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMapsIdUserProfileWithDTOUpdatableFieldsEquals(
            partialUpdatedMapsIdUserProfileWithDTO,
            getPersistedMapsIdUserProfileWithDTO(partialUpdatedMapsIdUserProfileWithDTO)
        );
    }

    @Test
    void patchNonExistingMapsIdUserProfileWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapsIdUserProfileWithDTO.setId(longCount.incrementAndGet());

        // Create the MapsIdUserProfileWithDTO
        MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO = mapsIdUserProfileWithDTOMapper.toDto(mapsIdUserProfileWithDTO);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, mapsIdUserProfileWithDTODTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(mapsIdUserProfileWithDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MapsIdUserProfileWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMapsIdUserProfileWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapsIdUserProfileWithDTO.setId(longCount.incrementAndGet());

        // Create the MapsIdUserProfileWithDTO
        MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO = mapsIdUserProfileWithDTOMapper.toDto(mapsIdUserProfileWithDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(mapsIdUserProfileWithDTODTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MapsIdUserProfileWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMapsIdUserProfileWithDTO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapsIdUserProfileWithDTO.setId(longCount.incrementAndGet());

        // Create the MapsIdUserProfileWithDTO
        MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO = mapsIdUserProfileWithDTOMapper.toDto(mapsIdUserProfileWithDTO);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(mapsIdUserProfileWithDTODTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MapsIdUserProfileWithDTO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMapsIdUserProfileWithDTO() {
        // Initialize the database
        insertedMapsIdUserProfileWithDTO = mapsIdUserProfileWithDTORepository.save(mapsIdUserProfileWithDTO).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mapsIdUserProfileWithDTO
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, mapsIdUserProfileWithDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mapsIdUserProfileWithDTORepository.count().block();
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

    protected MapsIdUserProfileWithDTO getPersistedMapsIdUserProfileWithDTO(MapsIdUserProfileWithDTO mapsIdUserProfileWithDTO) {
        return mapsIdUserProfileWithDTORepository.findById(mapsIdUserProfileWithDTO.getId()).block();
    }

    protected void assertPersistedMapsIdUserProfileWithDTOToMatchAllProperties(MapsIdUserProfileWithDTO expectedMapsIdUserProfileWithDTO) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMapsIdUserProfileWithDTOAllPropertiesEquals(expectedMapsIdUserProfileWithDTO, getPersistedMapsIdUserProfileWithDTO(expectedMapsIdUserProfileWithDTO));
        assertMapsIdUserProfileWithDTOUpdatableFieldsEquals(
            expectedMapsIdUserProfileWithDTO,
            getPersistedMapsIdUserProfileWithDTO(expectedMapsIdUserProfileWithDTO)
        );
    }

    protected void assertPersistedMapsIdUserProfileWithDTOToMatchUpdatableProperties(
        MapsIdUserProfileWithDTO expectedMapsIdUserProfileWithDTO
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMapsIdUserProfileWithDTOAllUpdatablePropertiesEquals(expectedMapsIdUserProfileWithDTO, getPersistedMapsIdUserProfileWithDTO(expectedMapsIdUserProfileWithDTO));
        assertMapsIdUserProfileWithDTOUpdatableFieldsEquals(
            expectedMapsIdUserProfileWithDTO,
            getPersistedMapsIdUserProfileWithDTO(expectedMapsIdUserProfileWithDTO)
        );
    }
}
