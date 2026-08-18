package tech.jhipster.sample.service;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.FieldTestServiceClassAndJpaFilteringEntity;
import tech.jhipster.sample.domain.criteria.FieldTestServiceClassAndJpaFilteringEntityCriteria;
import tech.jhipster.sample.repository.FieldTestServiceClassAndJpaFilteringEntityRepository;

/**
 * Service Implementation for managing {@link tech.jhipster.sample.domain.FieldTestServiceClassAndJpaFilteringEntity}.
 */
@Service
@Transactional
public class FieldTestServiceClassAndJpaFilteringEntityService {

    private static final Logger LOG = LoggerFactory.getLogger(FieldTestServiceClassAndJpaFilteringEntityService.class);

    private final FieldTestServiceClassAndJpaFilteringEntityRepository fieldTestServiceClassAndJpaFilteringEntityRepository;

    public FieldTestServiceClassAndJpaFilteringEntityService(
        FieldTestServiceClassAndJpaFilteringEntityRepository fieldTestServiceClassAndJpaFilteringEntityRepository
    ) {
        this.fieldTestServiceClassAndJpaFilteringEntityRepository = fieldTestServiceClassAndJpaFilteringEntityRepository;
    }

    /**
     * Save a fieldTestServiceClassAndJpaFilteringEntity.
     *
     * @param fieldTestServiceClassAndJpaFilteringEntity the entity to save.
     * @return the persisted entity.
     */
    public Mono<FieldTestServiceClassAndJpaFilteringEntity> save(
        FieldTestServiceClassAndJpaFilteringEntity fieldTestServiceClassAndJpaFilteringEntity
    ) {
        LOG.debug("Request to save FieldTestServiceClassAndJpaFilteringEntity : {}", fieldTestServiceClassAndJpaFilteringEntity);
        return fieldTestServiceClassAndJpaFilteringEntityRepository.save(fieldTestServiceClassAndJpaFilteringEntity);
    }

    /**
     * Update a fieldTestServiceClassAndJpaFilteringEntity.
     *
     * @param fieldTestServiceClassAndJpaFilteringEntity the entity to save.
     * @return the persisted entity.
     */
    public Mono<FieldTestServiceClassAndJpaFilteringEntity> update(
        FieldTestServiceClassAndJpaFilteringEntity fieldTestServiceClassAndJpaFilteringEntity
    ) {
        LOG.debug("Request to update FieldTestServiceClassAndJpaFilteringEntity : {}", fieldTestServiceClassAndJpaFilteringEntity);
        return fieldTestServiceClassAndJpaFilteringEntityRepository.save(fieldTestServiceClassAndJpaFilteringEntity);
    }

