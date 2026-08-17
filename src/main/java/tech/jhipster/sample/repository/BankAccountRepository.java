package tech.jhipster.sample.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.BankAccount;
import tech.jhipster.sample.domain.criteria.BankAccountCriteria;

/**
 * Spring Data R2DBC repository for the BankAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankAccountRepository extends ReactiveCrudRepository<BankAccount, Long>, BankAccountRepositoryInternal {
    @Override
    Mono<BankAccount> findOneWithEagerRelationships(Long id);

    @Override
    Flux<BankAccount> findAllWithEagerRelationships();

    @Override
    Flux<BankAccount> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM bank_account entity WHERE entity.user_id = :id")
    Flux<BankAccount> findByUser(Long id);

    @Query("SELECT * FROM bank_account entity WHERE entity.user_id IS NULL")
    Flux<BankAccount> findAllWhereUserIsNull();

    @Override
    <S extends BankAccount> Mono<S> save(S entity);

    @Override
    Flux<BankAccount> findAll();

    @Override
    Mono<BankAccount> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BankAccountRepositoryInternal {
    <S extends BankAccount> Mono<S> save(S entity);

    Flux<BankAccount> findAllBy(Pageable pageable);

    Flux<BankAccount> findAll();

    Mono<BankAccount> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<BankAccount> findAllBy(Pageable pageable, Criteria criteria);
    Flux<BankAccount> findByCriteria(BankAccountCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(BankAccountCriteria criteria);

    Mono<BankAccount> findOneWithEagerRelationships(Long id);

    Flux<BankAccount> findAllWithEagerRelationships();

    Flux<BankAccount> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
