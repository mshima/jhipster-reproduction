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
import tech.jhipster.sample.domain.FieldTestInfiniteScrollEntity;
import tech.jhipster.sample.repository.FieldTestInfiniteScrollEntityRepository;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.FieldTestInfiniteScrollEntity}.
 */
@RestController
@RequestMapping("/api/field-test-infinite-scroll-entities")
@Transactional(rollbackFor = Exception.class)
public class FieldTestInfiniteScrollEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(FieldTestInfiniteScrollEntityResource.class);

    private static final String ENTITY_NAME = "fieldTestInfiniteScrollEntity";

    @Value("${jhipster.clientApp.name:sampleWebfluxPsql}")
    private String applicationName;

    private final FieldTestInfiniteScrollEntityRepository fieldTestInfiniteScrollEntityRepository;

    public FieldTestInfiniteScrollEntityResource(FieldTestInfiniteScrollEntityRepository fieldTestInfiniteScrollEntityRepository) {
        this.fieldTestInfiniteScrollEntityRepository = fieldTestInfiniteScrollEntityRepository;
    }

    /**
     * {@code POST  /field-test-infinite-scroll-entities} : Create a new fieldTestInfiniteScrollEntity.
     *
     * @param fieldTestInfiniteScrollEntity the fieldTestInfiniteScrollEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fieldTestInfiniteScrollEntity, or with status {@code 400 (Bad Request)} if the fieldTestInfiniteScrollEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FieldTestInfiniteScrollEntity>> createFieldTestInfiniteScrollEntity(
        @Valid @RequestBody FieldTestInfiniteScrollEntity fieldTestInfiniteScrollEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to save FieldTestInfiniteScrollEntity : {}", fieldTestInfiniteScrollEntity);
        if (fieldTestInfiniteScrollEntity.getId() != null) {
            throw new BadRequestAlertException("A new fieldTestInfiniteScrollEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return fieldTestInfiniteScrollEntityRepository.save(fieldTestInfiniteScrollEntity).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/field-test-infinite-scroll-entities/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /field-test-infinite-scroll-entities/:id} : Updates an existing fieldTestInfiniteScrollEntity.
     *
     * @param id the id of the fieldTestInfiniteScrollEntity to save.
     * @param fieldTestInfiniteScrollEntity the fieldTestInfiniteScrollEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestInfiniteScrollEntity,
     * or with status {@code 400 (Bad Request)} if the fieldTestInfiniteScrollEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestInfiniteScrollEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FieldTestInfiniteScrollEntity>> updateFieldTestInfiniteScrollEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FieldTestInfiniteScrollEntity fieldTestInfiniteScrollEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to update FieldTestInfiniteScrollEntity : {}, {}", id, fieldTestInfiniteScrollEntity);
        if (fieldTestInfiniteScrollEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestInfiniteScrollEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestInfiniteScrollEntityRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return fieldTestInfiniteScrollEntityRepository
                .save(fieldTestInfiniteScrollEntity)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /field-test-infinite-scroll-entities/:id} : Partial updates given fields of an existing fieldTestInfiniteScrollEntity, field will ignore if it is null
     *
     * @param id the id of the fieldTestInfiniteScrollEntity to save.
     * @param fieldTestInfiniteScrollEntity the fieldTestInfiniteScrollEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestInfiniteScrollEntity,
     * or with status {@code 400 (Bad Request)} if the fieldTestInfiniteScrollEntity is not valid,
     * or with status {@code 404 (Not Found)} if the fieldTestInfiniteScrollEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestInfiniteScrollEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FieldTestInfiniteScrollEntity>> partialUpdateFieldTestInfiniteScrollEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FieldTestInfiniteScrollEntity fieldTestInfiniteScrollEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FieldTestInfiniteScrollEntity partially : {}, {}", id, fieldTestInfiniteScrollEntity);
        if (fieldTestInfiniteScrollEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestInfiniteScrollEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestInfiniteScrollEntityRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<FieldTestInfiniteScrollEntity> result = fieldTestInfiniteScrollEntityRepository
                .findById(fieldTestInfiniteScrollEntity.getId())
                .map(existingFieldTestInfiniteScrollEntity -> {
                    updateIfPresent(existingFieldTestInfiniteScrollEntity::setStringHugo, fieldTestInfiniteScrollEntity.getStringHugo());
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setStringRequiredHugo,
                        fieldTestInfiniteScrollEntity.getStringRequiredHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setStringMinlengthHugo,
                        fieldTestInfiniteScrollEntity.getStringMinlengthHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setStringMaxlengthHugo,
                        fieldTestInfiniteScrollEntity.getStringMaxlengthHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setStringPatternHugo,
                        fieldTestInfiniteScrollEntity.getStringPatternHugo()
                    );
                    updateIfPresent(existingFieldTestInfiniteScrollEntity::setIntegerHugo, fieldTestInfiniteScrollEntity.getIntegerHugo());
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setIntegerRequiredHugo,
                        fieldTestInfiniteScrollEntity.getIntegerRequiredHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setIntegerMinHugo,
                        fieldTestInfiniteScrollEntity.getIntegerMinHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setIntegerMaxHugo,
                        fieldTestInfiniteScrollEntity.getIntegerMaxHugo()
                    );
                    updateIfPresent(existingFieldTestInfiniteScrollEntity::setLongHugo, fieldTestInfiniteScrollEntity.getLongHugo());
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setLongRequiredHugo,
                        fieldTestInfiniteScrollEntity.getLongRequiredHugo()
                    );
                    updateIfPresent(existingFieldTestInfiniteScrollEntity::setLongMinHugo, fieldTestInfiniteScrollEntity.getLongMinHugo());
                    updateIfPresent(existingFieldTestInfiniteScrollEntity::setLongMaxHugo, fieldTestInfiniteScrollEntity.getLongMaxHugo());
                    updateIfPresent(existingFieldTestInfiniteScrollEntity::setFloatHugo, fieldTestInfiniteScrollEntity.getFloatHugo());
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setFloatRequiredHugo,
                        fieldTestInfiniteScrollEntity.getFloatRequiredHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setFloatMinHugo,
                        fieldTestInfiniteScrollEntity.getFloatMinHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setFloatMaxHugo,
                        fieldTestInfiniteScrollEntity.getFloatMaxHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setDoubleRequiredHugo,
                        fieldTestInfiniteScrollEntity.getDoubleRequiredHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setDoubleMinHugo,
                        fieldTestInfiniteScrollEntity.getDoubleMinHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setDoubleMaxHugo,
                        fieldTestInfiniteScrollEntity.getDoubleMaxHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setBigDecimalRequiredHugo,
                        fieldTestInfiniteScrollEntity.getBigDecimalRequiredHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setBigDecimalMinHugo,
                        fieldTestInfiniteScrollEntity.getBigDecimalMinHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setBigDecimalMaxHugo,
                        fieldTestInfiniteScrollEntity.getBigDecimalMaxHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setLocalDateHugo,
                        fieldTestInfiniteScrollEntity.getLocalDateHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setLocalDateRequiredHugo,
                        fieldTestInfiniteScrollEntity.getLocalDateRequiredHugo()
                    );
                    updateIfPresent(existingFieldTestInfiniteScrollEntity::setInstantHugo, fieldTestInfiniteScrollEntity.getInstantHugo());
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setInstanteRequiredHugo,
                        fieldTestInfiniteScrollEntity.getInstanteRequiredHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setZonedDateTimeHugo,
                        fieldTestInfiniteScrollEntity.getZonedDateTimeHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setZonedDateTimeRequiredHugo,
                        fieldTestInfiniteScrollEntity.getZonedDateTimeRequiredHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setLocalTimeHugo,
                        fieldTestInfiniteScrollEntity.getLocalTimeHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setLocalTimeRequiredHugo,
                        fieldTestInfiniteScrollEntity.getLocalTimeRequiredHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setDurationHugo,
                        fieldTestInfiniteScrollEntity.getDurationHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setDurationRequiredHugo,
                        fieldTestInfiniteScrollEntity.getDurationRequiredHugo()
                    );
                    updateIfPresent(existingFieldTestInfiniteScrollEntity::setBooleanHugo, fieldTestInfiniteScrollEntity.getBooleanHugo());
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setBooleanRequiredHugo,
                        fieldTestInfiniteScrollEntity.getBooleanRequiredHugo()
                    );
                    updateIfPresent(existingFieldTestInfiniteScrollEntity::setEnumHugo, fieldTestInfiniteScrollEntity.getEnumHugo());
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setEnumRequiredHugo,
                        fieldTestInfiniteScrollEntity.getEnumRequiredHugo()
                    );
                    updateIfPresent(existingFieldTestInfiniteScrollEntity::setUuidHugo, fieldTestInfiniteScrollEntity.getUuidHugo());
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setUuidRequiredHugo,
                        fieldTestInfiniteScrollEntity.getUuidRequiredHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteImageHugo,
                        fieldTestInfiniteScrollEntity.getByteImageHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteImageHugoContentType,
                        fieldTestInfiniteScrollEntity.getByteImageHugoContentType()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteImageRequiredHugo,
                        fieldTestInfiniteScrollEntity.getByteImageRequiredHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteImageRequiredHugoContentType,
                        fieldTestInfiniteScrollEntity.getByteImageRequiredHugoContentType()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteImageMinbytesHugo,
                        fieldTestInfiniteScrollEntity.getByteImageMinbytesHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteImageMinbytesHugoContentType,
                        fieldTestInfiniteScrollEntity.getByteImageMinbytesHugoContentType()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteImageMaxbytesHugo,
                        fieldTestInfiniteScrollEntity.getByteImageMaxbytesHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteImageMaxbytesHugoContentType,
                        fieldTestInfiniteScrollEntity.getByteImageMaxbytesHugoContentType()
                    );
                    updateIfPresent(existingFieldTestInfiniteScrollEntity::setByteAnyHugo, fieldTestInfiniteScrollEntity.getByteAnyHugo());
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteAnyHugoContentType,
                        fieldTestInfiniteScrollEntity.getByteAnyHugoContentType()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteAnyRequiredHugo,
                        fieldTestInfiniteScrollEntity.getByteAnyRequiredHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteAnyRequiredHugoContentType,
                        fieldTestInfiniteScrollEntity.getByteAnyRequiredHugoContentType()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteAnyMinbytesHugo,
                        fieldTestInfiniteScrollEntity.getByteAnyMinbytesHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteAnyMinbytesHugoContentType,
                        fieldTestInfiniteScrollEntity.getByteAnyMinbytesHugoContentType()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteAnyMaxbytesHugo,
                        fieldTestInfiniteScrollEntity.getByteAnyMaxbytesHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteAnyMaxbytesHugoContentType,
                        fieldTestInfiniteScrollEntity.getByteAnyMaxbytesHugoContentType()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteTextHugo,
                        fieldTestInfiniteScrollEntity.getByteTextHugo()
                    );
                    updateIfPresent(
                        existingFieldTestInfiniteScrollEntity::setByteTextRequiredHugo,
                        fieldTestInfiniteScrollEntity.getByteTextRequiredHugo()
                    );

                    return existingFieldTestInfiniteScrollEntity;
                })
                .flatMap(fieldTestInfiniteScrollEntityRepository::save);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /field-test-infinite-scroll-entities} : get all the Field Test Infinite Scroll Entities.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Field Test Infinite Scroll Entities in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<FieldTestInfiniteScrollEntity>>> getAllFieldTestInfiniteScrollEntities(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of FieldTestInfiniteScrollEntities");
        return fieldTestInfiniteScrollEntityRepository
            .count()
            .zipWith(fieldTestInfiniteScrollEntityRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /field-test-infinite-scroll-entities/:id} : get the "id" fieldTestInfiniteScrollEntity.
     *
     * @param id the id of the fieldTestInfiniteScrollEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fieldTestInfiniteScrollEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FieldTestInfiniteScrollEntity>> getFieldTestInfiniteScrollEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FieldTestInfiniteScrollEntity : {}", id);
        Mono<FieldTestInfiniteScrollEntity> fieldTestInfiniteScrollEntity = fieldTestInfiniteScrollEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fieldTestInfiniteScrollEntity);
    }

    /**
     * {@code DELETE  /field-test-infinite-scroll-entities/:id} : delete the "id" fieldTestInfiniteScrollEntity.
     *
     * @param id the id of the fieldTestInfiniteScrollEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFieldTestInfiniteScrollEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FieldTestInfiniteScrollEntity : {}", id);
        return fieldTestInfiniteScrollEntityRepository
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
