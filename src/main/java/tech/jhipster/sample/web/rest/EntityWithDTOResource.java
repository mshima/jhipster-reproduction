package tech.jhipster.sample.web.rest;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.EntityWithDTO;
import tech.jhipster.sample.repository.EntityWithDTORepository;
import tech.jhipster.sample.service.dto.EntityWithDTODTO;
import tech.jhipster.sample.service.mapper.EntityWithDTOMapper;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.EntityWithDTO}.
 */
@RestController
@RequestMapping("/api/entity-with-dtos")
@Transactional(rollbackFor = Exception.class)
public class EntityWithDTOResource {

    private static final Logger LOG = LoggerFactory.getLogger(EntityWithDTOResource.class);

    private static final String ENTITY_NAME = "entityWithDTO";

    @Value("${jhipster.clientApp.name:sampleWebfluxPsql}")
    private String applicationName;

    private final EntityWithDTORepository entityWithDTORepository;

    private final EntityWithDTOMapper entityWithDTOMapper;

    public EntityWithDTOResource(EntityWithDTORepository entityWithDTORepository, EntityWithDTOMapper entityWithDTOMapper) {
        this.entityWithDTORepository = entityWithDTORepository;
        this.entityWithDTOMapper = entityWithDTOMapper;
    }

    /**
     * {@code POST  /entity-with-dtos} : Create a new entityWithDTO.
     *
     * @param entityWithDTODTO the entityWithDTODTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entityWithDTODTO, or with status {@code 400 (Bad Request)} if the entityWithDTO has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<EntityWithDTODTO>> createEntityWithDTO(@RequestBody EntityWithDTODTO entityWithDTODTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EntityWithDTO : {}", entityWithDTODTO);
        if (entityWithDTODTO.getId() != null) {
            throw new BadRequestAlertException("A new entityWithDTO cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return entityWithDTORepository
            .save(entityWithDTOMapper.toEntity(entityWithDTODTO))
            .map(entityWithDTOMapper::toDto)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/entity-with-dtos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /entity-with-dtos/:id} : Updates an existing entityWithDTO.
     *
     * @param id the id of the entityWithDTODTO to save.
     * @param entityWithDTODTO the entityWithDTODTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityWithDTODTO,
     * or with status {@code 400 (Bad Request)} if the entityWithDTODTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entityWithDTODTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EntityWithDTODTO>> updateEntityWithDTO(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntityWithDTODTO entityWithDTODTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EntityWithDTO : {}, {}", id, entityWithDTODTO);
        if (entityWithDTODTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entityWithDTODTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return entityWithDTORepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return entityWithDTORepository
                .save(entityWithDTOMapper.toEntity(entityWithDTODTO))
                .map(entityWithDTOMapper::toDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /entity-with-dtos/:id} : Partial updates given fields of an existing entityWithDTO, field will ignore if it is null
     *
     * @param id the id of the entityWithDTODTO to save.
     * @param entityWithDTODTO the entityWithDTODTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityWithDTODTO,
     * or with status {@code 400 (Bad Request)} if the entityWithDTODTO is not valid,
     * or with status {@code 404 (Not Found)} if the entityWithDTODTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the entityWithDTODTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EntityWithDTODTO>> partialUpdateEntityWithDTO(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntityWithDTODTO entityWithDTODTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EntityWithDTO partially : {}, {}", id, entityWithDTODTO);
        if (entityWithDTODTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entityWithDTODTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return entityWithDTORepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<EntityWithDTODTO> result = entityWithDTORepository
                .findById(entityWithDTODTO.getId())
                .map(existingEntityWithDTO -> {
                    entityWithDTOMapper.partialUpdate(existingEntityWithDTO, entityWithDTODTO);

                    return existingEntityWithDTO;
                })
                .flatMap(entityWithDTORepository::save)
                .map(entityWithDTOMapper::toDto);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /entity-with-dtos} : get all the Entity With DTOS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Entity With DTOS in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<EntityWithDTODTO>> getAllEntityWithDTOS() {
        LOG.debug("REST request to get all EntityWithDTOS");
        Flux<EntityWithDTO> entityWithDTOS = entityWithDTORepository.findAll();
        return entityWithDTOS.map(entityWithDTOMapper::toDto).collectList();
    }

    /**
     * {@code GET  /entity-with-dtos} : get all the Entity With DTOS as a stream.
     * @return the {@link Flux} of Entity With DTOS.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<EntityWithDTODTO> getAllEntityWithDTOSAsStream() {
        LOG.debug("REST request to get all EntityWithDTOS as a stream");
        return entityWithDTORepository.findAll().map(entityWithDTOMapper::toDto);
    }

    /**
     * {@code GET  /entity-with-dtos/:id} : get the "id" entityWithDTO.
     *
     * @param id the id of the entityWithDTODTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entityWithDTODTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EntityWithDTODTO>> getEntityWithDTO(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EntityWithDTO : {}", id);
        Mono<EntityWithDTODTO> entityWithDTODTO = entityWithDTORepository.findById(id).map(entityWithDTOMapper::toDto);
        return ResponseUtil.wrapOrNotFound(entityWithDTODTO);
    }

    /**
     * {@code DELETE  /entity-with-dtos/:id} : delete the "id" entityWithDTO.
     *
     * @param id the id of the entityWithDTODTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEntityWithDTO(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EntityWithDTO : {}", id);
        return entityWithDTORepository
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
