package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.EntityWithDTO;

/**
 * Spring Data R2DBC repository for the EntityWithDTO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityWithDTORepository extends ReactiveCrudRepository<EntityWithDTO, Long>, EntityWithDTORepositoryInternal {
    @Override
    <S extends EntityWithDTO> Mono<S> save(S entity);

    @Override
    Flux<EntityWithDTO> findAll();

    @Override
    Mono<EntityWithDTO> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EntityWithDTORepositoryInternal {
    <S extends EntityWithDTO> Mono<S> save(S entity);

    Flux<EntityWithDTO> findAllBy(Pageable pageable);

    Flux<EntityWithDTO> findAll();

    Mono<EntityWithDTO> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EntityWithDTO> findAllBy(Pageable pageable, Criteria criteria);
}