    /**
     * Partially update a fieldTestServiceClassAndJpaFilteringEntity.
     *
     * @param fieldTestServiceClassAndJpaFilteringEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FieldTestServiceClassAndJpaFilteringEntity> partialUpdate(
        FieldTestServiceClassAndJpaFilteringEntity fieldTestServiceClassAndJpaFilteringEntity
    ) {
        LOG.debug(
            "Request to partially update FieldTestServiceClassAndJpaFilteringEntity : {}",
            fieldTestServiceClassAndJpaFilteringEntity
        );

        return fieldTestServiceClassAndJpaFilteringEntityRepository
            .findById(fieldTestServiceClassAndJpaFilteringEntity.getId())
            .map(existingFieldTestServiceClassAndJpaFilteringEntity -> {
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setStringBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getStringBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setStringRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getStringRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setStringMinlengthBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getStringMinlengthBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setStringMaxlengthBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getStringMaxlengthBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setStringPatternBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getStringPatternBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setIntegerBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getIntegerBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setIntegerRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getIntegerRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setIntegerMinBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getIntegerMinBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setIntegerMaxBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getIntegerMaxBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setLongBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getLongBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setLongRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getLongRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setLongMinBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getLongMinBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setLongMaxBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getLongMaxBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setFloatBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getFloatBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setFloatRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getFloatRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setFloatMinBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getFloatMinBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setFloatMaxBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getFloatMaxBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setDoubleRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getDoubleRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setDoubleMinBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getDoubleMinBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setDoubleMaxBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getDoubleMaxBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setBigDecimalRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getBigDecimalRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setBigDecimalMinBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getBigDecimalMinBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setBigDecimalMaxBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getBigDecimalMaxBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setLocalDateBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getLocalDateBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setLocalDateRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getLocalDateRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setInstantBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getInstantBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setInstanteRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getInstanteRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setZonedDateTimeBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getZonedDateTimeBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setZonedDateTimeRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getZonedDateTimeRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setLocalTimeBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getLocalTimeBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setLocalTimeRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getLocalTimeRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setDurationBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getDurationBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setDurationRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getDurationRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setBooleanBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getBooleanBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setBooleanRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getBooleanRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setEnumBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getEnumBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setEnumRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getEnumRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setUuidBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getUuidBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setUuidRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getUuidRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteImageBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteImageBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteImageBobContentType,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteImageBobContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteImageRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteImageRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteImageRequiredBobContentType,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteImageRequiredBobContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteImageMinbytesBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteImageMinbytesBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteImageMinbytesBobContentType,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteImageMinbytesBobContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteImageMaxbytesBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteImageMaxbytesBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteImageMaxbytesBobContentType,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteImageMaxbytesBobContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteAnyBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteAnyBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteAnyBobContentType,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteAnyBobContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteAnyRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteAnyRequiredBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteAnyRequiredBobContentType,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteAnyRequiredBobContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteAnyMinbytesBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteAnyMinbytesBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteAnyMinbytesBobContentType,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteAnyMinbytesBobContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteAnyMaxbytesBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteAnyMaxbytesBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteAnyMaxbytesBobContentType,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteAnyMaxbytesBobContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteTextBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteTextBob()
                );
                updateIfPresent(
                    existingFieldTestServiceClassAndJpaFilteringEntity::setByteTextRequiredBob,
                    fieldTestServiceClassAndJpaFilteringEntity.getByteTextRequiredBob()
                );

                return existingFieldTestServiceClassAndJpaFilteringEntity;
            })
            .flatMap(fieldTestServiceClassAndJpaFilteringEntityRepository::save);
    }

    /**
     * Find fieldTestServiceClassAndJpaFilteringEntities by Criteria.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FieldTestServiceClassAndJpaFilteringEntity> findByCriteria(FieldTestServiceClassAndJpaFilteringEntityCriteria criteria) {
        LOG.debug("Request to get all FieldTestServiceClassAndJpaFilteringEntities by Criteria");
        return fieldTestServiceClassAndJpaFilteringEntityRepository.findByCriteria(criteria, null);
    }

    /**
     * Find the count of fieldTestServiceClassAndJpaFilteringEntities by criteria.
     * @param criteria filtering criteria
     * @return the count of fieldTestServiceClassAndJpaFilteringEntities
     */
    public Mono<Long> countByCriteria(FieldTestServiceClassAndJpaFilteringEntityCriteria criteria) {
        LOG.debug("Request to get the count of all FieldTestServiceClassAndJpaFilteringEntities by Criteria");
        return fieldTestServiceClassAndJpaFilteringEntityRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of fieldTestServiceClassAndJpaFilteringEntities available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return fieldTestServiceClassAndJpaFilteringEntityRepository.count();
    }

    /**
     * Get one fieldTestServiceClassAndJpaFilteringEntity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<FieldTestServiceClassAndJpaFilteringEntity> findOne(Long id) {
        LOG.debug("Request to get FieldTestServiceClassAndJpaFilteringEntity : {}", id);
        return fieldTestServiceClassAndJpaFilteringEntityRepository.findById(id);
    }

    /**
     * Delete the fieldTestServiceClassAndJpaFilteringEntity by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete FieldTestServiceClassAndJpaFilteringEntity : {}", id);
        return fieldTestServiceClassAndJpaFilteringEntityRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
