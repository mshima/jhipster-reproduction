package tech.jhipster.sample.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.FieldTestEntity;
import tech.jhipster.sample.repository.FieldTestEntityRepository;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.FieldTestEntity}.
 */
@RestController
@RequestMapping("/api/field-test-entities")
@Transactional(rollbackFor = Exception.class)
public class FieldTestEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(FieldTestEntityResource.class);

    private static final String ENTITY_NAME = "fieldTestEntity";

    @Value("${jhipster.clientApp.name:sampleWebfluxPsql}")
    private String applicationName;

    private final FieldTestEntityRepository fieldTestEntityRepository;

    public FieldTestEntityResource(FieldTestEntityRepository fieldTestEntityRepository) {
        this.fieldTestEntityRepository = fieldTestEntityRepository;
    }

    /**
     * {@code POST  /field-test-entities} : Create a new fieldTestEntity.
     *
     * @param fieldTestEntity the fieldTestEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fieldTestEntity, or with status {@code 400 (Bad Request)} if the fieldTestEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FieldTestEntity>> createFieldTestEntity(@Valid @RequestBody FieldTestEntity fieldTestEntity)
        throws URISyntaxException {
        LOG.debug("REST request to save FieldTestEntity : {}", fieldTestEntity);
        if (fieldTestEntity.getId() != null) {
            throw new BadRequestAlertException("A new fieldTestEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return fieldTestEntityRepository.save(fieldTestEntity).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/field-test-entities/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /field-test-entities/:id} : Updates an existing fieldTestEntity.
     *
     * @param id the id of the fieldTestEntity to save.
     * @param fieldTestEntity the fieldTestEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestEntity,
     * or with status {@code 400 (Bad Request)} if the fieldTestEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FieldTestEntity>> updateFieldTestEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FieldTestEntity fieldTestEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to update FieldTestEntity : {}, {}", id, fieldTestEntity);
        if (fieldTestEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestEntityRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return fieldTestEntityRepository
                .save(fieldTestEntity)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /field-test-entities/:id} : Partial updates given fields of an existing fieldTestEntity, field will ignore if it is null
     *
     * @param id the id of the fieldTestEntity to save.
     * @param fieldTestEntity the fieldTestEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestEntity,
     * or with status {@code 400 (Bad Request)} if the fieldTestEntity is not valid,
     * or with status {@code 404 (Not Found)} if the fieldTestEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FieldTestEntity>> partialUpdateFieldTestEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FieldTestEntity fieldTestEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FieldTestEntity partially : {}, {}", id, fieldTestEntity);
        if (fieldTestEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestEntityRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<FieldTestEntity> result = fieldTestEntityRepository
                .findById(fieldTestEntity.getId())
                .map(existingFieldTestEntity -> {
                    updateIfPresent(existingFieldTestEntity::setStringTom, fieldTestEntity.getStringTom());
                    updateIfPresent(existingFieldTestEntity::setStringRequiredTom, fieldTestEntity.getStringRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setStringMinlengthTom, fieldTestEntity.getStringMinlengthTom());
                    updateIfPresent(existingFieldTestEntity::setStringMaxlengthTom, fieldTestEntity.getStringMaxlengthTom());
                    updateIfPresent(existingFieldTestEntity::setStringPatternTom, fieldTestEntity.getStringPatternTom());
                    updateIfPresent(existingFieldTestEntity::setNumberPatternTom, fieldTestEntity.getNumberPatternTom());
                    updateIfPresent(existingFieldTestEntity::setNumberPatternRequiredTom, fieldTestEntity.getNumberPatternRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setIntegerTom, fieldTestEntity.getIntegerTom());
                    updateIfPresent(existingFieldTestEntity::setIntegerRequiredTom, fieldTestEntity.getIntegerRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setIntegerMinTom, fieldTestEntity.getIntegerMinTom());
                    updateIfPresent(existingFieldTestEntity::setIntegerMaxTom, fieldTestEntity.getIntegerMaxTom());
                    updateIfPresent(existingFieldTestEntity::setLongTom, fieldTestEntity.getLongTom());
                    updateIfPresent(existingFieldTestEntity::setLongRequiredTom, fieldTestEntity.getLongRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setLongMinTom, fieldTestEntity.getLongMinTom());
                    updateIfPresent(existingFieldTestEntity::setLongMaxTom, fieldTestEntity.getLongMaxTom());
                    updateIfPresent(existingFieldTestEntity::setFloatTom, fieldTestEntity.getFloatTom());
                    updateIfPresent(existingFieldTestEntity::setFloatRequiredTom, fieldTestEntity.getFloatRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setFloatMinTom, fieldTestEntity.getFloatMinTom());
                    updateIfPresent(existingFieldTestEntity::setFloatMaxTom, fieldTestEntity.getFloatMaxTom());
                    updateIfPresent(existingFieldTestEntity::setDoubleRequiredTom, fieldTestEntity.getDoubleRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setDoubleMinTom, fieldTestEntity.getDoubleMinTom());
                    updateIfPresent(existingFieldTestEntity::setDoubleMaxTom, fieldTestEntity.getDoubleMaxTom());
                    updateIfPresent(existingFieldTestEntity::setBigDecimalRequiredTom, fieldTestEntity.getBigDecimalRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setBigDecimalMinTom, fieldTestEntity.getBigDecimalMinTom());
                    updateIfPresent(existingFieldTestEntity::setBigDecimalMaxTom, fieldTestEntity.getBigDecimalMaxTom());
                    updateIfPresent(existingFieldTestEntity::setLocalDateTom, fieldTestEntity.getLocalDateTom());
                    updateIfPresent(existingFieldTestEntity::setLocalDateRequiredTom, fieldTestEntity.getLocalDateRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setInstantTom, fieldTestEntity.getInstantTom());
                    updateIfPresent(existingFieldTestEntity::setInstantRequiredTom, fieldTestEntity.getInstantRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setZonedDateTimeTom, fieldTestEntity.getZonedDateTimeTom());
                    updateIfPresent(existingFieldTestEntity::setZonedDateTimeRequiredTom, fieldTestEntity.getZonedDateTimeRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setLocalTimeTom, fieldTestEntity.getLocalTimeTom());
                    updateIfPresent(existingFieldTestEntity::setLocalTimeRequiredTom, fieldTestEntity.getLocalTimeRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setDurationTom, fieldTestEntity.getDurationTom());
                    updateIfPresent(existingFieldTestEntity::setDurationRequiredTom, fieldTestEntity.getDurationRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setBooleanTom, fieldTestEntity.getBooleanTom());
                    updateIfPresent(existingFieldTestEntity::setBooleanRequiredTom, fieldTestEntity.getBooleanRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setEnumTom, fieldTestEntity.getEnumTom());
                    updateIfPresent(existingFieldTestEntity::setEnumRequiredTom, fieldTestEntity.getEnumRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setUuidTom, fieldTestEntity.getUuidTom());
                    updateIfPresent(existingFieldTestEntity::setUuidRequiredTom, fieldTestEntity.getUuidRequiredTom());
                    updateIfPresent(existingFieldTestEntity::setByteImageTom, fieldTestEntity.getByteImageTom());
                    updateIfPresent(existingFieldTestEntity::setByteImageTomContentType, fieldTestEntity.getByteImageTomContentType());
                    updateIfPresent(existingFieldTestEntity::setByteImageRequiredTom, fieldTestEntity.getByteImageRequiredTom());
                    updateIfPresent(
                        existingFieldTestEntity::setByteImageRequiredTomContentType,
                        fieldTestEntity.getByteImageRequiredTomContentType()
                    );
                    updateIfPresent(existingFieldTestEntity::setByteImageMinbytesTom, fieldTestEntity.getByteImageMinbytesTom());
                    updateIfPresent(
                        existingFieldTestEntity::setByteImageMinbytesTomContentType,
                        fieldTestEntity.getByteImageMinbytesTomContentType()
                    );
                    updateIfPresent(existingFieldTestEntity::setByteImageMaxbytesTom, fieldTestEntity.getByteImageMaxbytesTom());
                    updateIfPresent(
                        existingFieldTestEntity::setByteImageMaxbytesTomContentType,
                        fieldTestEntity.getByteImageMaxbytesTomContentType()
                    );
                    updateIfPresent(existingFieldTestEntity::setByteAnyTom, fieldTestEntity.getByteAnyTom());
                    updateIfPresent(existingFieldTestEntity::setByteAnyTomContentType, fieldTestEntity.getByteAnyTomContentType());
                    updateIfPresent(existingFieldTestEntity::setByteAnyRequiredTom, fieldTestEntity.getByteAnyRequiredTom());
                    updateIfPresent(
                        existingFieldTestEntity::setByteAnyRequiredTomContentType,
                        fieldTestEntity.getByteAnyRequiredTomContentType()
                    );
                    updateIfPresent(existingFieldTestEntity::setByteAnyMinbytesTom, fieldTestEntity.getByteAnyMinbytesTom());
                    updateIfPresent(
                        existingFieldTestEntity::setByteAnyMinbytesTomContentType,
                        fieldTestEntity.getByteAnyMinbytesTomContentType()
                    );
                    updateIfPresent(existingFieldTestEntity::setByteAnyMaxbytesTom, fieldTestEntity.getByteAnyMaxbytesTom());
                    updateIfPresent(
                        existingFieldTestEntity::setByteAnyMaxbytesTomContentType,
                        fieldTestEntity.getByteAnyMaxbytesTomContentType()
                    );
                    updateIfPresent(existingFieldTestEntity::setByteTextTom, fieldTestEntity.getByteTextTom());
                    updateIfPresent(existingFieldTestEntity::setByteTextRequiredTom, fieldTestEntity.getByteTextRequiredTom());

                    return existingFieldTestEntity;
                })
                .flatMap(fieldTestEntityRepository::save);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /field-test-entities} : get all the Field Test Entities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Field Test Entities in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<FieldTestEntity>> getAllFieldTestEntities() {
        LOG.debug("REST request to get all FieldTestEntities");
        return fieldTestEntityRepository.findAll().collectList();
    }

    /**
     * {@code GET  /field-test-entities} : get all the Field Test Entities as a stream.
     * @return the {@link Flux} of Field Test Entities.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FieldTestEntity> getAllFieldTestEntitiesAsStream() {
        LOG.debug("REST request to get all FieldTestEntities as a stream");
        return fieldTestEntityRepository.findAll();
    }

    /**
     * {@code GET  /field-test-entities/:id} : get the "id" fieldTestEntity.
     *
     * @param id the id of the fieldTestEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fieldTestEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FieldTestEntity>> getFieldTestEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FieldTestEntity : {}", id);
        Mono<FieldTestEntity> fieldTestEntity = fieldTestEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fieldTestEntity);
    }

    /**
     * {@code DELETE  /field-test-entities/:id} : delete the "id" fieldTestEntity.
     *
     * @param id the id of the fieldTestEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFieldTestEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FieldTestEntity : {}", id);
        return fieldTestEntityRepository
            .deleteById(id)

            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
