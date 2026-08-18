package tech.jhipster.sample.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.EntityWithServiceImplAndPagination;
import tech.jhipster.sample.repository.EntityWithServiceImplAndPaginationRepository;
import tech.jhipster.sample.service.EntityWithServiceImplAndPaginationService;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.EntityWithServiceImplAndPagination}.
 */
@RestController
@RequestMapping("/api/entity-with-service-impl-and-paginations")
public class EntityWithServiceImplAndPaginationResource {

    private static final Logger LOG = LoggerFactory.getLogger(EntityWithServiceImplAndPaginationResource.class);

    private static final String ENTITY_NAME = "entityWithServiceImplAndPagination";

    @Value("${jhipster.clientApp.name:sampleWebfluxPsql}")
    private String applicationName;

    private final EntityWithServiceImplAndPaginationService entityWithServiceImplAndPaginationService;

    private final EntityWithServiceImplAndPaginationRepository entityWithServiceImplAndPaginationRepository;

    public EntityWithServiceImplAndPaginationResource(
        EntityWithServiceImplAndPaginationService entityWithServiceImplAndPaginationService,
        EntityWithServiceImplAndPaginationRepository entityWithServiceImplAndPaginationRepository
    ) {
        this.entityWithServiceImplAndPaginationService = entityWithServiceImplAndPaginationService;
        this.entityWithServiceImplAndPaginationRepository = entityWithServiceImplAndPaginationRepository;
    }

    /**
     * {@code POST  /entity-with-service-impl-and-paginations} : Create a new entityWithServiceImplAndPagination.
     *
     * @param entityWithServiceImplAndPagination the entityWithServiceImplAndPagination to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entityWithServiceImplAndPagination, or with status {@code 400 (Bad Request)} if the entityWithServiceImplAndPagination has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<EntityWithServiceImplAndPagination>> createEntityWithServiceImplAndPagination(
        @RequestBody EntityWithServiceImplAndPagination entityWithServiceImplAndPagination
    ) throws URISyntaxException {
        LOG.debug("REST request to save EntityWithServiceImplAndPagination : {}", entityWithServiceImplAndPagination);
        if (entityWithServiceImplAndPagination.getId() != null) {
            throw new BadRequestAlertException(
                "A new entityWithServiceImplAndPagination cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        return entityWithServiceImplAndPaginationService.save(entityWithServiceImplAndPagination).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/entity-with-service-impl-and-paginations/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /entity-with-service-impl-and-paginations/:id} : Updates an existing entityWithServiceImplAndPagination.
     *
     * @param id the id of the entityWithServiceImplAndPagination to save.
     * @param entityWithServiceImplAndPagination the entityWithServiceImplAndPagination to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityWithServiceImplAndPagination,
     * or with status {@code 400 (Bad Request)} if the entityWithServiceImplAndPagination is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entityWithServiceImplAndPagination couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EntityWithServiceImplAndPagination>> updateEntityWithServiceImplAndPagination(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntityWithServiceImplAndPagination entityWithServiceImplAndPagination
    ) throws URISyntaxException {
        LOG.debug("REST request to update EntityWithServiceImplAndPagination : {}, {}", id, entityWithServiceImplAndPagination);
        if (entityWithServiceImplAndPagination.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entityWithServiceImplAndPagination.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return entityWithServiceImplAndPaginationRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return entityWithServiceImplAndPaginationService
                .update(entityWithServiceImplAndPagination)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /entity-with-service-impl-and-paginations/:id} : Partial updates given fields of an existing entityWithServiceImplAndPagination, field will ignore if it is null
     *
     * @param id the id of the entityWithServiceImplAndPagination to save.
     * @param entityWithServiceImplAndPagination the entityWithServiceImplAndPagination to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityWithServiceImplAndPagination,
     * or with status {@code 400 (Bad Request)} if the entityWithServiceImplAndPagination is not valid,
     * or with status {@code 404 (Not Found)} if the entityWithServiceImplAndPagination is not found,
     * or with status {@code 500 (Internal Server Error)} if the entityWithServiceImplAndPagination couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EntityWithServiceImplAndPagination>> partialUpdateEntityWithServiceImplAndPagination(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntityWithServiceImplAndPagination entityWithServiceImplAndPagination
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update EntityWithServiceImplAndPagination partially : {}, {}",
            id,
            entityWithServiceImplAndPagination
        );
        if (entityWithServiceImplAndPagination.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entityWithServiceImplAndPagination.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return entityWithServiceImplAndPaginationRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<EntityWithServiceImplAndPagination> result = entityWithServiceImplAndPaginationService.partialUpdate(
                entityWithServiceImplAndPagination
            );

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /entity-with-service-impl-and-paginations} : get all the Entity With Service Impl And Paginations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Entity With Service Impl And Paginations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<EntityWithServiceImplAndPagination>>> getAllEntityWithServiceImplAndPaginations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of EntityWithServiceImplAndPaginations");
        return entityWithServiceImplAndPaginationService
            .countAll()
            .zipWith(entityWithServiceImplAndPaginationService.findAll(pageable).collectList())
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
     * {@code GET  /entity-with-service-impl-and-paginations/:id} : get the "id" entityWithServiceImplAndPagination.
     *
     * @param id the id of the entityWithServiceImplAndPagination to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entityWithServiceImplAndPagination, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EntityWithServiceImplAndPagination>> getEntityWithServiceImplAndPagination(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EntityWithServiceImplAndPagination : {}", id);
        Mono<EntityWithServiceImplAndPagination> entityWithServiceImplAndPagination = entityWithServiceImplAndPaginationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entityWithServiceImplAndPagination);
    }

    /**
     * {@code DELETE  /entity-with-service-impl-and-paginations/:id} : delete the "id" entityWithServiceImplAndPagination.
     *
     * @param id the id of the entityWithServiceImplAndPagination to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEntityWithServiceImplAndPagination(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EntityWithServiceImplAndPagination : {}", id);
        return entityWithServiceImplAndPaginationService
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
