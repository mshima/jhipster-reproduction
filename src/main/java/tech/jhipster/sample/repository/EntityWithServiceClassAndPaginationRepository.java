package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.EntityWithServiceClassAndPagination;

/**
 * Spring Data R2DBC repository for the EntityWithServiceClassAndPagination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityWithServiceClassAndPaginationRepository
    extends ReactiveCrudRepository<EntityWithServiceClassAndPagination, Long>, EntityWithServiceClassAndPaginationRepositoryInternal
{
    Flux<EntityWithServiceClassAndPagination> findAllBy(Pageable pageable);

    @Override
    <S extends EntityWithServiceClassAndPagination> Mono<S> save(S entity);

    @Override
    Flux<EntityWithServiceClassAndPagination> findAll();

    @Override
    Mono<EntityWithServiceClassAndPagination> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EntityWithServiceClassAndPaginationRepositoryInternal {
    <S extends EntityWithServiceClassAndPagination> Mono<S> save(S entity);

    Flux<EntityWithServiceClassAndPagination> findAllBy(Pageable pageable);

    Flux<EntityWithServiceClassAndPagination> findAll();

    Mono<EntityWithServiceClassAndPagination> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EntityWithServiceClassAndPagination> findAllBy(Pageable pageable, Criteria criteria);
}
