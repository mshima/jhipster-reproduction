package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.MapsIdUserProfileWithDTO;

/**
 * Spring Data R2DBC repository for the MapsIdUserProfileWithDTO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MapsIdUserProfileWithDTORepository
    extends ReactiveCrudRepository<MapsIdUserProfileWithDTO, Long>, MapsIdUserProfileWithDTORepositoryInternal
{
    @Override
    Mono<MapsIdUserProfileWithDTO> findOneWithEagerRelationships(Long id);

    @Override
    Flux<MapsIdUserProfileWithDTO> findAllWithEagerRelationships();

    @Override
    Flux<MapsIdUserProfileWithDTO> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM maps_id_user_profile_with_dto entity WHERE entity.id = :id")
    Flux<MapsIdUserProfileWithDTO> findByUser(Long id);

    @Query("SELECT * FROM maps_id_user_profile_with_dto entity WHERE entity.id IS NULL")
    Flux<MapsIdUserProfileWithDTO> findAllWhereUserIsNull();

    @Override
    <S extends MapsIdUserProfileWithDTO> Mono<S> save(S entity);

    @Override
    Flux<MapsIdUserProfileWithDTO> findAll();

    @Override
    Mono<MapsIdUserProfileWithDTO> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MapsIdUserProfileWithDTORepositoryInternal {
    <S extends MapsIdUserProfileWithDTO> Mono<S> save(S entity);

    Flux<MapsIdUserProfileWithDTO> findAllBy(Pageable pageable);

    Flux<MapsIdUserProfileWithDTO> findAll();

    Mono<MapsIdUserProfileWithDTO> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MapsIdUserProfileWithDTO> findAllBy(Pageable pageable, Criteria criteria);

    Mono<MapsIdUserProfileWithDTO> findOneWithEagerRelationships(Long id);

    Flux<MapsIdUserProfileWithDTO> findAllWithEagerRelationships();

    Flux<MapsIdUserProfileWithDTO> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
