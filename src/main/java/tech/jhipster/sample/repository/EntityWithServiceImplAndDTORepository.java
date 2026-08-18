package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.EntityWithServiceImplAndDTO;

/**
 * Spring Data R2DBC repository for the EntityWithServiceImplAndDTO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityWithServiceImplAndDTORepository
    extends ReactiveCrudRepository<EntityWithServiceImplAndDTO, Long>, EntityWithServiceImplAndDTORepositoryInternal
{
    @Override
    <S extends EntityWithServiceImplAndDTO> Mono<S> save(S entity);

    @Override
    Flux<EntityWithServiceImplAndDTO> findAll();

    @Override
    Mono<EntityWithServiceImplAndDTO> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EntityWithServiceImplAndDTORepositoryInternal {
    <S extends EntityWithServiceImplAndDTO> Mono<S> save(S entity);

    Flux<EntityWithServiceImplAndDTO> findAllBy(Pageable pageable);

    Flux<EntityWithServiceImplAndDTO> findAll();

    Mono<EntityWithServiceImplAndDTO> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EntityWithServiceImplAndDTO> findAllBy(Pageable pageable, Criteria criteria);
}
