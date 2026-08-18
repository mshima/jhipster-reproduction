package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.Label;

/**
 * Spring Data R2DBC repository for the Label entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LabelRepository extends ReactiveCrudRepository<Label, Long>, LabelRepositoryInternal {
    Flux<Label> findAllBy(Pageable pageable);

    @Override
    <S extends Label> Mono<S> save(S entity);

    @Override
    Flux<Label> findAll();

    @Override
    Mono<Label> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface LabelRepositoryInternal {
    <S extends Label> Mono<S> save(S entity);

    Flux<Label> findAllBy(Pageable pageable);

    Flux<Label> findAll();

    Mono<Label> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Label> findAllBy(Pageable pageable, Criteria criteria);
}
