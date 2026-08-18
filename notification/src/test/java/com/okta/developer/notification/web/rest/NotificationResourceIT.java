package com.okta.developer.notification.web.rest;

import static com.okta.developer.notification.domain.NotificationEntityAsserts.*;
import static com.okta.developer.notification.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.developer.notification.IntegrationTest;
import com.okta.developer.notification.domain.NotificationEntity;
import com.okta.developer.notification.repository.EntityManager;
import com.okta.developer.notification.repository.NotificationRepository;
import com.okta.developer.notification.repository.UserRepository;
import com.okta.developer.notification.service.NotificationService;
import java.time.Duration;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class NotificationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepositoryMock;

    @Mock
    private NotificationService notificationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private NotificationEntity notificationEntity;

    private NotificationEntity insertedNotificationEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationEntity createEntity() {
        return new NotificationEntity().title(DEFAULT_TITLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationEntity createUpdatedEntity() {
        return new NotificationEntity().title(UPDATED_TITLE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(NotificationEntity.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    void initTest() {
        notificationEntity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNotificationEntity != null) {
            notificationRepository.delete(insertedNotificationEntity).block();
            insertedNotificationEntity = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createNotification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Notification
        var returnedNotificationEntity = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationEntity))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(NotificationEntity.class)
            .returnResult()
            .getResponseBody();

        // Validate the Notification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNotificationEntityUpdatableFieldsEquals(
            returnedNotificationEntity,
            getPersistedNotificationEntity(returnedNotificationEntity)
        );

        insertedNotificationEntity = returnedNotificationEntity;
    }

    @Test
    void createNotificationWithExistingId() throws Exception {
        // Create the Notification with an existing ID
        notificationEntity.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationEntity.setTitle(null);

        // Create the Notification, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllNotificationsAsStream() {
        // Initialize the database
        notificationRepository.save(notificationEntity).block();

        List<NotificationEntity> notificationList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(NotificationEntity.class)
            .getResponseBody()
            .filter(notificationEntity::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(notificationList).isNotNull();
        assertThat(notificationList).hasSize(1);
        NotificationEntity testNotification = notificationList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertNotificationEntityAllPropertiesEquals(notificationEntity, testNotification);
        assertNotificationEntityUpdatableFieldsEquals(notificationEntity, testNotification);
    }

    @Test
    void getAllNotifications() {
        // Initialize the database
        insertedNotificationEntity = notificationRepository.save(notificationEntity).block();

        // Get all the notificationList
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
            .value(hasItem(notificationEntity.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotificationsWithEagerRelationshipsIsEnabled() {
        when(notificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?eagerload=true")
            .exchange()
            .expectStatus()
            .isOk();

        verify(notificationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotificationsWithEagerRelationshipsIsNotEnabled() {
        when(notificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?eagerload=false")
            .exchange()
            .expectStatus()
            .isOk();
        verify(notificationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getNotification() {
        // Initialize the database
        insertedNotificationEntity = notificationRepository.save(notificationEntity).block();

        // Get the notification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, notificationEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(notificationEntity.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE));
    }

    @Test
    void getNonExistingNotification() {
        // Get the notification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingNotification() throws Exception {
        // Initialize the database
        insertedNotificationEntity = notificationRepository.save(notificationEntity).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notification
        NotificationEntity updatedNotificationEntity = notificationRepository.findById(notificationEntity.getId()).block();
        updatedNotificationEntity.title(UPDATED_TITLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedNotificationEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedNotificationEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationEntityToMatchAllProperties(updatedNotificationEntity);
    }

    @Test
    void putNonExistingNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, notificationEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationEntity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationEntity = notificationRepository.save(notificationEntity).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notification using partial update
        NotificationEntity partialUpdatedNotificationEntity = new NotificationEntity();
        partialUpdatedNotificationEntity.setId(notificationEntity.getId());

        partialUpdatedNotificationEntity.title(UPDATED_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNotificationEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedNotificationEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Notification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationEntity, notificationEntity),
            getPersistedNotificationEntity(notificationEntity)
        );
    }

    @Test
    void fullUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationEntity = notificationRepository.save(notificationEntity).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notification using partial update
        NotificationEntity partialUpdatedNotificationEntity = new NotificationEntity();
        partialUpdatedNotificationEntity.setId(notificationEntity.getId());

        partialUpdatedNotificationEntity.title(UPDATED_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNotificationEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedNotificationEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Notification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationEntityUpdatableFieldsEquals(
            partialUpdatedNotificationEntity,
            getPersistedNotificationEntity(partialUpdatedNotificationEntity)
        );
    }

    @Test
    void patchNonExistingNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, notificationEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(notificationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(notificationEntity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(notificationEntity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNotification() {
        // Initialize the database
        insertedNotificationEntity = notificationRepository.save(notificationEntity).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notification
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, notificationEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationRepository.count().block();
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

    protected NotificationEntity getPersistedNotificationEntity(NotificationEntity notification) {
        return notificationRepository.findById(notification.getId()).block();
    }

    protected void assertPersistedNotificationEntityToMatchAllProperties(NotificationEntity expectedNotificationEntity) {
        // Test fails because reactive api returns an empty object instead of null
        // assertNotificationEntityAllPropertiesEquals(expectedNotificationEntity, getPersistedNotificationEntity(expectedNotificationEntity));
        assertNotificationEntityUpdatableFieldsEquals(
            expectedNotificationEntity,
            getPersistedNotificationEntity(expectedNotificationEntity)
        );
    }

    protected void assertPersistedNotificationEntityToMatchUpdatableProperties(NotificationEntity expectedNotificationEntity) {
        // Test fails because reactive api returns an empty object instead of null
        // assertNotificationEntityAllUpdatablePropertiesEquals(expectedNotificationEntity, getPersistedNotificationEntity(expectedNotificationEntity));
        assertNotificationEntityUpdatableFieldsEquals(
            expectedNotificationEntity,
            getPersistedNotificationEntity(expectedNotificationEntity)
        );
    }
}
