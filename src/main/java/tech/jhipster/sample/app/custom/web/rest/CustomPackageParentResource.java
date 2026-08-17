package tech.jhipster.sample.app.custom.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
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
import tech.jhipster.sample.app.custom.repository.CustomPackageParentRepository;
import tech.jhipster.sample.app.custom.service.CustomPackageParentService;
import tech.jhipster.sample.app.custom.service.dto.CustomPackageParentDTO;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.app.custom.domain.CustomPackageParent}.
 */
@RestController
@RequestMapping("/api/custom-package-parents")
public class CustomPackageParentResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomPackageParentResource.class);

    private static final String ENTITY_NAME = "customPackageParent";

    @Value("${jhipster.clientApp.name:sampleWebfluxH2mem}")
    private String applicationName;

    private final CustomPackageParentService customPackageParentService;

    private final CustomPackageParentRepository customPackageParentRepository;

    public CustomPackageParentResource(
        CustomPackageParentService customPackageParentService,
        CustomPackageParentRepository customPackageParentRepository
    ) {
        this.customPackageParentService = customPackageParentService;
        this.customPackageParentRepository = customPackageParentRepository;
    }

    /**
     * {@code POST  /custom-package-parents} : Create a new customPackageParent.
     *
     * @param customPackageParentDTO the customPackageParentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customPackageParentDTO, or with status {@code 400 (Bad Request)} if the customPackageParent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CustomPackageParentDTO>> createCustomPackageParent(
        @RequestBody CustomPackageParentDTO customPackageParentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save CustomPackageParent : {}", customPackageParentDTO);
        if (customPackageParentDTO.getId() != null) {
            throw new BadRequestAlertException("A new customPackageParent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return customPackageParentService.save(customPackageParentDTO).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/custom-package-parents/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /custom-package-parents/:id} : Updates an existing customPackageParent.
     *
     * @param id the id of the customPackageParentDTO to save.
     * @param customPackageParentDTO the customPackageParentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customPackageParentDTO,
     * or with status {@code 400 (Bad Request)} if the customPackageParentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customPackageParentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomPackageParentDTO>> updateCustomPackageParent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomPackageParentDTO customPackageParentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomPackageParent : {}, {}", id, customPackageParentDTO);
        if (customPackageParentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customPackageParentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return customPackageParentRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return customPackageParentService
                .update(customPackageParentDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /custom-package-parents/:id} : Partial updates given fields of an existing customPackageParent, field will ignore if it is null
     *
     * @param id the id of the customPackageParentDTO to save.
     * @param customPackageParentDTO the customPackageParentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customPackageParentDTO,
     * or with status {@code 400 (Bad Request)} if the customPackageParentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customPackageParentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customPackageParentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CustomPackageParentDTO>> partialUpdateCustomPackageParent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomPackageParentDTO customPackageParentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomPackageParent partially : {}, {}", id, customPackageParentDTO);
        if (customPackageParentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customPackageParentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return customPackageParentRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<CustomPackageParentDTO> result = customPackageParentService.partialUpdate(customPackageParentDTO);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /custom-package-parents} : get all the Custom Package Parents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Custom Package Parents in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<CustomPackageParentDTO>> getAllCustomPackageParents() {
        LOG.debug("REST request to get all CustomPackageParents");
        return customPackageParentService.findAll().collectList();
    }

    /**
     * {@code GET  /custom-package-parents} : get all the Custom Package Parents as a stream.
     * @return the {@link Flux} of Custom Package Parents.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CustomPackageParentDTO> getAllCustomPackageParentsAsStream() {
        LOG.debug("REST request to get all CustomPackageParents as a stream");
        return customPackageParentService.findAll();
    }

    /**
     * {@code GET  /custom-package-parents/:id} : get the "id" customPackageParent.
     *
     * @param id the id of the customPackageParentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customPackageParentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomPackageParentDTO>> getCustomPackageParent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomPackageParent : {}", id);
        Mono<CustomPackageParentDTO> customPackageParentDTO = customPackageParentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customPackageParentDTO);
    }

    /**
     * {@code DELETE  /custom-package-parents/:id} : delete the "id" customPackageParent.
     *
     * @param id the id of the customPackageParentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomPackageParent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomPackageParent : {}", id);
        return customPackageParentService
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
