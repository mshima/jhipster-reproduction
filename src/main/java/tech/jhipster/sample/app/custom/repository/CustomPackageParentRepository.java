package tech.jhipster.sample.app.custom.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.app.custom.domain.CustomPackageParent;

/**
 * Spring Data R2DBC repository for the CustomPackageParent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomPackageParentRepository
    extends ReactiveCrudRepository<CustomPackageParent, Long>, CustomPackageParentRepositoryInternal
{
    @Override
    <S extends CustomPackageParent> Mono<S> save(S entity);

    @Override
    Flux<CustomPackageParent> findAll();

    @Override
    Mono<CustomPackageParent> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CustomPackageParentRepositoryInternal {
    <S extends CustomPackageParent> Mono<S> save(S entity);

    Flux<CustomPackageParent> findAllBy(Pageable pageable);

    Flux<CustomPackageParent> findAll();

    Mono<CustomPackageParent> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CustomPackageParent> findAllBy(Pageable pageable, Criteria criteria);
}
