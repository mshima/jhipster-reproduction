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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.EntityWithPaginationAndDTO;
import tech.jhipster.sample.repository.EntityWithPaginationAndDTORepository;
import tech.jhipster.sample.service.dto.EntityWithPaginationAndDTODTO;
import tech.jhipster.sample.service.mapper.EntityWithPaginationAndDTOMapper;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.EntityWithPaginationAndDTO}.
 */
@RestController
@RequestMapping("/api/entity-with-pagination-and-dtos")
@Transactional(rollbackFor = Exception.class)
public class EntityWithPaginationAndDTOResource {

    private static final Logger LOG = LoggerFactory.getLogger(EntityWithPaginationAndDTOResource.class);

    private static final String ENTITY_NAME = "entityWithPaginationAndDTO";

    @Value("${jhipster.clientApp.name:sampleWebfluxPsql}")
    private String applicationName;

    private final EntityWithPaginationAndDTORepository entityWithPaginationAndDTORepository;

    private final EntityWithPaginationAndDTOMapper entityWithPaginationAndDTOMapper;

    public EntityWithPaginationAndDTOResource(
        EntityWithPaginationAndDTORepository entityWithPaginationAndDTORepository,
        EntityWithPaginationAndDTOMapper entityWithPaginationAndDTOMapper
    ) {
        this.entityWithPaginationAndDTORepository = entityWithPaginationAndDTORepository;
        this.entityWithPaginationAndDTOMapper = entityWithPaginationAndDTOMapper;
    }

    /**
     * {@code POST  /entity-with-pagination-and-dtos} : Create a new entityWithPaginationAndDTO.
     *
     * @param entityWithPaginationAndDTODTO the entityWithPaginationAndDTODTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entityWithPaginationAndDTODTO, or with status {@code 400 (Bad Request)} if the entityWithPaginationAndDTO has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<EntityWithPaginationAndDTODTO>> createEntityWithPaginationAndDTO(
        @RequestBody EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save EntityWithPaginationAndDTO : {}", entityWithPaginationAndDTODTO);
        if (entityWithPaginationAndDTODTO.getId() != null) {
            throw new BadRequestAlertException("A new entityWithPaginationAndDTO cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return entityWithPaginationAndDTORepository
            .save(entityWithPaginationAndDTOMapper.toEntity(entityWithPaginationAndDTODTO))
            .map(entityWithPaginationAndDTOMapper::toDto)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/entity-with-pagination-and-dtos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /entity-with-pagination-and-dtos/:id} : Updates an existing entityWithPaginationAndDTO.
     *
     * @param id the id of the entityWithPaginationAndDTODTO to save.
     * @param entityWithPaginationAndDTODTO the entityWithPaginationAndDTODTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityWithPaginationAndDTODTO,
     * or with status {@code 400 (Bad Request)} if the entityWithPaginationAndDTODTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entityWithPaginationAndDTODTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EntityWithPaginationAndDTODTO>> updateEntityWithPaginationAndDTO(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EntityWithPaginationAndDTO : {}, {}", id, entityWithPaginationAndDTODTO);
        if (entityWithPaginationAndDTODTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entityWithPaginationAndDTODTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return entityWithPaginationAndDTORepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return entityWithPaginationAndDTORepository
                .save(entityWithPaginationAndDTOMapper.toEntity(entityWithPaginationAndDTODTO))
                .map(entityWithPaginationAndDTOMapper::toDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /entity-with-pagination-and-dtos/:id} : Partial updates given fields of an existing entityWithPaginationAndDTO, field will ignore if it is null
     *
     * @param id the id of the entityWithPaginationAndDTODTO to save.
     * @param entityWithPaginationAndDTODTO the entityWithPaginationAndDTODTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityWithPaginationAndDTODTO,
     * or with status {@code 400 (Bad Request)} if the entityWithPaginationAndDTODTO is not valid,
     * or with status {@code 404 (Not Found)} if the entityWithPaginationAndDTODTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the entityWithPaginationAndDTODTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EntityWithPaginationAndDTODTO>> partialUpdateEntityWithPaginationAndDTO(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntityWithPaginationAndDTODTO entityWithPaginationAndDTODTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EntityWithPaginationAndDTO partially : {}, {}", id, entityWithPaginationAndDTODTO);
        if (entityWithPaginationAndDTODTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entityWithPaginationAndDTODTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return entityWithPaginationAndDTORepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<EntityWithPaginationAndDTODTO> result = entityWithPaginationAndDTORepository
                .findById(entityWithPaginationAndDTODTO.getId())
                .map(existingEntityWithPaginationAndDTO -> {
                    entityWithPaginationAndDTOMapper.partialUpdate(existingEntityWithPaginationAndDTO, entityWithPaginationAndDTODTO);

                    return existingEntityWithPaginationAndDTO;
                })
                .flatMap(entityWithPaginationAndDTORepository::save)
                .map(entityWithPaginationAndDTOMapper::toDto);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /entity-with-pagination-and-dtos} : get all the Entity With Pagination And DTOS.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Entity With Pagination And DTOS in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<EntityWithPaginationAndDTODTO>>> getAllEntityWithPaginationAndDTOS(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of EntityWithPaginationAndDTOS");
        return entityWithPaginationAndDTORepository
            .count()
            .zipWith(entityWithPaginationAndDTORepository.findAllBy(pageable).map(entityWithPaginationAndDTOMapper::toDto).collectList())
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
     * {@code GET  /entity-with-pagination-and-dtos/:id} : get the "id" entityWithPaginationAndDTO.
     *
     * @param id the id of the entityWithPaginationAndDTODTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entityWithPaginationAndDTODTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EntityWithPaginationAndDTODTO>> getEntityWithPaginationAndDTO(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EntityWithPaginationAndDTO : {}", id);
        Mono<EntityWithPaginationAndDTODTO> entityWithPaginationAndDTODTO = entityWithPaginationAndDTORepository
            .findById(id)
            .map(entityWithPaginationAndDTOMapper::toDto);
        return ResponseUtil.wrapOrNotFound(entityWithPaginationAndDTODTO);
    }

    /**
     * {@code DELETE  /entity-with-pagination-and-dtos/:id} : delete the "id" entityWithPaginationAndDTO.
     *
     * @param id the id of the entityWithPaginationAndDTODTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEntityWithPaginationAndDTO(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EntityWithPaginationAndDTO : {}", id);
        return entityWithPaginationAndDTORepository
            .deleteById(id)

            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
