package tech.jhipster.sample.service.impl;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.FieldTestServiceImplEntity;
import tech.jhipster.sample.repository.FieldTestServiceImplEntityRepository;
import tech.jhipster.sample.service.FieldTestServiceImplEntityService;

/**
 * Service Implementation for managing {@link tech.jhipster.sample.domain.FieldTestServiceImplEntity}.
 */
@Service
@Transactional
public class FieldTestServiceImplEntityServiceImpl implements FieldTestServiceImplEntityService {

    private static final Logger LOG = LoggerFactory.getLogger(FieldTestServiceImplEntityServiceImpl.class);

    private final FieldTestServiceImplEntityRepository fieldTestServiceImplEntityRepository;

    public FieldTestServiceImplEntityServiceImpl(FieldTestServiceImplEntityRepository fieldTestServiceImplEntityRepository) {
        this.fieldTestServiceImplEntityRepository = fieldTestServiceImplEntityRepository;
    }

    @Override
    public Mono<FieldTestServiceImplEntity> save(FieldTestServiceImplEntity fieldTestServiceImplEntity) {
        LOG.debug("Request to save FieldTestServiceImplEntity : {}", fieldTestServiceImplEntity);
        return fieldTestServiceImplEntityRepository.save(fieldTestServiceImplEntity);
    }

    @Override
    public Mono<FieldTestServiceImplEntity> update(FieldTestServiceImplEntity fieldTestServiceImplEntity) {
        LOG.debug("Request to update FieldTestServiceImplEntity : {}", fieldTestServiceImplEntity);
        return fieldTestServiceImplEntityRepository.save(fieldTestServiceImplEntity);
    }

