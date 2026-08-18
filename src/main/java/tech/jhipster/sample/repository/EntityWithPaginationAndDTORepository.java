package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.EntityWithPaginationAndDTO;

/**
 * Spring Data R2DBC repository for the EntityWithPaginationAndDTO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityWithPaginationAndDTORepository
    extends ReactiveCrudRepository<EntityWithPaginationAndDTO, Long>, EntityWithPaginationAndDTORepositoryInternal
{
    Flux<EntityWithPaginationAndDTO> findAllBy(Pageable pageable);

    @Override
    <S extends EntityWithPaginationAndDTO> Mono<S> save(S entity);

    @Override
    Flux<EntityWithPaginationAndDTO> findAll();

    @Override
    Mono<EntityWithPaginationAndDTO> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EntityWithPaginationAndDTORepositoryInternal {
    <S extends EntityWithPaginationAndDTO> Mono<S> save(S entity);

    Flux<EntityWithPaginationAndDTO> findAllBy(Pageable pageable);

    Flux<EntityWithPaginationAndDTO> findAll();

    Mono<EntityWithPaginationAndDTO> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EntityWithPaginationAndDTO> findAllBy(Pageable pageable, Criteria criteria);
}
