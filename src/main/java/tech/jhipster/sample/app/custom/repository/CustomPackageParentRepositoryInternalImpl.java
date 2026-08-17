package tech.jhipster.sample.app.custom.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.app.custom.domain.CustomPackageParent;
import tech.jhipster.sample.app.custom.repository.rowmapper.CustomPackageParentRowMapper;
import tech.jhipster.sample.repository.EntityManager;

/**
 * Spring Data R2DBC custom repository implementation for the CustomPackageParent entity.
 */
@SuppressWarnings("unused")
class CustomPackageParentRepositoryInternalImpl
    extends SimpleR2dbcRepository<CustomPackageParent, Long>
    implements CustomPackageParentRepositoryInternal
{

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CustomPackageParentRowMapper custompackageparentMapper;

    private static final Table entityTable = Table.aliased("custom_package_parent", EntityManager.ENTITY_ALIAS);

    public CustomPackageParentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CustomPackageParentRowMapper custompackageparentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(CustomPackageParent.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.custompackageparentMapper = custompackageparentMapper;
    }

    @Override
    public Flux<CustomPackageParent> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<CustomPackageParent> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CustomPackageParentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, CustomPackageParent.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CustomPackageParent> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<CustomPackageParent> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private CustomPackageParent process(Row row, RowMetadata metadata) {
        CustomPackageParent entity = custompackageparentMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends CustomPackageParent> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