    @Override
    public Mono<FieldTestServiceImplEntity> partialUpdate(FieldTestServiceImplEntity fieldTestServiceImplEntity) {
        LOG.debug("Request to partially update FieldTestServiceImplEntity : {}", fieldTestServiceImplEntity);

        return fieldTestServiceImplEntityRepository
            .findById(fieldTestServiceImplEntity.getId())
            .map(existingFieldTestServiceImplEntity -> {
                updateIfPresent(existingFieldTestServiceImplEntity::setStringMika, fieldTestServiceImplEntity.getStringMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setStringRequiredMika,
                    fieldTestServiceImplEntity.getStringRequiredMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setStringMinlengthMika,
                    fieldTestServiceImplEntity.getStringMinlengthMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setStringMaxlengthMika,
                    fieldTestServiceImplEntity.getStringMaxlengthMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setStringPatternMika,
                    fieldTestServiceImplEntity.getStringPatternMika()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setIntegerMika, fieldTestServiceImplEntity.getIntegerMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setIntegerRequiredMika,
                    fieldTestServiceImplEntity.getIntegerRequiredMika()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setIntegerMinMika, fieldTestServiceImplEntity.getIntegerMinMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setIntegerMaxMika, fieldTestServiceImplEntity.getIntegerMaxMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setLongMika, fieldTestServiceImplEntity.getLongMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setLongRequiredMika, fieldTestServiceImplEntity.getLongRequiredMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setLongMinMika, fieldTestServiceImplEntity.getLongMinMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setLongMaxMika, fieldTestServiceImplEntity.getLongMaxMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setFloatMika, fieldTestServiceImplEntity.getFloatMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setFloatRequiredMika,
                    fieldTestServiceImplEntity.getFloatRequiredMika()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setFloatMinMika, fieldTestServiceImplEntity.getFloatMinMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setFloatMaxMika, fieldTestServiceImplEntity.getFloatMaxMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setDoubleRequiredMika,
                    fieldTestServiceImplEntity.getDoubleRequiredMika()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setDoubleMinMika, fieldTestServiceImplEntity.getDoubleMinMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setDoubleMaxMika, fieldTestServiceImplEntity.getDoubleMaxMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setBigDecimalRequiredMika,
                    fieldTestServiceImplEntity.getBigDecimalRequiredMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setBigDecimalMinMika,
                    fieldTestServiceImplEntity.getBigDecimalMinMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setBigDecimalMaxMika,
                    fieldTestServiceImplEntity.getBigDecimalMaxMika()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setLocalDateMika, fieldTestServiceImplEntity.getLocalDateMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setLocalDateRequiredMika,
                    fieldTestServiceImplEntity.getLocalDateRequiredMika()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setInstantMika, fieldTestServiceImplEntity.getInstantMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setInstanteRequiredMika,
                    fieldTestServiceImplEntity.getInstanteRequiredMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setZonedDateTimeMika,
                    fieldTestServiceImplEntity.getZonedDateTimeMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setZonedDateTimeRequiredMika,
                    fieldTestServiceImplEntity.getZonedDateTimeRequiredMika()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setLocalTimeMika, fieldTestServiceImplEntity.getLocalTimeMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setLocalTimeRequiredMika,
                    fieldTestServiceImplEntity.getLocalTimeRequiredMika()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setDurationMika, fieldTestServiceImplEntity.getDurationMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setDurationRequiredMika,
                    fieldTestServiceImplEntity.getDurationRequiredMika()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setBooleanMika, fieldTestServiceImplEntity.getBooleanMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setBooleanRequiredMika,
                    fieldTestServiceImplEntity.getBooleanRequiredMika()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setEnumMika, fieldTestServiceImplEntity.getEnumMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setEnumRequiredMika, fieldTestServiceImplEntity.getEnumRequiredMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setUuidMika, fieldTestServiceImplEntity.getUuidMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setUuidRequiredMika, fieldTestServiceImplEntity.getUuidRequiredMika());
                updateIfPresent(existingFieldTestServiceImplEntity::setByteImageMika, fieldTestServiceImplEntity.getByteImageMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteImageMikaContentType,
                    fieldTestServiceImplEntity.getByteImageMikaContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteImageRequiredMika,
                    fieldTestServiceImplEntity.getByteImageRequiredMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteImageRequiredMikaContentType,
                    fieldTestServiceImplEntity.getByteImageRequiredMikaContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteImageMinbytesMika,
                    fieldTestServiceImplEntity.getByteImageMinbytesMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteImageMinbytesMikaContentType,
                    fieldTestServiceImplEntity.getByteImageMinbytesMikaContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteImageMaxbytesMika,
                    fieldTestServiceImplEntity.getByteImageMaxbytesMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteImageMaxbytesMikaContentType,
                    fieldTestServiceImplEntity.getByteImageMaxbytesMikaContentType()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setByteAnyMika, fieldTestServiceImplEntity.getByteAnyMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteAnyMikaContentType,
                    fieldTestServiceImplEntity.getByteAnyMikaContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteAnyRequiredMika,
                    fieldTestServiceImplEntity.getByteAnyRequiredMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteAnyRequiredMikaContentType,
                    fieldTestServiceImplEntity.getByteAnyRequiredMikaContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteAnyMinbytesMika,
                    fieldTestServiceImplEntity.getByteAnyMinbytesMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteAnyMinbytesMikaContentType,
                    fieldTestServiceImplEntity.getByteAnyMinbytesMikaContentType()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteAnyMaxbytesMika,
                    fieldTestServiceImplEntity.getByteAnyMaxbytesMika()
                );
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteAnyMaxbytesMikaContentType,
                    fieldTestServiceImplEntity.getByteAnyMaxbytesMikaContentType()
                );
                updateIfPresent(existingFieldTestServiceImplEntity::setByteTextMika, fieldTestServiceImplEntity.getByteTextMika());
                updateIfPresent(
                    existingFieldTestServiceImplEntity::setByteTextRequiredMika,
                    fieldTestServiceImplEntity.getByteTextRequiredMika()
                );

                return existingFieldTestServiceImplEntity;
            })
            .flatMap(fieldTestServiceImplEntityRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<FieldTestServiceImplEntity> findAll() {
        LOG.debug("Request to get all FieldTestServiceImplEntities");
        return fieldTestServiceImplEntityRepository.findAll();
    }

    public Mono<Long> countAll() {
        return fieldTestServiceImplEntityRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<FieldTestServiceImplEntity> findOne(Long id) {
        LOG.debug("Request to get FieldTestServiceImplEntity : {}", id);
        return fieldTestServiceImplEntityRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete FieldTestServiceImplEntity : {}", id);
        return fieldTestServiceImplEntityRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
