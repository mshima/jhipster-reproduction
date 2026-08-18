package com.okta.developer.notification.service;

import com.okta.developer.notification.domain.NotificationEntity;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.okta.developer.notification.domain.NotificationEntity}.
 */
public interface NotificationService {
    /**
     * Save a notification.
     *
     * @param notificationEntity the entity to save.
     * @return the persisted entity.
     */
    Mono<NotificationEntity> save(NotificationEntity notificationEntity);

    /**
     * Updates a notification.
     *
     * @param notificationEntity the entity to update.
     * @return the persisted entity.
     */
    Mono<NotificationEntity> update(NotificationEntity notificationEntity);

    /**
     * Partially updates a notification.
     *
     * @param notificationEntity the entity to update partially.
     * @return the persisted entity.
     */
    Mono<NotificationEntity> partialUpdate(NotificationEntity notificationEntity);

    /**
     * Get all the notifications.
     *
     * @return the list of entities.
     */
    Flux<NotificationEntity> findAll();

    /**
     * Get all the notifications with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<NotificationEntity> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of notifications available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" notification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<NotificationEntity> findOne(Long id);

    /**
     * Delete the "id" notification.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
