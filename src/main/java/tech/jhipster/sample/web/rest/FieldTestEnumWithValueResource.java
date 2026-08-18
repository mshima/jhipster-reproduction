package tech.jhipster.sample.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
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
import tech.jhipster.sample.domain.FieldTestEnumWithValue;
import tech.jhipster.sample.repository.FieldTestEnumWithValueRepository;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.FieldTestEnumWithValue}.
 */
@RestController
@RequestMapping("/api/field-test-enum-with-values")
@Transactional(rollbackFor = Exception.class)
public class FieldTestEnumWithValueResource {

    private static final Logger LOG = LoggerFactory.getLogger(FieldTestEnumWithValueResource.class);

    private static final String ENTITY_NAME = "fieldTestEnumWithValue";

    @Value("${jhipster.clientApp.name:sampleWebfluxPsql}")
    private String applicationName;

    private final FieldTestEnumWithValueRepository fieldTestEnumWithValueRepository;

    public FieldTestEnumWithValueResource(FieldTestEnumWithValueRepository fieldTestEnumWithValueRepository) {
        this.fieldTestEnumWithValueRepository = fieldTestEnumWithValueRepository;
    }

    /**
     * {@code POST  /field-test-enum-with-values} : Create a new fieldTestEnumWithValue.
     *
     * @param fieldTestEnumWithValue the fieldTestEnumWithValue to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fieldTestEnumWithValue, or with status {@code 400 (Bad Request)} if the fieldTestEnumWithValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FieldTestEnumWithValue>> createFieldTestEnumWithValue(
        @RequestBody FieldTestEnumWithValue fieldTestEnumWithValue
    ) throws URISyntaxException {
        LOG.debug("REST request to save FieldTestEnumWithValue : {}", fieldTestEnumWithValue);
        if (fieldTestEnumWithValue.getId() != null) {
            throw new BadRequestAlertException("A new fieldTestEnumWithValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return fieldTestEnumWithValueRepository.save(fieldTestEnumWithValue).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/field-test-enum-with-values/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /field-test-enum-with-values/:id} : Updates an existing fieldTestEnumWithValue.
     *
     * @param id the id of the fieldTestEnumWithValue to save.
     * @param fieldTestEnumWithValue the fieldTestEnumWithValue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestEnumWithValue,
     * or with status {@code 400 (Bad Request)} if the fieldTestEnumWithValue is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestEnumWithValue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FieldTestEnumWithValue>> updateFieldTestEnumWithValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FieldTestEnumWithValue fieldTestEnumWithValue
    ) throws URISyntaxException {
        LOG.debug("REST request to update FieldTestEnumWithValue : {}, {}", id, fieldTestEnumWithValue);
        if (fieldTestEnumWithValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestEnumWithValue.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestEnumWithValueRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return fieldTestEnumWithValueRepository
                .save(fieldTestEnumWithValue)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /field-test-enum-with-values/:id} : Partial updates given fields of an existing fieldTestEnumWithValue, field will ignore if it is null
     *
     * @param id the id of the fieldTestEnumWithValue to save.
     * @param fieldTestEnumWithValue the fieldTestEnumWithValue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTestEnumWithValue,
     * or with status {@code 400 (Bad Request)} if the fieldTestEnumWithValue is not valid,
     * or with status {@code 404 (Not Found)} if the fieldTestEnumWithValue is not found,
     * or with status {@code 500 (Internal Server Error)} if the fieldTestEnumWithValue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FieldTestEnumWithValue>> partialUpdateFieldTestEnumWithValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FieldTestEnumWithValue fieldTestEnumWithValue
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FieldTestEnumWithValue partially : {}, {}", id, fieldTestEnumWithValue);
        if (fieldTestEnumWithValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTestEnumWithValue.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fieldTestEnumWithValueRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<FieldTestEnumWithValue> result = fieldTestEnumWithValueRepository
                .findById(fieldTestEnumWithValue.getId())
                .map(existingFieldTestEnumWithValue -> {
                    updateIfPresent(existingFieldTestEnumWithValue::setMyFieldA, fieldTestEnumWithValue.getMyFieldA());
                    updateIfPresent(existingFieldTestEnumWithValue::setMyFieldB, fieldTestEnumWithValue.getMyFieldB());
                    updateIfPresent(existingFieldTestEnumWithValue::setMyFieldC, fieldTestEnumWithValue.getMyFieldC());
                    updateIfPresent(existingFieldTestEnumWithValue::setMyFieldD, fieldTestEnumWithValue.getMyFieldD());
                    updateIfPresent(existingFieldTestEnumWithValue::setMyFieldE, fieldTestEnumWithValue.getMyFieldE());

                    return existingFieldTestEnumWithValue;
                })
                .flatMap(fieldTestEnumWithValueRepository::save);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /field-test-enum-with-values} : get all the Field Test Enum With Values.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Field Test Enum With Values in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<FieldTestEnumWithValue>> getAllFieldTestEnumWithValues() {
        LOG.debug("REST request to get all FieldTestEnumWithValues");
        return fieldTestEnumWithValueRepository.findAll().collectList();
    }

    /**
     * {@code GET  /field-test-enum-with-values} : get all the Field Test Enum With Values as a stream.
     * @return the {@link Flux} of Field Test Enum With Values.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FieldTestEnumWithValue> getAllFieldTestEnumWithValuesAsStream() {
        LOG.debug("REST request to get all FieldTestEnumWithValues as a stream");
        return fieldTestEnumWithValueRepository.findAll();
    }

    /**
     * {@code GET  /field-test-enum-with-values/:id} : get the "id" fieldTestEnumWithValue.
     *
     * @param id the id of the fieldTestEnumWithValue to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fieldTestEnumWithValue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FieldTestEnumWithValue>> getFieldTestEnumWithValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FieldTestEnumWithValue : {}", id);
        Mono<FieldTestEnumWithValue> fieldTestEnumWithValue = fieldTestEnumWithValueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fieldTestEnumWithValue);
    }

    /**
     * {@code DELETE  /field-test-enum-with-values/:id} : delete the "id" fieldTestEnumWithValue.
     *
     * @param id the id of the fieldTestEnumWithValue to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFieldTestEnumWithValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FieldTestEnumWithValue : {}", id);
        return fieldTestEnumWithValueRepository
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
