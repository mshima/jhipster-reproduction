package tech.jhipster.sample.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
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
import tech.jhipster.sample.domain.FieldTestPaginationEntity;
import tech.jhipster.sample.domain.enumeration.EnumFieldClass;
import tech.jhipster.sample.domain.enumeration.EnumRequiredFieldClass;
import tech.jhipster.sample.repository.rowmapper.FieldTestPaginationEntityRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the FieldTestPaginationEntity entity.
 */
@SuppressWarnings("unused")
class FieldTestPaginationEntityRepositoryInternalImpl
    extends SimpleR2dbcRepository<FieldTestPaginationEntity, Long>
    implements FieldTestPaginationEntityRepositoryInternal
{

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FieldTestPaginationEntityRowMapper fieldtestpaginationentityMapper;

    private static final Table entityTable = Table.aliased("field_test_pagination_entity", EntityManager.ENTITY_ALIAS);

    public FieldTestPaginationEntityRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FieldTestPaginationEntityRowMapper fieldtestpaginationentityMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(FieldTestPaginationEntity.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.fieldtestpaginationentityMapper = fieldtestpaginationentityMapper;
    }

    @Override
    public Flux<FieldTestPaginationEntity> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FieldTestPaginationEntity> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FieldTestPaginationEntitySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FieldTestPaginationEntity.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FieldTestPaginationEntity> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FieldTestPaginationEntity> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private FieldTestPaginationEntity process(Row row, RowMetadata metadata) {
        FieldTestPaginationEntity entity = fieldtestpaginationentityMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends FieldTestPaginationEntity> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
