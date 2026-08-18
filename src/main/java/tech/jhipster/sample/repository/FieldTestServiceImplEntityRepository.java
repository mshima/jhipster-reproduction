package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.FieldTestServiceImplEntity;

/**
 * Spring Data R2DBC repository for the FieldTestServiceImplEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FieldTestServiceImplEntityRepository
    extends ReactiveCrudRepository<FieldTestServiceImplEntity, Long>, FieldTestServiceImplEntityRepositoryInternal
{
    @Override
    <S extends FieldTestServiceImplEntity> Mono<S> save(S entity);

    @Override
    Flux<FieldTestServiceImplEntity> findAll();

    @Override
    Mono<FieldTestServiceImplEntity> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FieldTestServiceImplEntityRepositoryInternal {
    <S extends FieldTestServiceImplEntity> Mono<S> save(S entity);

    Flux<FieldTestServiceImplEntity> findAllBy(Pageable pageable);

    Flux<FieldTestServiceImplEntity> findAll();

    Mono<FieldTestServiceImplEntity> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FieldTestServiceImplEntity> findAllBy(Pageable pageable, Criteria criteria);
}
