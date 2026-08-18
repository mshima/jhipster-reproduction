package tech.jhipster.sample.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.BankAccount;
import tech.jhipster.sample.domain.criteria.BankAccountCriteria;

/**
 * Service Interface for managing {@link tech.jhipster.sample.domain.BankAccount}.
 */
public interface BankAccountService {
    /**
     * Save a bankAccount.
     *
     * @param bankAccount the entity to save.
     * @return the persisted entity.
     */
    Mono<BankAccount> save(BankAccount bankAccount);

    /**
     * Updates a bankAccount.
     *
     * @param bankAccount the entity to update.
     * @return the persisted entity.
     */
    Mono<BankAccount> update(BankAccount bankAccount);

    /**
     * Partially updates a bankAccount.
     *
     * @param bankAccount the entity to update partially.
     * @return the persisted entity.
     */
    Mono<BankAccount> partialUpdate(BankAccount bankAccount);
    /**
     * Find bankAccounts by criteria.
     *
     * @return the list of entities.
     */
    Flux<BankAccount> findByCriteria(BankAccountCriteria criteria);

    /**
     * Find the count of bankAccounts by criteria.
     * @param criteria filtering criteria
     * @return the count of bankAccounts
     */
    public Mono<Long> countByCriteria(BankAccountCriteria criteria);

    /**
     * Get all the bankAccounts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<BankAccount> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of bankAccounts available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of bankAccounts available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" bankAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<BankAccount> findOne(Long id);

    /**
     * Delete the "id" bankAccount.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the bankAccount corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    Flux<BankAccount> search(String query);
}
