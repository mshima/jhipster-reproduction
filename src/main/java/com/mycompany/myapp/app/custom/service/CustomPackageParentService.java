package com.mycompany.myapp.app.custom.service;

import com.mycompany.myapp.app.custom.repository.CustomPackageParentRepository;
import com.mycompany.myapp.app.custom.service.dto.CustomPackageParentDTO;
import com.mycompany.myapp.app.custom.service.mapper.CustomPackageParentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.app.custom.domain.CustomPackageParent}.
 */
@Service
@Transactional
public class CustomPackageParentService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomPackageParentService.class);

    private final CustomPackageParentRepository customPackageParentRepository;

    private final CustomPackageParentMapper customPackageParentMapper;

    public CustomPackageParentService(
        CustomPackageParentRepository customPackageParentRepository,
        CustomPackageParentMapper customPackageParentMapper
    ) {
        this.customPackageParentRepository = customPackageParentRepository;
        this.customPackageParentMapper = customPackageParentMapper;
    }

    /**
     * Save a customPackageParent.
     *
     * @param customPackageParentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CustomPackageParentDTO> save(CustomPackageParentDTO customPackageParentDTO) {
        LOG.debug("Request to save CustomPackageParent : {}", customPackageParentDTO);
        return customPackageParentRepository
            .save(customPackageParentMapper.toEntity(customPackageParentDTO))
            .map(customPackageParentMapper::toDto);
    }

    /**
     * Update a customPackageParent.
     *
     * @param customPackageParentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CustomPackageParentDTO> update(CustomPackageParentDTO customPackageParentDTO) {
        LOG.debug("Request to update CustomPackageParent : {}", customPackageParentDTO);
        return customPackageParentRepository
            .save(customPackageParentMapper.toEntity(customPackageParentDTO))
            .map(customPackageParentMapper::toDto);
    }

    /**
     * Partially update a customPackageParent.
     *
     * @param customPackageParentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CustomPackageParentDTO> partialUpdate(CustomPackageParentDTO customPackageParentDTO) {
        LOG.debug("Request to partially update CustomPackageParent : {}", customPackageParentDTO);

        return customPackageParentRepository
            .findById(customPackageParentDTO.getId())
            .map(existingCustomPackageParent -> {
                customPackageParentMapper.partialUpdate(existingCustomPackageParent, customPackageParentDTO);

                return existingCustomPackageParent;
            })
            .flatMap(customPackageParentRepository::save)
            .map(customPackageParentMapper::toDto);
    }

    /**
     * Get all the customPackageParents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CustomPackageParentDTO> findAll() {
        LOG.debug("Request to get all CustomPackageParents");
        return customPackageParentRepository.findAll().map(customPackageParentMapper::toDto);
    }

    /**
     * Returns the number of customPackageParents available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return customPackageParentRepository.count();
    }

    /**
     * Get one customPackageParent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CustomPackageParentDTO> findOne(Long id) {
        LOG.debug("Request to get CustomPackageParent : {}", id);
        return customPackageParentRepository.findById(id).map(customPackageParentMapper::toDto);
    }

    /**
     * Delete the customPackageParent by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete CustomPackageParent : {}", id);
        return customPackageParentRepository.deleteById(id);
    }
}
