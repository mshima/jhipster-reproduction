package com.okta.developer.notification.web.rest;

import com.okta.developer.notification.domain.NotificationEntity;
import com.okta.developer.notification.repository.NotificationRepository;
import com.okta.developer.notification.repository.UserRepository;
import com.okta.developer.notification.service.NotificationService;
import com.okta.developer.notification.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.okta.developer.notification.domain.NotificationEntity}.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationResource {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationResource.class);

    private static final String ENTITY_NAME = "notificationNotification";

    @Value("${jhipster.clientApp.name:notification}")
    private String applicationName;

    private final NotificationService notificationService;

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    public NotificationResource(
        NotificationService notificationService,
        NotificationRepository notificationRepository,
        UserRepository userRepository
    ) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /notifications} : Create a new notification.
     *
     * @param notificationEntity the notificationEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationEntity, or with status {@code 400 (Bad Request)} if the notification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<NotificationEntity>> createNotification(@Valid @RequestBody NotificationEntity notificationEntity)
        throws URISyntaxException {
        LOG.debug("REST request to save Notification : {}", notificationEntity);
        if (notificationEntity.getId() != null) {
            throw new BadRequestAlertException("A new notification cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (notificationEntity.getUser() != null) {
            // Save user in case it's new and only exists in gateway
            userRepository.save(notificationEntity.getUser());
        }

        return notificationService.save(notificationEntity).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/notifications/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /notifications/:id} : Updates an existing notification.
     *
     * @param id the id of the notificationEntity to save.
     * @param notificationEntity the notificationEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationEntity,
     * or with status {@code 400 (Bad Request)} if the notificationEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<NotificationEntity>> updateNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NotificationEntity notificationEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to update Notification : {}, {}", id, notificationEntity);
        if (notificationEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return notificationRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            if (notificationEntity.getUser() != null) {
                // Save user in case it's new and only exists in gateway
                userRepository.save(notificationEntity.getUser());
            }

            return notificationService
                .update(notificationEntity)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /notifications/:id} : Partial updates given fields of an existing notification, field will ignore if it is null
     *
     * @param id the id of the notificationEntity to save.
     * @param notificationEntity the notificationEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationEntity,
     * or with status {@code 400 (Bad Request)} if the notificationEntity is not valid,
     * or with status {@code 404 (Not Found)} if the notificationEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<NotificationEntity>> partialUpdateNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificationEntity notificationEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Notification partially : {}, {}", id, notificationEntity);
        if (notificationEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return notificationRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            if (notificationEntity.getUser() != null) {
                // Save user in case it's new and only exists in gateway
                userRepository.save(notificationEntity.getUser());
            }

            Mono<NotificationEntity> result = notificationService.partialUpdate(notificationEntity);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /notifications} : get all the Notifications.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Notifications in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<NotificationEntity>> getAllNotifications(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Notifications");
        return notificationService.findAll().collectList();
    }

    /**
     * {@code GET  /notifications} : get all the Notifications as a stream.
     * @return the {@link Flux} of Notifications.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<NotificationEntity> getAllNotificationsAsStream() {
        LOG.debug("REST request to get all Notifications as a stream");
        return notificationService.findAll();
    }

    /**
     * {@code GET  /notifications/:id} : get the "id" notification.
     *
     * @param id the id of the notificationEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<NotificationEntity>> getNotification(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Notification : {}", id);
        Mono<NotificationEntity> notificationEntity = notificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationEntity);
    }

    /**
     * {@code DELETE  /notifications/:id} : delete the "id" notification.
     *
     * @param id the id of the notificationEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteNotification(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Notification : {}", id);
        return notificationService
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
