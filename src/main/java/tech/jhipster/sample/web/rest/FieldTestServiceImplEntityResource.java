package tech.jhipster.sample.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.FieldTestServiceImplEntity;
import tech.jhipster.sample.repository.FieldTestServiceImplEntityRepository;
import tech.jhipster.sample.service.FieldTestServiceImplEntityService;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.FieldTestServiceImplEntity}.
 */
@RestController
@RequestMapping("/api/field-test-service-impl-entities")
public class FieldTestServiceImplEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(FieldTestServiceImplEntityResource.class);

    private static final String ENTITY_NAME = "fieldTestServiceImplEntity";

    @Value("${jhipster.clientApp.name:sampleWebfluxPsql}")
    private String applicationName;

    private final FieldTestServiceImplEntityService fieldTestServiceImplEntityService;

    private final FieldTestServiceImplEntityRepository fieldTestServiceImplEntityRepository;

    public FieldTestServiceImplEntityResource(
        FieldTestServiceImplEntityService fieldTestServiceImplEntityService,
        FieldTestServiceImplEntityRepository fieldTestServiceImplEntityRepository
    ) {
        this.fieldTestServiceImplEntityService = fieldTestServiceImplEntityService;
        this.fieldTestServiceImplEntityRepository = fieldTestServiceImplEntityRepository;
    }

    /**
     * {@code POST  /field-test-service-impl-entities} : Create a new fieldTestServiceImplEntity.
     *
     * @param fieldTestServiceImplEntity the fieldTestServiceImplEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fieldTestServiceImplEntity, or with status {@code 400 (Bad Request)} if the fieldTestServiceImplEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FieldTestServiceImplEntity>> createFieldTestServiceImplEntity(
        @Valid @RequestBody FieldTestServiceImplEntity fieldTestServiceImplEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to save FieldTestServiceImplEntity : {}", fieldTestServiceImplEntity);
        if (fieldTestServiceImplEntity.getId() != null) {
            throw new BadRequestAlertException("A new fieldTestServiceImplEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return fieldTestServiceImplEntityService.save(fieldTestServiceImplEntity).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/field-test-service-impl-entities/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /field-test-service-impl-entities/:id} : Updates an existing fieldTestServiceImplEntity.
     *
     * @param id the id of the fieldTestServiceImplEntity to save.
     * @param fieldTestServiceImplEntity the fieldTestServiceImplEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestServiceImplEntity,
     * or with status {@code 400 (Bad Request)} if the fieldTestServiceImplEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestServiceImplEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FieldTestServiceImplEntity>> updateFieldTestServiceImplEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FieldTestServiceImplEntity fieldTestServiceImplEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to update FieldTestServiceImplEntity : {}, {}", id, fieldTestServiceImplEntity);
        if (fieldTestServiceImplEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestServiceImplEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestServiceImplEntityRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return fieldTestServiceImplEntityService
                .update(fieldTestServiceImplEntity)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /field-test-service-impl-entities/:id} : Partial updates given fields of an existing fieldTestServiceImplEntity, field will ignore if it is null
     *
     * @param id the id of the fieldTestServiceImplEntity to save.
     * @param fieldTestServiceImplEntity the fieldTestServiceImplEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestServiceImplEntity,
     * or with status {@code 400 (Bad Request)} if the fieldTestServiceImplEntity is not valid,
     * or with status {@code 404 (Not Found)} if the fieldTestServiceImplEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestServiceImplEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FieldTestServiceImplEntity>> partialUpdateFieldTestServiceImplEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FieldTestServiceImplEntity fieldTestServiceImplEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FieldTestServiceImplEntity partially : {}, {}", id, fieldTestServiceImplEntity);
        if (fieldTestServiceImplEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestServiceImplEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestServiceImplEntityRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<FieldTestServiceImplEntity> result = fieldTestServiceImplEntityService.partialUpdate(fieldTestServiceImplEntity);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /field-test-service-impl-entities} : get all the Field Test Service Impl Entities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Field Test Service Impl Entities in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<FieldTestServiceImplEntity>> getAllFieldTestServiceImplEntities() {
        LOG.debug("REST request to get all FieldTestServiceImplEntities");
        return fieldTestServiceImplEntityService.findAll().collectList();
    }

    /**
     * {@code GET  /field-test-service-impl-entities} : get all the Field Test Service Impl Entities as a stream.
     * @return the {@link Flux} of Field Test Service Impl Entities.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FieldTestServiceImplEntity> getAllFieldTestServiceImplEntitiesAsStream() {
        LOG.debug("REST request to get all FieldTestServiceImplEntities as a stream");
        return fieldTestServiceImplEntityService.findAll();
    }

    /**
     * {@code GET  /field-test-service-impl-entities/:id} : get the "id" fieldTestServiceImplEntity.
     *
     * @param id the id of the fieldTestServiceImplEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fieldTestServiceImplEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FieldTestServiceImplEntity>> getFieldTestServiceImplEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FieldTestServiceImplEntity : {}", id);
        Mono<FieldTestServiceImplEntity> fieldTestServiceImplEntity = fieldTestServiceImplEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fieldTestServiceImplEntity);
    }

    /**
     * {@code DELETE  /field-test-service-impl-entities/:id} : delete the "id" fieldTestServiceImplEntity.
     *
     * @param id the id of the fieldTestServiceImplEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFieldTestServiceImplEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FieldTestServiceImplEntity : {}", id);
        return fieldTestServiceImplEntityService
            .delete(id)

            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
