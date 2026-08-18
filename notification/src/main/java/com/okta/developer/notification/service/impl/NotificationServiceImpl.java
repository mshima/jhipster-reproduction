package com.okta.developer.notification.service.impl;

import com.okta.developer.notification.domain.NotificationEntity;
import com.okta.developer.notification.repository.NotificationRepository;
import com.okta.developer.notification.service.NotificationService;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.okta.developer.notification.domain.NotificationEntity}.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Mono<NotificationEntity> save(NotificationEntity notificationEntity) {
        LOG.debug("Request to save Notification : {}", notificationEntity);
        return notificationRepository.save(notificationEntity);
    }

    @Override
    public Mono<NotificationEntity> update(NotificationEntity notificationEntity) {
        LOG.debug("Request to update Notification : {}", notificationEntity);
        return notificationRepository.save(notificationEntity);
    }

    @Override
    public Mono<NotificationEntity> partialUpdate(NotificationEntity notificationEntity) {
        LOG.debug("Request to partially update Notification : {}", notificationEntity);

        return notificationRepository
            .findById(notificationEntity.getId())
            .map(existingNotification -> {
                updateIfPresent(existingNotification::setTitle, notificationEntity.getTitle());

                return existingNotification;
            })
            .flatMap(notificationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<NotificationEntity> findAll() {
        LOG.debug("Request to get all Notifications");
        return notificationRepository.findAll();
    }

    public Flux<NotificationEntity> findAllWithEagerRelationships(Pageable pageable) {
        return notificationRepository.findAllWithEagerRelationships(pageable);
    }

    public Mono<Long> countAll() {
        return notificationRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<NotificationEntity> findOne(Long id) {
        LOG.debug("Request to get Notification : {}", id);
        return notificationRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Notification : {}", id);
        return notificationRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
