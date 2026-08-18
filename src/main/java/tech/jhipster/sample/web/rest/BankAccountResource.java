package tech.jhipster.sample.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.BankAccount;
import tech.jhipster.sample.domain.criteria.BankAccountCriteria;
import tech.jhipster.sample.repository.BankAccountRepository;
import tech.jhipster.sample.service.BankAccountService;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.sample.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.BankAccount}.
 */
@RestController
@RequestMapping("/api/bank-accounts")
public class BankAccountResource {

    private static final Logger LOG = LoggerFactory.getLogger(BankAccountResource.class);

    private static final String ENTITY_NAME = "testRootBankAccount";

    @Value("${jhipster.clientApp.name:sampleWebfluxH2mem}")
    private String applicationName;

    private final BankAccountService bankAccountService;

    private final BankAccountRepository bankAccountRepository;

    public BankAccountResource(BankAccountService bankAccountService, BankAccountRepository bankAccountRepository) {
        this.bankAccountService = bankAccountService;
        this.bankAccountRepository = bankAccountRepository;
    }

    /**
     * {@code POST  /bank-accounts} : Create a new bankAccount.
     *
     * @param bankAccount the bankAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankAccount, or with status {@code 400 (Bad Request)} if the bankAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<BankAccount>> createBankAccount(@Valid @RequestBody BankAccount bankAccount) throws URISyntaxException {
        LOG.debug("REST request to save BankAccount : {}", bankAccount);
        if (bankAccount.getId() != null) {
            throw new BadRequestAlertException("A new bankAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return bankAccountService.save(bankAccount).map(result -> {
            try {
                return ResponseEntity.created(new URI("/api/bank-accounts/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@code PUT  /bank-accounts/:id} : Updates an existing bankAccount.
     *
     * @param id the id of the bankAccount to save.
     * @param bankAccount the bankAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankAccount,
     * or with status {@code 400 (Bad Request)} if the bankAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<BankAccount>> updateBankAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BankAccount bankAccount
    ) throws URISyntaxException {
        LOG.debug("REST request to update BankAccount : {}, {}", id, bankAccount);
        if (bankAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bankAccountRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            return bankAccountService
                .update(bankAccount)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                );
        });
    }

    /**
     * {@code PATCH  /bank-accounts/:id} : Partial updates given fields of an existing bankAccount, field will ignore if it is null
     *
     * @param id the id of the bankAccount to save.
     * @param bankAccount the bankAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankAccount,
     * or with status {@code 400 (Bad Request)} if the bankAccount is not valid,
     * or with status {@code 404 (Not Found)} if the bankAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the bankAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BankAccount>> partialUpdateBankAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BankAccount bankAccount
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BankAccount partially : {}, {}", id, bankAccount);
        if (bankAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bankAccountRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<BankAccount> result = bankAccountService.partialUpdate(bankAccount);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
        });
    }

    /**
     * {@code GET  /bank-accounts} : get all the Bank Accounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Bank Accounts in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<BankAccount> getAllBankAccounts(BankAccountCriteria criteria) {
        LOG.debug("REST request to get BankAccounts by criteria: {}", criteria);
        return bankAccountService.findByCriteria(criteria);
    }

    /**
     * {@code GET  /bank-accounts/count} : count all the bankAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countBankAccounts(BankAccountCriteria criteria) {
        LOG.debug("REST request to count BankAccounts by criteria: {}", criteria);
        return bankAccountService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /bank-accounts/:id} : get the "id" bankAccount.
     *
     * @param id the id of the bankAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<BankAccount>> getBankAccount(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BankAccount : {}", id);
        Mono<BankAccount> bankAccount = bankAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankAccount);
    }

    /**
     * {@code DELETE  /bank-accounts/:id} : delete the "id" bankAccount.
     *
     * @param id the id of the bankAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteBankAccount(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BankAccount : {}", id);
        return bankAccountService
            .delete(id)

            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    /**
     * {@code SEARCH  /bank-accounts/_search?query=:query} : search for the bankAccount corresponding
     * to the query.
     *
     * @param query the query of the bankAccount search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<BankAccount>> searchBankAccounts(@RequestParam("query") String query) {
        LOG.debug("REST request to search BankAccounts for query {}", query);
        try {
            return bankAccountService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
