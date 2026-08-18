package tech.jhipster.sample.service.impl;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.BankAccount;
import tech.jhipster.sample.domain.criteria.BankAccountCriteria;
import tech.jhipster.sample.repository.BankAccountRepository;
import tech.jhipster.sample.repository.search.BankAccountSearchRepository;
import tech.jhipster.sample.service.BankAccountService;

/**
 * Service Implementation for managing {@link tech.jhipster.sample.domain.BankAccount}.
 */
@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    private final BankAccountRepository bankAccountRepository;

    private final BankAccountSearchRepository bankAccountSearchRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, BankAccountSearchRepository bankAccountSearchRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountSearchRepository = bankAccountSearchRepository;
    }

    @Override
    public Mono<BankAccount> save(BankAccount bankAccount) {
        LOG.debug("Request to save BankAccount : {}", bankAccount);
        return bankAccountRepository
            .save(bankAccount)

            .flatMap(bankAccountSearchRepository::save);
    }

    @Override
    public Mono<BankAccount> update(BankAccount bankAccount) {
        LOG.debug("Request to update BankAccount : {}", bankAccount);
        return bankAccountRepository
            .save(bankAccount)

            .flatMap(bankAccountSearchRepository::save);
    }

    @Override
    public Mono<BankAccount> partialUpdate(BankAccount bankAccount) {
        LOG.debug("Request to partially update BankAccount : {}", bankAccount);

        return bankAccountRepository
            .findById(bankAccount.getId())
            .map(existingBankAccount -> {
                updateIfPresent(existingBankAccount::setName, bankAccount.getName());
                updateIfPresent(existingBankAccount::setGuid, bankAccount.getGuid());
                updateIfPresent(existingBankAccount::setBankNumber, bankAccount.getBankNumber());
                updateIfPresent(existingBankAccount::setAgencyNumber, bankAccount.getAgencyNumber());
                updateIfPresent(existingBankAccount::setLastOperationDuration, bankAccount.getLastOperationDuration());
                updateIfPresent(existingBankAccount::setMeanOperationDuration, bankAccount.getMeanOperationDuration());
                updateIfPresent(existingBankAccount::setMeanQueueDuration, bankAccount.getMeanQueueDuration());
                updateIfPresent(existingBankAccount::setBalance, bankAccount.getBalance());
                updateIfPresent(existingBankAccount::setOpeningDay, bankAccount.getOpeningDay());
                updateIfPresent(existingBankAccount::setLastOperationDate, bankAccount.getLastOperationDate());
                updateIfPresent(existingBankAccount::setActive, bankAccount.getActive());
                updateIfPresent(existingBankAccount::setAccountType, bankAccount.getAccountType());
                updateIfPresent(existingBankAccount::setAttachment, bankAccount.getAttachment());
                updateIfPresent(existingBankAccount::setAttachmentContentType, bankAccount.getAttachmentContentType());
                updateIfPresent(existingBankAccount::setDescription, bankAccount.getDescription());

                return existingBankAccount;
            })
            .flatMap(bankAccountRepository::save)
            .flatMap(savedBankAccount -> {
                bankAccountSearchRepository.save(savedBankAccount);
                return Mono.just(savedBankAccount);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BankAccount> findByCriteria(BankAccountCriteria criteria) {
        LOG.debug("Request to get all BankAccounts by Criteria");
        return bankAccountRepository.findByCriteria(criteria, null);
    }

    /**
     * Find the count of bankAccounts by criteria.
     * @param criteria filtering criteria
     * @return the count of bankAccounts
     */
    public Mono<Long> countByCriteria(BankAccountCriteria criteria) {
        LOG.debug("Request to get the count of all BankAccounts by Criteria");
        return bankAccountRepository.countByCriteria(criteria);
    }

    public Flux<BankAccount> findAllWithEagerRelationships(Pageable pageable) {
        return bankAccountRepository.findAllWithEagerRelationships(pageable);
    }

    public Mono<Long> countAll() {
        return bankAccountRepository.count();
    }

    public Mono<Long> searchCount() {
        return bankAccountSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<BankAccount> findOne(Long id) {
        LOG.debug("Request to get BankAccount : {}", id);
        return bankAccountRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete BankAccount : {}", id);
        return bankAccountRepository
            .deleteById(id)

            .then(bankAccountSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BankAccount> search(String query) {
        LOG.debug("Request to search BankAccounts for query {}", query);
        try {
            return bankAccountSearchRepository.search(query);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
