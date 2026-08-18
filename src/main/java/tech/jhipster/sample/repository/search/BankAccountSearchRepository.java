package tech.jhipster.sample.repository.search;

import static org.springframework.data.elasticsearch.client.elc.QueryBuilders.queryStringQuery;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import tech.jhipster.sample.domain.BankAccount;
import tech.jhipster.sample.repository.BankAccountRepository;

/**
 * Spring Data Elasticsearch repository for the {@link BankAccount} entity.
 */
public interface BankAccountSearchRepository
    extends ReactiveElasticsearchRepository<BankAccount, Long>, BankAccountSearchRepositoryInternal {}

interface BankAccountSearchRepositoryInternal {
    Flux<BankAccount> search(String query);

    Flux<BankAccount> search(Query query);
}

class BankAccountSearchRepositoryInternalImpl implements BankAccountSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    BankAccountSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<BankAccount> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Flux<BankAccount> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, BankAccount.class).map(SearchHit::getContent);
    }
}
