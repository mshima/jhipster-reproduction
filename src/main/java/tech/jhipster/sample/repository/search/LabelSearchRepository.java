package tech.jhipster.sample.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import tech.jhipster.sample.domain.Label;

/**
 * Spring Data Elasticsearch repository for the {@link Label} entity.
 */
public interface LabelSearchRepository extends ReactiveElasticsearchRepository<Label, Long>, LabelSearchRepositoryInternal {}

interface LabelSearchRepositoryInternal {
    Flux<Label> search(String query, Pageable pageable);

    Flux<Label> search(Query query);
}

class LabelSearchRepositoryInternalImpl implements LabelSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    LabelSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<Label> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<Label> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, Label.class).map(SearchHit::getContent);
    }
}
