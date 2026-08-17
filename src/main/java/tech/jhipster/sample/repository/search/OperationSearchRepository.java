package tech.jhipster.sample.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import tech.jhipster.sample.domain.Operation;

/**
 * Spring Data Elasticsearch repository for the {@link Operation} entity.
 */
public interface OperationSearchRepository extends ReactiveElasticsearchRepository<Operation, Long>, OperationSearchRepositoryInternal {}

interface OperationSearchRepositoryInternal {
    Flux<Operation> search(String query, Pageable pageable);

    Flux<Operation> search(Query query);
}

class OperationSearchRepositoryInternalImpl implements OperationSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    OperationSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<Operation> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<Operation> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, Operation.class).map(SearchHit::getContent);
    }
}
