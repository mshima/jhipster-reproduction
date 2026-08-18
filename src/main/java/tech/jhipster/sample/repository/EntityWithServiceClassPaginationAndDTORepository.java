package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.EntityWithServiceClassPaginationAndDTO;

/**
 * Spring Data R2DBC repository for the EntityWithServiceClassPaginationAndDTO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityWithServiceClassPaginationAndDTORepository
    extends ReactiveCrudRepository<EntityWithServiceClassPaginationAndDTO, Long>, EntityWithServiceClassPaginationAndDTORepositoryInternal
{
    Flux<EntityWithServiceClassPaginationAndDTO> findAllBy(Pageable pageable);

    @Override
    <S extends EntityWithServiceClassPaginationAndDTO> Mono<S> save(S entity);

    @Override
    Flux<EntityWithServiceClassPaginationAndDTO> findAll();

    @Override
    Mono<EntityWithServiceClassPaginationAndDTO> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EntityWithServiceClassPaginationAndDTORepositoryInternal {
    <S extends EntityWithServiceClassPaginationAndDTO> Mono<S> save(S entity);

    Flux<EntityWithServiceClassPaginationAndDTO> findAllBy(Pageable pageable);

    Flux<EntityWithServiceClassPaginationAndDTO> findAll();

    Mono<EntityWithServiceClassPaginationAndDTO> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EntityWithServiceClassPaginationAndDTO> findAllBy(Pageable pageable, Criteria criteria);
}
