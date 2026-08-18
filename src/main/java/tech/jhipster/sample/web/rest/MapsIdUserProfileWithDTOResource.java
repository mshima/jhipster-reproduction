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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.repository.MapsIdUserProfileWithDTORepository;
import tech.jhipster.sample.service.MapsIdUserProfileWithDTOService;
import tech.jhipster.sample.service.dto.MapsIdUserProfileWithDTODTO;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.MapsIdUserProfileWithDTO}.
 */
@RestController
@RequestMapping("/api/maps-id-user-profile-with-dtos")
public class MapsIdUserProfileWithDTOResource {

    private static final Logger LOG = LoggerFactory.getLogger(MapsIdUserProfileWithDTOResource.class);

    private static final String ENTITY_NAME = "mapsIdUserProfileWithDTO";

    @Value("${jhipster.clientApp.name:sampleWebfluxPsql}")
    private String applicationName;

    private final MapsIdUserProfileWithDTOService mapsIdUserProfileWithDTOService;

    private final MapsIdUserProfileWithDTORepository mapsIdUserProfileWithDTORepository;

    public MapsIdUserProfileWithDTOResource(
        MapsIdUserProfileWithDTOService mapsIdUserProfileWithDTOService,
        MapsIdUserProfileWithDTORepository mapsIdUserProfileWithDTORepository
    ) {
        this.mapsIdUserProfileWithDTOService = mapsIdUserProfileWithDTOService;
        this.mapsIdUserProfileWithDTORepository = mapsIdUserProfileWithDTORepository;
    }

    /**
     * {@code POST  /maps-id-user-profile-with-dtos} : Create a new mapsIdUserProfileWithDTO.
     *
     * @param mapsIdUserProfileWithDTODTO the mapsIdUserProfileWithDTODTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mapsIdUserProfileWithDTODTO, or with status {@code 400 (Bad Request)} if the mapsIdUserProfileWithDTO has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MapsIdUserProfileWithDTODTO>> createMapsIdUserProfileWithDTO(
        @RequestBody MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save MapsIdUserProfileWithDTO : {}", mapsIdUserProfileWithDTODTO);
        if (mapsIdUserProfileWithDTODTO.getId() != null) {
            throw new BadRequestAlertException("A new mapsIdUserProfileWithDTO cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(mapsIdUserProfileWithDTODTO.getUser())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        return mapsIdUserProfileWithDTOService.save(mapsIdUserProfileWithDTODTO).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/maps-id-user-profile-with-dtos/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /maps-id-user-profile-with-dtos/:id} : Updates an existing mapsIdUserProfileWithDTO.
     *
     * @param id the id of the mapsIdUserProfileWithDTODTO to save.
     * @param mapsIdUserProfileWithDTODTO the mapsIdUserProfileWithDTODTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mapsIdUserProfileWithDTODTO,
     * or with status {@code 400 (Bad Request)} if the mapsIdUserProfileWithDTODTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mapsIdUserProfileWithDTODTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<MapsIdUserProfileWithDTODTO>> updateMapsIdUserProfileWithDTO(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MapsIdUserProfileWithDTO : {}, {}", id, mapsIdUserProfileWithDTODTO);
        if (mapsIdUserProfileWithDTODTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mapsIdUserProfileWithDTODTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return mapsIdUserProfileWithDTORepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return mapsIdUserProfileWithDTOService
                .update(mapsIdUserProfileWithDTODTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /maps-id-user-profile-with-dtos/:id} : Partial updates given fields of an existing mapsIdUserProfileWithDTO, field will ignore if it is null
     *
     * @param id the id of the mapsIdUserProfileWithDTODTO to save.
     * @param mapsIdUserProfileWithDTODTO the mapsIdUserProfileWithDTODTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mapsIdUserProfileWithDTODTO,
     * or with status {@code 400 (Bad Request)} if the mapsIdUserProfileWithDTODTO is not valid,
     * or with status {@code 404 (Not Found)} if the mapsIdUserProfileWithDTODTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mapsIdUserProfileWithDTODTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MapsIdUserProfileWithDTODTO>> partialUpdateMapsIdUserProfileWithDTO(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MapsIdUserProfileWithDTO partially : {}, {}", id, mapsIdUserProfileWithDTODTO);
        if (mapsIdUserProfileWithDTODTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mapsIdUserProfileWithDTODTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return mapsIdUserProfileWithDTORepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<MapsIdUserProfileWithDTODTO> result = mapsIdUserProfileWithDTOService.partialUpdate(mapsIdUserProfileWithDTODTO);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /maps-id-user-profile-with-dtos} : get all the Maps Id User Profile With DTOS.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Maps Id User Profile With DTOS in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<MapsIdUserProfileWithDTODTO>> getAllMapsIdUserProfileWithDTOS(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all MapsIdUserProfileWithDTOS");
        return mapsIdUserProfileWithDTOService.findAll().collectList();
    }

    /**
     * {@code GET  /maps-id-user-profile-with-dtos} : get all the Maps Id User Profile With DTOS as a stream.
     * @return the {@link Flux} of Maps Id User Profile With DTOS.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MapsIdUserProfileWithDTODTO> getAllMapsIdUserProfileWithDTOSAsStream() {
        LOG.debug("REST request to get all MapsIdUserProfileWithDTOS as a stream");
        return mapsIdUserProfileWithDTOService.findAll();
    }

    /**
     * {@code GET  /maps-id-user-profile-with-dtos/:id} : get the "id" mapsIdUserProfileWithDTO.
     *
     * @param id the id of the mapsIdUserProfileWithDTODTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mapsIdUserProfileWithDTODTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MapsIdUserProfileWithDTODTO>> getMapsIdUserProfileWithDTO(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MapsIdUserProfileWithDTO : {}", id);
        Mono<MapsIdUserProfileWithDTODTO> mapsIdUserProfileWithDTODTO = mapsIdUserProfileWithDTOService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mapsIdUserProfileWithDTODTO);
    }

    /**
     * {@code DELETE  /maps-id-user-profile-with-dtos/:id} : delete the "id" mapsIdUserProfileWithDTO.
     *
     * @param id the id of the mapsIdUserProfileWithDTODTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMapsIdUserProfileWithDTO(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MapsIdUserProfileWithDTO : {}", id);
        return mapsIdUserProfileWithDTOService
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
