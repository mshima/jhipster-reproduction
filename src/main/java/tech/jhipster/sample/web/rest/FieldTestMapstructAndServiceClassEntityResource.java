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
import tech.jhipster.sample.repository.FieldTestMapstructAndServiceClassEntityRepository;
import tech.jhipster.sample.service.FieldTestMapstructAndServiceClassEntityService;
import tech.jhipster.sample.service.dto.FieldTestMapstructAndServiceClassEntityDTO;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.FieldTestMapstructAndServiceClassEntity}.
 */
@RestController
@RequestMapping("/api/field-test-mapstruct-and-service-class-entities")
public class FieldTestMapstructAndServiceClassEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(FieldTestMapstructAndServiceClassEntityResource.class);

    private static final String ENTITY_NAME = "fieldTestMapstructAndServiceClassEntity";

    @Value("${jhipster.clientApp.name:sampleWebfluxPsql}")
    private String applicationName;

    private final FieldTestMapstructAndServiceClassEntityService fieldTestMapstructAndServiceClassEntityService;

    private final FieldTestMapstructAndServiceClassEntityRepository fieldTestMapstructAndServiceClassEntityRepository;

    public FieldTestMapstructAndServiceClassEntityResource(
        FieldTestMapstructAndServiceClassEntityService fieldTestMapstructAndServiceClassEntityService,
        FieldTestMapstructAndServiceClassEntityRepository fieldTestMapstructAndServiceClassEntityRepository
    ) {
        this.fieldTestMapstructAndServiceClassEntityService = fieldTestMapstructAndServiceClassEntityService;
        this.fieldTestMapstructAndServiceClassEntityRepository = fieldTestMapstructAndServiceClassEntityRepository;
    }

    /**
     * {@code POST  /field-test-mapstruct-and-service-class-entities} : Create a new fieldTestMapstructAndServiceClassEntity.
     *
     * @param fieldTestMapstructAndServiceClassEntityDTO the fieldTestMapstructAndServiceClassEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fieldTestMapstructAndServiceClassEntityDTO, or with status {@code 400 (Bad Request)} if the fieldTestMapstructAndServiceClassEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FieldTestMapstructAndServiceClassEntityDTO>> createFieldTestMapstructAndServiceClassEntity(
        @Valid @RequestBody FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save FieldTestMapstructAndServiceClassEntity : {}", fieldTestMapstructAndServiceClassEntityDTO);
        if (fieldTestMapstructAndServiceClassEntityDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new fieldTestMapstructAndServiceClassEntity cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        return fieldTestMapstructAndServiceClassEntityService.save(fieldTestMapstructAndServiceClassEntityDTO).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/field-test-mapstruct-and-service-class-entities/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /field-test-mapstruct-and-service-class-entities/:id} : Updates an existing fieldTestMapstructAndServiceClassEntity.
     *
     * @param id the id of the fieldTestMapstructAndServiceClassEntityDTO to save.
     * @param fieldTestMapstructAndServiceClassEntityDTO the fieldTestMapstructAndServiceClassEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestMapstructAndServiceClassEntityDTO,
     * or with status {@code 400 (Bad Request)} if the fieldTestMapstructAndServiceClassEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestMapstructAndServiceClassEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FieldTestMapstructAndServiceClassEntityDTO>> updateFieldTestMapstructAndServiceClassEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to update FieldTestMapstructAndServiceClassEntity : {}, {}",
            id,
            fieldTestMapstructAndServiceClassEntityDTO
        );
        if (fieldTestMapstructAndServiceClassEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestMapstructAndServiceClassEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestMapstructAndServiceClassEntityRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return fieldTestMapstructAndServiceClassEntityService
                .update(fieldTestMapstructAndServiceClassEntityDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /field-test-mapstruct-and-service-class-entities/:id} : Partial updates given fields of an existing fieldTestMapstructAndServiceClassEntity, field will ignore if it is null
     *
     * @param id the id of the fieldTestMapstructAndServiceClassEntityDTO to save.
     * @param fieldTestMapstructAndServiceClassEntityDTO the fieldTestMapstructAndServiceClassEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestMapstructAndServiceClassEntityDTO,
     * or with status {@code 400 (Bad Request)} if the fieldTestMapstructAndServiceClassEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fieldTestMapstructAndServiceClassEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestMapstructAndServiceClassEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FieldTestMapstructAndServiceClassEntityDTO>> partialUpdateFieldTestMapstructAndServiceClassEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FieldTestMapstructAndServiceClassEntityDTO fieldTestMapstructAndServiceClassEntityDTO
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update FieldTestMapstructAndServiceClassEntity partially : {}, {}",
            id,
            fieldTestMapstructAndServiceClassEntityDTO
        );
        if (fieldTestMapstructAndServiceClassEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestMapstructAndServiceClassEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestMapstructAndServiceClassEntityRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<FieldTestMapstructAndServiceClassEntityDTO> result = fieldTestMapstructAndServiceClassEntityService.partialUpdate(
                fieldTestMapstructAndServiceClassEntityDTO
            );

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /field-test-mapstruct-and-service-class-entities} : get all the Field Test Mapstruct And Service Class Entities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Field Test Mapstruct And Service Class Entities in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<FieldTestMapstructAndServiceClassEntityDTO>> getAllFieldTestMapstructAndServiceClassEntities() {
        LOG.debug("REST request to get all FieldTestMapstructAndServiceClassEntities");
        return fieldTestMapstructAndServiceClassEntityService.findAll().collectList();
    }

    /**
     * {@code GET  /field-test-mapstruct-and-service-class-entities} : get all the Field Test Mapstruct And Service Class Entities as a stream.
     * @return the {@link Flux} of Field Test Mapstruct And Service Class Entities.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FieldTestMapstructAndServiceClassEntityDTO> getAllFieldTestMapstructAndServiceClassEntitiesAsStream() {
        LOG.debug("REST request to get all FieldTestMapstructAndServiceClassEntities as a stream");
        return fieldTestMapstructAndServiceClassEntityService.findAll();
    }

    /**
     * {@code GET  /field-test-mapstruct-and-service-class-entities/:id} : get the "id" fieldTestMapstructAndServiceClassEntity.
     *
     * @param id the id of the fieldTestMapstructAndServiceClassEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fieldTestMapstructAndServiceClassEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FieldTestMapstructAndServiceClassEntityDTO>> getFieldTestMapstructAndServiceClassEntity(
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get FieldTestMapstructAndServiceClassEntity : {}", id);
        Mono<FieldTestMapstructAndServiceClassEntityDTO> fieldTestMapstructAndServiceClassEntityDTO =
            fieldTestMapstructAndServiceClassEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fieldTestMapstructAndServiceClassEntityDTO);
    }

    /**
     * {@code DELETE  /field-test-mapstruct-and-service-class-entities/:id} : delete the "id" fieldTestMapstructAndServiceClassEntity.
     *
     * @param id the id of the fieldTestMapstructAndServiceClassEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFieldTestMapstructAndServiceClassEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FieldTestMapstructAndServiceClassEntity : {}", id);
        return fieldTestMapstructAndServiceClassEntityService
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
