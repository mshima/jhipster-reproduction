package tech.jhipster.sample.app.child.web.rest;

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
import tech.jhipster.sample.app.child.repository.CustomPackageChildRepository;
import tech.jhipster.sample.app.child.service.CustomPackageChildService;
import tech.jhipster.sample.app.child.service.dto.CustomPackageChildDTO;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.app.child.domain.CustomPackageChild}.
 */
@RestController
@RequestMapping("/api/custom-package-children")
public class CustomPackageChildResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomPackageChildResource.class);

    private static final String ENTITY_NAME = "customPackageChild";

    @Value("${jhipster.clientApp.name:sampleWebfluxH2mem}")
    private String applicationName;

    private final CustomPackageChildService customPackageChildService;

    private final CustomPackageChildRepository customPackageChildRepository;

    public CustomPackageChildResource(
        CustomPackageChildService customPackageChildService,
        CustomPackageChildRepository customPackageChildRepository
    ) {
        this.customPackageChildService = customPackageChildService;
        this.customPackageChildRepository = customPackageChildRepository;
    }

    /**
     * {@code POST  /custom-package-children} : Create a new customPackageChild.
     *
     * @param customPackageChildDTO the customPackageChildDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customPackageChildDTO, or with status {@code 400 (Bad Request)} if the customPackageChild has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CustomPackageChildDTO>> createCustomPackageChild(@RequestBody CustomPackageChildDTO customPackageChildDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomPackageChild : {}", customPackageChildDTO);
        if (customPackageChildDTO.getId() != null) {
            throw new BadRequestAlertException("A new customPackageChild cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return customPackageChildService.save(customPackageChildDTO).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/custom-package-children/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /custom-package-children/:id} : Updates an existing customPackageChild.
     *
     * @param id the id of the customPackageChildDTO to save.
     * @param customPackageChildDTO the customPackageChildDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customPackageChildDTO,
     * or with status {@code 400 (Bad Request)} if the customPackageChildDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customPackageChildDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomPackageChildDTO>> updateCustomPackageChild(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomPackageChildDTO customPackageChildDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomPackageChild : {}, {}", id, customPackageChildDTO);
        if (customPackageChildDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customPackageChildDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return customPackageChildRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return customPackageChildService
                .update(customPackageChildDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /custom-package-children/:id} : Partial updates given fields of an existing customPackageChild, field will ignore if it is null
     *
     * @param id the id of the customPackageChildDTO to save.
     * @param customPackageChildDTO the customPackageChildDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customPackageChildDTO,
     * or with status {@code 400 (Bad Request)} if the customPackageChildDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customPackageChildDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customPackageChildDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CustomPackageChildDTO>> partialUpdateCustomPackageChild(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomPackageChildDTO customPackageChildDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomPackageChild partially : {}, {}", id, customPackageChildDTO);
        if (customPackageChildDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customPackageChildDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return customPackageChildRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<CustomPackageChildDTO> result = customPackageChildService.partialUpdate(customPackageChildDTO);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /custom-package-children} : get all the Custom Package Children.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Custom Package Children in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<CustomPackageChildDTO>> getAllCustomPackageChildren() {
        LOG.debug("REST request to get all CustomPackageChildren");
        return customPackageChildService.findAll().collectList();
    }

    /**
     * {@code GET  /custom-package-children} : get all the Custom Package Children as a stream.
     * @return the {@link Flux} of Custom Package Children.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CustomPackageChildDTO> getAllCustomPackageChildrenAsStream() {
        LOG.debug("REST request to get all CustomPackageChildren as a stream");
        return customPackageChildService.findAll();
    }

    /**
     * {@code GET  /custom-package-children/:id} : get the "id" customPackageChild.
     *
     * @param id the id of the customPackageChildDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customPackageChildDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomPackageChildDTO>> getCustomPackageChild(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomPackageChild : {}", id);
        Mono<CustomPackageChildDTO> customPackageChildDTO = customPackageChildService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customPackageChildDTO);
    }

    /**
     * {@code DELETE  /custom-package-children/:id} : delete the "id" customPackageChild.
     *
     * @param id the id of the customPackageChildDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomPackageChild(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomPackageChild : {}", id);
        return customPackageChildService
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
