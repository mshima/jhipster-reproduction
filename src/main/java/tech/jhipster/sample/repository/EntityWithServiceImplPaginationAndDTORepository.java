package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.EntityWithServiceImplPaginationAndDTO;

/**
 * Spring Data R2DBC repository for the EntityWithServiceImplPaginationAndDTO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityWithServiceImplPaginationAndDTORepository
    extends ReactiveCrudRepository<EntityWithServiceImplPaginationAndDTO, Long>, EntityWithServiceImplPaginationAndDTORepositoryInternal
{
    Flux<EntityWithServiceImplPaginationAndDTO> findAllBy(Pageable pageable);

    @Override
    <S extends EntityWithServiceImplPaginationAndDTO> Mono<S> save(S entity);

    @Override
    Flux<EntityWithServiceImplPaginationAndDTO> findAll();

    @Override
    Mono<EntityWithServiceImplPaginationAndDTO> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EntityWithServiceImplPaginationAndDTORepositoryInternal {
    <S extends EntityWithServiceImplPaginationAndDTO> Mono<S> save(S entity);

    Flux<EntityWithServiceImplPaginationAndDTO> findAllBy(Pageable pageable);

    Flux<EntityWithServiceImplPaginationAndDTO> findAll();

    Mono<EntityWithServiceImplPaginationAndDTO> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EntityWithServiceImplPaginationAndDTO> findAllBy(Pageable pageable, Criteria criteria);
}
