package tech.jhipster.sample.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.EntityWithServiceImplAndDTO;
import tech.jhipster.sample.repository.EntityWithServiceImplAndDTORepository;
import tech.jhipster.sample.service.EntityWithServiceImplAndDTOService;
import tech.jhipster.sample.service.dto.EntityWithServiceImplAndDTODTO;
import tech.jhipster.sample.service.mapper.EntityWithServiceImplAndDTOMapper;

/**
 * Service Implementation for managing {@link tech.jhipster.sample.domain.EntityWithServiceImplAndDTO}.
 */
@Service
@Transactional
public class EntityWithServiceImplAndDTOServiceImpl implements EntityWithServiceImplAndDTOService {

    private static final Logger LOG = LoggerFactory.getLogger(EntityWithServiceImplAndDTOServiceImpl.class);

    private final EntityWithServiceImplAndDTORepository entityWithServiceImplAndDTORepository;

    private final EntityWithServiceImplAndDTOMapper entityWithServiceImplAndDTOMapper;

    public EntityWithServiceImplAndDTOServiceImpl(
        EntityWithServiceImplAndDTORepository entityWithServiceImplAndDTORepository,
        EntityWithServiceImplAndDTOMapper entityWithServiceImplAndDTOMapper
    ) {
        this.entityWithServiceImplAndDTORepository = entityWithServiceImplAndDTORepository;
        this.entityWithServiceImplAndDTOMapper = entityWithServiceImplAndDTOMapper;
    }

    @Override
    public Mono<EntityWithServiceImplAndDTODTO> save(EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO) {
        LOG.debug("Request to save EntityWithServiceImplAndDTO : {}", entityWithServiceImplAndDTODTO);
        return entityWithServiceImplAndDTORepository
            .save(entityWithServiceImplAndDTOMapper.toEntity(entityWithServiceImplAndDTODTO))
            .map(entityWithServiceImplAndDTOMapper::toDto);
    }

    @Override
    public Mono<EntityWithServiceImplAndDTODTO> update(EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO) {
        LOG.debug("Request to update EntityWithServiceImplAndDTO : {}", entityWithServiceImplAndDTODTO);
        return entityWithServiceImplAndDTORepository
            .save(entityWithServiceImplAndDTOMapper.toEntity(entityWithServiceImplAndDTODTO))
            .map(entityWithServiceImplAndDTOMapper::toDto);
    }

    @Override
    public Mono<EntityWithServiceImplAndDTODTO> partialUpdate(EntityWithServiceImplAndDTODTO entityWithServiceImplAndDTODTO) {
        LOG.debug("Request to partially update EntityWithServiceImplAndDTO : {}", entityWithServiceImplAndDTODTO);

        return entityWithServiceImplAndDTORepository
            .findById(entityWithServiceImplAndDTODTO.getId())
            .map(existingEntityWithServiceImplAndDTO -> {
                entityWithServiceImplAndDTOMapper.partialUpdate(existingEntityWithServiceImplAndDTO, entityWithServiceImplAndDTODTO);

                return existingEntityWithServiceImplAndDTO;
            })
            .flatMap(entityWithServiceImplAndDTORepository::save)
            .map(entityWithServiceImplAndDTOMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EntityWithServiceImplAndDTODTO> findAll() {
        LOG.debug("Request to get all EntityWithServiceImplAndDTOS");
        return entityWithServiceImplAndDTORepository.findAll().map(entityWithServiceImplAndDTOMapper::toDto);
    }

    public Mono<Long> countAll() {
        return entityWithServiceImplAndDTORepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<EntityWithServiceImplAndDTODTO> findOne(Long id) {
        LOG.debug("Request to get EntityWithServiceImplAndDTO : {}", id);
        return entityWithServiceImplAndDTORepository.findById(id).map(entityWithServiceImplAndDTOMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete EntityWithServiceImplAndDTO : {}", id);
        return entityWithServiceImplAndDTORepository.deleteById(id);
    }
}
