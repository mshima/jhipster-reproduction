package tech.jhipster.sample.service;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.Label;
import tech.jhipster.sample.repository.LabelRepository;
import tech.jhipster.sample.repository.search.LabelSearchRepository;

/**
 * Service Implementation for managing {@link tech.jhipster.sample.domain.Label}.
 */
@Service
@Transactional
public class LabelService {

    private static final Logger LOG = LoggerFactory.getLogger(LabelService.class);

    private final LabelRepository labelRepository;

    private final LabelSearchRepository labelSearchRepository;

    public LabelService(LabelRepository labelRepository, LabelSearchRepository labelSearchRepository) {
        this.labelRepository = labelRepository;
        this.labelSearchRepository = labelSearchRepository;
    }

    /**
     * Save a label.
     *
     * @param label the entity to save.
     * @return the persisted entity.
     */
    public Mono<Label> save(Label label) {
        LOG.debug("Request to save Label : {}", label);
        return labelRepository
            .save(label)

            .flatMap(labelSearchRepository::save);
    }

    /**
     * Update a label.
     *
     * @param label the entity to save.
     * @return the persisted entity.
     */
    public Mono<Label> update(Label label) {
        LOG.debug("Request to update Label : {}", label);
        return labelRepository
            .save(label)

            .flatMap(labelSearchRepository::save);
    }

    /**
     * Partially update a label.
     *
     * @param label the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Label> partialUpdate(Label label) {
        LOG.debug("Request to partially update Label : {}", label);

        return labelRepository
            .findById(label.getId())
            .map(existingLabel -> {
                updateIfPresent(existingLabel::setLabelName, label.getLabelName());

                return existingLabel;
            })
            .flatMap(labelRepository::save)
            .flatMap(savedLabel -> {
                labelSearchRepository.save(savedLabel);
                return Mono.just(savedLabel);
            });
    }

    /**
     * Get all the labels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Label> findAll(Pageable pageable) {
        LOG.debug("Request to get all Labels");
        return labelRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of labels available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return labelRepository.count();
    }

    /**
     * Returns the number of labels available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return labelSearchRepository.count();
    }

    /**
     * Get one label by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Label> findOne(Long id) {
        LOG.debug("Request to get Label : {}", id);
        return labelRepository.findById(id);
    }

    /**
     * Delete the label by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Label : {}", id);
        return labelRepository
            .deleteById(id)

            .then(labelSearchRepository.deleteById(id));
    }

    /**
     * Search for the label corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Label> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Labels for query {}", query);
        return labelSearchRepository.search(query, pageable);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
