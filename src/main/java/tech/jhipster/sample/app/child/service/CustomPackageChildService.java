package tech.jhipster.sample.app.child.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.app.child.repository.CustomPackageChildRepository;
import tech.jhipster.sample.app.child.service.dto.CustomPackageChildDTO;
import tech.jhipster.sample.app.child.service.mapper.CustomPackageChildMapper;

/**
 * Service Implementation for managing {@link tech.jhipster.sample.app.child.domain.CustomPackageChild}.
 */
@Service
@Transactional
public class CustomPackageChildService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomPackageChildService.class);

    private final CustomPackageChildRepository customPackageChildRepository;

    private final CustomPackageChildMapper customPackageChildMapper;

    public CustomPackageChildService(
        CustomPackageChildRepository customPackageChildRepository,
        CustomPackageChildMapper customPackageChildMapper
    ) {
        this.customPackageChildRepository = customPackageChildRepository;
        this.customPackageChildMapper = customPackageChildMapper;
    }

    /**
     * Save a customPackageChild.
     *
     * @param customPackageChildDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CustomPackageChildDTO> save(CustomPackageChildDTO customPackageChildDTO) {
        LOG.debug("Request to save CustomPackageChild : {}", customPackageChildDTO);
        return customPackageChildRepository
            .save(customPackageChildMapper.toEntity(customPackageChildDTO))
            .map(customPackageChildMapper::toDto);
    }

    /**
     * Update a customPackageChild.
     *
     * @param customPackageChildDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CustomPackageChildDTO> update(CustomPackageChildDTO customPackageChildDTO) {
        LOG.debug("Request to update CustomPackageChild : {}", customPackageChildDTO);
        return customPackageChildRepository
            .save(customPackageChildMapper.toEntity(customPackageChildDTO))
            .map(customPackageChildMapper::toDto);
    }

    /**
     * Partially update a customPackageChild.
     *
     * @param customPackageChildDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CustomPackageChildDTO> partialUpdate(CustomPackageChildDTO customPackageChildDTO) {
        LOG.debug("Request to partially update CustomPackageChild : {}", customPackageChildDTO);

        return customPackageChildRepository
            .findById(customPackageChildDTO.getId())
            .map(existingCustomPackageChild -> {
                customPackageChildMapper.partialUpdate(existingCustomPackageChild, customPackageChildDTO);

                return existingCustomPackageChild;
            })
            .flatMap(customPackageChildRepository::save)
            .map(customPackageChildMapper::toDto);
    }

    /**
     * Get all the customPackageChildren.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CustomPackageChildDTO> findAll() {
        LOG.debug("Request to get all CustomPackageChildren");
        return customPackageChildRepository.findAll().map(customPackageChildMapper::toDto);
    }

    /**
     * Returns the number of customPackageChildren available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return customPackageChildRepository.count();
    }

    /**
     * Get one customPackageChild by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CustomPackageChildDTO> findOne(Long id) {
        LOG.debug("Request to get CustomPackageChild : {}", id);
        return customPackageChildRepository.findById(id).map(customPackageChildMapper::toDto);
    }

    /**
     * Delete the customPackageChild by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete CustomPackageChild : {}", id);
        return customPackageChildRepository.deleteById(id);
    }
}
