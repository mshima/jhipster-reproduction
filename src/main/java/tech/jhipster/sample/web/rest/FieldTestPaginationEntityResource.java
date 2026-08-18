package tech.jhipster.sample.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.FieldTestPaginationEntity;
import tech.jhipster.sample.repository.FieldTestPaginationEntityRepository;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.FieldTestPaginationEntity}.
 */
@RestController
@RequestMapping("/api/field-test-pagination-entities")
@Transactional(rollbackFor = Exception.class)
public class FieldTestPaginationEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(FieldTestPaginationEntityResource.class);

    private static final String ENTITY_NAME = "fieldTestPaginationEntity";

    @Value("${jhipster.clientApp.name:sampleWebfluxPsql}")
    private String applicationName;

    private final FieldTestPaginationEntityRepository fieldTestPaginationEntityRepository;

    public FieldTestPaginationEntityResource(FieldTestPaginationEntityRepository fieldTestPaginationEntityRepository) {
        this.fieldTestPaginationEntityRepository = fieldTestPaginationEntityRepository;
    }

    /**
     * {@code POST  /field-test-pagination-entities} : Create a new fieldTestPaginationEntity.
     *
     * @param fieldTestPaginationEntity the fieldTestPaginationEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fieldTestPaginationEntity, or with status {@code 400 (Bad Request)} if the fieldTestPaginationEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FieldTestPaginationEntity>> createFieldTestPaginationEntity(
        @Valid @RequestBody FieldTestPaginationEntity fieldTestPaginationEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to save FieldTestPaginationEntity : {}", fieldTestPaginationEntity);
        if (fieldTestPaginationEntity.getId() != null) {
            throw new BadRequestAlertException("A new fieldTestPaginationEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return fieldTestPaginationEntityRepository.save(fieldTestPaginationEntity).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/field-test-pagination-entities/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /field-test-pagination-entities/:id} : Updates an existing fieldTestPaginationEntity.
     *
     * @param id the id of the fieldTestPaginationEntity to save.
     * @param fieldTestPaginationEntity the fieldTestPaginationEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestPaginationEntity,
     * or with status {@code 400 (Bad Request)} if the fieldTestPaginationEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestPaginationEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FieldTestPaginationEntity>> updateFieldTestPaginationEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FieldTestPaginationEntity fieldTestPaginationEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to update FieldTestPaginationEntity : {}, {}", id, fieldTestPaginationEntity);
        if (fieldTestPaginationEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestPaginationEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestPaginationEntityRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return fieldTestPaginationEntityRepository
                .save(fieldTestPaginationEntity)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /field-test-pagination-entities/:id} : Partial updates given fields of an existing fieldTestPaginationEntity, field will ignore if it is null
     *
     * @param id the id of the fieldTestPaginationEntity to save.
     * @param fieldTestPaginationEntity the fieldTestPaginationEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestPaginationEntity,
     * or with status {@code 400 (Bad Request)} if the fieldTestPaginationEntity is not valid,
     * or with status {@code 404 (Not Found)} if the fieldTestPaginationEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestPaginationEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FieldTestPaginationEntity>> partialUpdateFieldTestPaginationEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FieldTestPaginationEntity fieldTestPaginationEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FieldTestPaginationEntity partially : {}, {}", id, fieldTestPaginationEntity);
        if (fieldTestPaginationEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestPaginationEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestPaginationEntityRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<FieldTestPaginationEntity> result = fieldTestPaginationEntityRepository
                .findById(fieldTestPaginationEntity.getId())
                .map(existingFieldTestPaginationEntity -> {
                    updateIfPresent(existingFieldTestPaginationEntity::setStringAlice, fieldTestPaginationEntity.getStringAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setStringRequiredAlice,
                        fieldTestPaginationEntity.getStringRequiredAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setStringMinlengthAlice,
                        fieldTestPaginationEntity.getStringMinlengthAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setStringMaxlengthAlice,
                        fieldTestPaginationEntity.getStringMaxlengthAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setStringPatternAlice,
                        fieldTestPaginationEntity.getStringPatternAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setIntegerAlice, fieldTestPaginationEntity.getIntegerAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setIntegerRequiredAlice,
                        fieldTestPaginationEntity.getIntegerRequiredAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setIntegerMinAlice, fieldTestPaginationEntity.getIntegerMinAlice());
                    updateIfPresent(existingFieldTestPaginationEntity::setIntegerMaxAlice, fieldTestPaginationEntity.getIntegerMaxAlice());
                    updateIfPresent(existingFieldTestPaginationEntity::setLongAlice, fieldTestPaginationEntity.getLongAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setLongRequiredAlice,
                        fieldTestPaginationEntity.getLongRequiredAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setLongMinAlice, fieldTestPaginationEntity.getLongMinAlice());
                    updateIfPresent(existingFieldTestPaginationEntity::setLongMaxAlice, fieldTestPaginationEntity.getLongMaxAlice());
                    updateIfPresent(existingFieldTestPaginationEntity::setFloatAlice, fieldTestPaginationEntity.getFloatAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setFloatRequiredAlice,
                        fieldTestPaginationEntity.getFloatRequiredAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setFloatMinAlice, fieldTestPaginationEntity.getFloatMinAlice());
                    updateIfPresent(existingFieldTestPaginationEntity::setFloatMaxAlice, fieldTestPaginationEntity.getFloatMaxAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setDoubleRequiredAlice,
                        fieldTestPaginationEntity.getDoubleRequiredAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setDoubleMinAlice, fieldTestPaginationEntity.getDoubleMinAlice());
                    updateIfPresent(existingFieldTestPaginationEntity::setDoubleMaxAlice, fieldTestPaginationEntity.getDoubleMaxAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setBigDecimalRequiredAlice,
                        fieldTestPaginationEntity.getBigDecimalRequiredAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setBigDecimalMinAlice,
                        fieldTestPaginationEntity.getBigDecimalMinAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setBigDecimalMaxAlice,
                        fieldTestPaginationEntity.getBigDecimalMaxAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setLocalDateAlice, fieldTestPaginationEntity.getLocalDateAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setLocalDateRequiredAlice,
                        fieldTestPaginationEntity.getLocalDateRequiredAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setInstantAlice, fieldTestPaginationEntity.getInstantAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setInstanteRequiredAlice,
                        fieldTestPaginationEntity.getInstanteRequiredAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setZonedDateTimeAlice,
                        fieldTestPaginationEntity.getZonedDateTimeAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setZonedDateTimeRequiredAlice,
                        fieldTestPaginationEntity.getZonedDateTimeRequiredAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setLocalTimeAlice, fieldTestPaginationEntity.getLocalTimeAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setLocalTimeRequiredAlice,
                        fieldTestPaginationEntity.getLocalTimeRequiredAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setDurationAlice, fieldTestPaginationEntity.getDurationAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setDurationRequiredAlice,
                        fieldTestPaginationEntity.getDurationRequiredAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setBooleanAlice, fieldTestPaginationEntity.getBooleanAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setBooleanRequiredAlice,
                        fieldTestPaginationEntity.getBooleanRequiredAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setEnumAlice, fieldTestPaginationEntity.getEnumAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setEnumRequiredAlice,
                        fieldTestPaginationEntity.getEnumRequiredAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setUuidAlice, fieldTestPaginationEntity.getUuidAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setUuidRequiredAlice,
                        fieldTestPaginationEntity.getUuidRequiredAlice()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setByteImageAlice, fieldTestPaginationEntity.getByteImageAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteImageAliceContentType,
                        fieldTestPaginationEntity.getByteImageAliceContentType()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteImageRequiredAlice,
                        fieldTestPaginationEntity.getByteImageRequiredAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteImageRequiredAliceContentType,
                        fieldTestPaginationEntity.getByteImageRequiredAliceContentType()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteImageMinbytesAlice,
                        fieldTestPaginationEntity.getByteImageMinbytesAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteImageMinbytesAliceContentType,
                        fieldTestPaginationEntity.getByteImageMinbytesAliceContentType()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteImageMaxbytesAlice,
                        fieldTestPaginationEntity.getByteImageMaxbytesAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteImageMaxbytesAliceContentType,
                        fieldTestPaginationEntity.getByteImageMaxbytesAliceContentType()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setByteAnyAlice, fieldTestPaginationEntity.getByteAnyAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteAnyAliceContentType,
                        fieldTestPaginationEntity.getByteAnyAliceContentType()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteAnyRequiredAlice,
                        fieldTestPaginationEntity.getByteAnyRequiredAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteAnyRequiredAliceContentType,
                        fieldTestPaginationEntity.getByteAnyRequiredAliceContentType()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteAnyMinbytesAlice,
                        fieldTestPaginationEntity.getByteAnyMinbytesAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteAnyMinbytesAliceContentType,
                        fieldTestPaginationEntity.getByteAnyMinbytesAliceContentType()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteAnyMaxbytesAlice,
                        fieldTestPaginationEntity.getByteAnyMaxbytesAlice()
                    );
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteAnyMaxbytesAliceContentType,
                        fieldTestPaginationEntity.getByteAnyMaxbytesAliceContentType()
                    );
                    updateIfPresent(existingFieldTestPaginationEntity::setByteTextAlice, fieldTestPaginationEntity.getByteTextAlice());
                    updateIfPresent(
                        existingFieldTestPaginationEntity::setByteTextRequiredAlice,
                        fieldTestPaginationEntity.getByteTextRequiredAlice()
                    );

                    return existingFieldTestPaginationEntity;
                })
                .flatMap(fieldTestPaginationEntityRepository::save);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /field-test-pagination-entities} : get all the Field Test Pagination Entities.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Field Test Pagination Entities in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<FieldTestPaginationEntity>>> getAllFieldTestPaginationEntities(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of FieldTestPaginationEntities");
        return fieldTestPaginationEntityRepository
            .count()
            .zipWith(fieldTestPaginationEntityRepository.findAllBy(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /field-test-pagination-entities/:id} : get the "id" fieldTestPaginationEntity.
     *
     * @param id the id of the fieldTestPaginationEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fieldTestPaginationEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FieldTestPaginationEntity>> getFieldTestPaginationEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FieldTestPaginationEntity : {}", id);
        Mono<FieldTestPaginationEntity> fieldTestPaginationEntity = fieldTestPaginationEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fieldTestPaginationEntity);
    }

    /**
     * {@code DELETE  /field-test-pagination-entities/:id} : delete the "id" fieldTestPaginationEntity.
     *
     * @param id the id of the fieldTestPaginationEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFieldTestPaginationEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FieldTestPaginationEntity : {}", id);
        return fieldTestPaginationEntityRepository
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
