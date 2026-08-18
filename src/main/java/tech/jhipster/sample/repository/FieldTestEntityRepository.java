package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.FieldTestEntity;

/**
 * Spring Data R2DBC repository for the FieldTestEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FieldTestEntityRepository extends ReactiveCrudRepository<FieldTestEntity, Long>, FieldTestEntityRepositoryInternal {
    @Override
    <S extends FieldTestEntity> Mono<S> save(S entity);

    @Override
    Flux<FieldTestEntity> findAll();

    @Override
    Mono<FieldTestEntity> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FieldTestEntityRepositoryInternal {
    <S extends FieldTestEntity> Mono<S> save(S entity);

    Flux<FieldTestEntity> findAllBy(Pageable pageable);

    Flux<FieldTestEntity> findAll();

    Mono<FieldTestEntity> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FieldTestEntity> findAllBy(Pageable pageable, Criteria criteria);
}
