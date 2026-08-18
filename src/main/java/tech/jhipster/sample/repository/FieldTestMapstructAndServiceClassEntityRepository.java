package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.FieldTestMapstructAndServiceClassEntity;

/**
 * Spring Data R2DBC repository for the FieldTestMapstructAndServiceClassEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FieldTestMapstructAndServiceClassEntityRepository
    extends ReactiveCrudRepository<FieldTestMapstructAndServiceClassEntity, Long>, FieldTestMapstructAndServiceClassEntityRepositoryInternal
{
    @Override
    <S extends FieldTestMapstructAndServiceClassEntity> Mono<S> save(S entity);

    @Override
    Flux<FieldTestMapstructAndServiceClassEntity> findAll();

    @Override
    Mono<FieldTestMapstructAndServiceClassEntity> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FieldTestMapstructAndServiceClassEntityRepositoryInternal {
    <S extends FieldTestMapstructAndServiceClassEntity> Mono<S> save(S entity);

    Flux<FieldTestMapstructAndServiceClassEntity> findAllBy(Pageable pageable);

    Flux<FieldTestMapstructAndServiceClassEntity> findAll();

    Mono<FieldTestMapstructAndServiceClassEntity> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FieldTestMapstructAndServiceClassEntity> findAllBy(Pageable pageable, Criteria criteria);
}
