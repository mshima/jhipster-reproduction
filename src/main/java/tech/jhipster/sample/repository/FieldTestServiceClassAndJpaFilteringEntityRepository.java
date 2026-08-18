package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.FieldTestServiceClassAndJpaFilteringEntity;
import tech.jhipster.sample.domain.criteria.FieldTestServiceClassAndJpaFilteringEntityCriteria;

/**
 * Spring Data R2DBC repository for the FieldTestServiceClassAndJpaFilteringEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FieldTestServiceClassAndJpaFilteringEntityRepository
    extends
        ReactiveCrudRepository<FieldTestServiceClassAndJpaFilteringEntity, Long>,
        FieldTestServiceClassAndJpaFilteringEntityRepositoryInternal
{
    @Override
    <S extends FieldTestServiceClassAndJpaFilteringEntity> Mono<S> save(S entity);

    @Override
    Flux<FieldTestServiceClassAndJpaFilteringEntity> findAll();

    @Override
    Mono<FieldTestServiceClassAndJpaFilteringEntity> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FieldTestServiceClassAndJpaFilteringEntityRepositoryInternal {
    <S extends FieldTestServiceClassAndJpaFilteringEntity> Mono<S> save(S entity);

    Flux<FieldTestServiceClassAndJpaFilteringEntity> findAllBy(Pageable pageable);

    Flux<FieldTestServiceClassAndJpaFilteringEntity> findAll();

    Mono<FieldTestServiceClassAndJpaFilteringEntity> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FieldTestServiceClassAndJpaFilteringEntity> findAllBy(Pageable pageable, Criteria criteria);
    Flux<FieldTestServiceClassAndJpaFilteringEntity> findByCriteria(
        FieldTestServiceClassAndJpaFilteringEntityCriteria criteria,
        Pageable pageable
    );

    Mono<Long> countByCriteria(FieldTestServiceClassAndJpaFilteringEntityCriteria criteria);
}
