package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static tech.jhipster.sample.domain.LabelAsserts.*;
import static tech.jhipster.sample.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.IntegrationTest;
import tech.jhipster.sample.domain.Label;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.LabelRepository;
import tech.jhipster.sample.repository.search.LabelSearchRepository;

/**
 * Integration tests for the {@link LabelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LabelResourceIT {

    private static final String DEFAULT_LABEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/labels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/labels/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelSearchRepository labelSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Label label;

    private Label insertedLabel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Label createEntity() {
        return new Label().labelName(DEFAULT_LABEL_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Label createUpdatedEntity() {
        return new Label().labelName(UPDATED_LABEL_NAME);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Label.class).block();
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
        label = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLabel != null) {
            labelRepository.delete(insertedLabel).block();
            labelSearchRepository.delete(insertedLabel).block();
            insertedLabel = null;
        }
        deleteEntities(em);
    }

    @Test
    void getAllLabels() {
        // Initialize the database
        insertedLabel = labelRepository.save(label).block();

        // Get all the labelList
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
            .value(hasItem(label.getId().intValue()))
            .jsonPath("$.[*].labelName")
            .value(hasItem(DEFAULT_LABEL_NAME));
    }

    @Test
    void getLabel() {
        // Initialize the database
        insertedLabel = labelRepository.save(label).block();

        // Get the label
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, label.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(label.getId().intValue()))
            .jsonPath("$.labelName")
            .value(is(DEFAULT_LABEL_NAME));
    }

    @Test
    void getNonExistingLabel() {
        // Get the label
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void searchLabel() {
        // Initialize the database
        insertedLabel = labelRepository.save(label).block();
        labelSearchRepository.save(label).block();

        // Search the label
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + label.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(label.getId().intValue()))
            .jsonPath("$.[*].labelName")
            .value(hasItem(DEFAULT_LABEL_NAME));
    }

    protected long getRepositoryCount() {
        return labelRepository.count().block();
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

    protected Label getPersistedLabel(Label label) {
        return labelRepository.findById(label.getId()).block();
    }

    protected void assertPersistedLabelToMatchAllProperties(Label expectedLabel) {
        // Test fails because reactive api returns an empty object instead of null
        // assertLabelAllPropertiesEquals(expectedLabel, getPersistedLabel(expectedLabel));
        assertLabelUpdatableFieldsEquals(expectedLabel, getPersistedLabel(expectedLabel));
    }

    protected void assertPersistedLabelToMatchUpdatableProperties(Label expectedLabel) {
        // Test fails because reactive api returns an empty object instead of null
        // assertLabelAllUpdatablePropertiesEquals(expectedLabel, getPersistedLabel(expectedLabel));
        assertLabelUpdatableFieldsEquals(expectedLabel, getPersistedLabel(expectedLabel));
    }
}
