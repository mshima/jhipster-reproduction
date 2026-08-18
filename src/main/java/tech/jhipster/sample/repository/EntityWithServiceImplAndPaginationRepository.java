package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.EntityWithServiceImplAndPagination;

/**
 * Spring Data R2DBC repository for the EntityWithServiceImplAndPagination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityWithServiceImplAndPaginationRepository
    extends ReactiveCrudRepository<EntityWithServiceImplAndPagination, Long>, EntityWithServiceImplAndPaginationRepositoryInternal
{
    Flux<EntityWithServiceImplAndPagination> findAllBy(Pageable pageable);

    @Override
    <S extends EntityWithServiceImplAndPagination> Mono<S> save(S entity);

    @Override
    Flux<EntityWithServiceImplAndPagination> findAll();

    @Override
    Mono<EntityWithServiceImplAndPagination> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EntityWithServiceImplAndPaginationRepositoryInternal {
    <S extends EntityWithServiceImplAndPagination> Mono<S> save(S entity);

    Flux<EntityWithServiceImplAndPagination> findAllBy(Pageable pageable);

    Flux<EntityWithServiceImplAndPagination> findAll();

    Mono<EntityWithServiceImplAndPagination> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EntityWithServiceImplAndPagination> findAllBy(Pageable pageable, Criteria criteria);
}
