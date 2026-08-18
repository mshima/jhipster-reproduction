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
import tech.jhipster.sample.domain.FieldTestServiceClassAndJpaFilteringEntity;
import tech.jhipster.sample.domain.criteria.FieldTestServiceClassAndJpaFilteringEntityCriteria;
import tech.jhipster.sample.domain.enumeration.EnumFieldClass;
import tech.jhipster.sample.domain.enumeration.EnumRequiredFieldClass;
import tech.jhipster.sample.repository.rowmapper.ColumnConverter;
import tech.jhipster.sample.repository.rowmapper.FieldTestServiceClassAndJpaFilteringEntityRowMapper;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the FieldTestServiceClassAndJpaFilteringEntity entity.
 */
@SuppressWarnings("unused")
class FieldTestServiceClassAndJpaFilteringEntityRepositoryInternalImpl
    extends SimpleR2dbcRepository<FieldTestServiceClassAndJpaFilteringEntity, Long>
    implements FieldTestServiceClassAndJpaFilteringEntityRepositoryInternal
{

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FieldTestServiceClassAndJpaFilteringEntityRowMapper fieldtestserviceclassandjpafilteringentityMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("field_test_service_class_and_jpa_filtering_entity", EntityManager.ENTITY_ALIAS);

    public FieldTestServiceClassAndJpaFilteringEntityRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FieldTestServiceClassAndJpaFilteringEntityRowMapper fieldtestserviceclassandjpafilteringentityMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(FieldTestServiceClassAndJpaFilteringEntity.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.fieldtestserviceclassandjpafilteringentityMapper = fieldtestserviceclassandjpafilteringentityMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<FieldTestServiceClassAndJpaFilteringEntity> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FieldTestServiceClassAndJpaFilteringEntity> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FieldTestServiceClassAndJpaFilteringEntitySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FieldTestServiceClassAndJpaFilteringEntity.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FieldTestServiceClassAndJpaFilteringEntity> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FieldTestServiceClassAndJpaFilteringEntity> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private FieldTestServiceClassAndJpaFilteringEntity process(Row row, RowMetadata metadata) {
        FieldTestServiceClassAndJpaFilteringEntity entity = fieldtestserviceclassandjpafilteringentityMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends FieldTestServiceClassAndJpaFilteringEntity> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<FieldTestServiceClassAndJpaFilteringEntity> findByCriteria(
        FieldTestServiceClassAndJpaFilteringEntityCriteria fieldTestServiceClassAndJpaFilteringEntityCriteria,
        Pageable page
    ) {
        return createQuery(page, buildConditions(fieldTestServiceClassAndJpaFilteringEntityCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(FieldTestServiceClassAndJpaFilteringEntityCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(FieldTestServiceClassAndJpaFilteringEntityCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getStringBob() != null) {
                builder.buildFilterConditionForField(criteria.getStringBob(), entityTable.column("string_bob"));
            }
            if (criteria.getStringRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getStringRequiredBob(), entityTable.column("string_required_bob"));
            }
            if (criteria.getStringMinlengthBob() != null) {
                builder.buildFilterConditionForField(criteria.getStringMinlengthBob(), entityTable.column("string_minlength_bob"));
            }
            if (criteria.getStringMaxlengthBob() != null) {
                builder.buildFilterConditionForField(criteria.getStringMaxlengthBob(), entityTable.column("string_maxlength_bob"));
            }
            if (criteria.getStringPatternBob() != null) {
                builder.buildFilterConditionForField(criteria.getStringPatternBob(), entityTable.column("string_pattern_bob"));
            }
            if (criteria.getIntegerBob() != null) {
                builder.buildFilterConditionForField(criteria.getIntegerBob(), entityTable.column("integer_bob"));
            }
            if (criteria.getIntegerRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getIntegerRequiredBob(), entityTable.column("integer_required_bob"));
            }
            if (criteria.getIntegerMinBob() != null) {
                builder.buildFilterConditionForField(criteria.getIntegerMinBob(), entityTable.column("integer_min_bob"));
            }
            if (criteria.getIntegerMaxBob() != null) {
                builder.buildFilterConditionForField(criteria.getIntegerMaxBob(), entityTable.column("integer_max_bob"));
            }
            if (criteria.getLongBob() != null) {
                builder.buildFilterConditionForField(criteria.getLongBob(), entityTable.column("long_bob"));
            }
            if (criteria.getLongRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getLongRequiredBob(), entityTable.column("long_required_bob"));
            }
            if (criteria.getLongMinBob() != null) {
                builder.buildFilterConditionForField(criteria.getLongMinBob(), entityTable.column("long_min_bob"));
            }
            if (criteria.getLongMaxBob() != null) {
                builder.buildFilterConditionForField(criteria.getLongMaxBob(), entityTable.column("long_max_bob"));
            }
            if (criteria.getFloatBob() != null) {
                builder.buildFilterConditionForField(criteria.getFloatBob(), entityTable.column("float_bob"));
            }
            if (criteria.getFloatRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getFloatRequiredBob(), entityTable.column("float_required_bob"));
            }
            if (criteria.getFloatMinBob() != null) {
                builder.buildFilterConditionForField(criteria.getFloatMinBob(), entityTable.column("float_min_bob"));
            }
            if (criteria.getFloatMaxBob() != null) {
                builder.buildFilterConditionForField(criteria.getFloatMaxBob(), entityTable.column("float_max_bob"));
            }
            if (criteria.getDoubleRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getDoubleRequiredBob(), entityTable.column("double_required_bob"));
            }
            if (criteria.getDoubleMinBob() != null) {
                builder.buildFilterConditionForField(criteria.getDoubleMinBob(), entityTable.column("double_min_bob"));
            }
            if (criteria.getDoubleMaxBob() != null) {
                builder.buildFilterConditionForField(criteria.getDoubleMaxBob(), entityTable.column("double_max_bob"));
            }
            if (criteria.getBigDecimalRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getBigDecimalRequiredBob(), entityTable.column("big_decimal_required_bob"));
            }
            if (criteria.getBigDecimalMinBob() != null) {
                builder.buildFilterConditionForField(criteria.getBigDecimalMinBob(), entityTable.column("big_decimal_min_bob"));
            }
            if (criteria.getBigDecimalMaxBob() != null) {
                builder.buildFilterConditionForField(criteria.getBigDecimalMaxBob(), entityTable.column("big_decimal_max_bob"));
            }
            if (criteria.getLocalDateBob() != null) {
                builder.buildFilterConditionForField(criteria.getLocalDateBob(), entityTable.column("local_date_bob"));
            }
            if (criteria.getLocalDateRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getLocalDateRequiredBob(), entityTable.column("local_date_required_bob"));
            }
            if (criteria.getInstantBob() != null) {
                builder.buildFilterConditionForField(criteria.getInstantBob(), entityTable.column("instant_bob"));
            }
            if (criteria.getInstanteRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getInstanteRequiredBob(), entityTable.column("instante_required_bob"));
            }
            if (criteria.getZonedDateTimeBob() != null) {
                builder.buildFilterConditionForField(criteria.getZonedDateTimeBob(), entityTable.column("zoned_date_time_bob"));
            }
            if (criteria.getZonedDateTimeRequiredBob() != null) {
                builder.buildFilterConditionForField(
                    criteria.getZonedDateTimeRequiredBob(),
                    entityTable.column("zoned_date_time_required_bob")
                );
            }
            if (criteria.getLocalTimeBob() != null) {
                builder.buildFilterConditionForField(criteria.getLocalTimeBob(), entityTable.column("local_time_bob"));
            }
            if (criteria.getLocalTimeRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getLocalTimeRequiredBob(), entityTable.column("local_time_required_bob"));
            }
            if (criteria.getDurationBob() != null) {
                builder.buildFilterConditionForField(criteria.getDurationBob(), entityTable.column("duration_bob"));
            }
            if (criteria.getDurationRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getDurationRequiredBob(), entityTable.column("duration_required_bob"));
            }
            if (criteria.getBooleanBob() != null) {
                builder.buildFilterConditionForField(criteria.getBooleanBob(), entityTable.column("boolean_bob"));
            }
            if (criteria.getBooleanRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getBooleanRequiredBob(), entityTable.column("boolean_required_bob"));
            }
            if (criteria.getEnumBob() != null) {
                builder.buildFilterConditionForField(criteria.getEnumBob(), entityTable.column("enum_bob"));
            }
            if (criteria.getEnumRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getEnumRequiredBob(), entityTable.column("enum_required_bob"));
            }
            if (criteria.getUuidBob() != null) {
                builder.buildFilterConditionForField(criteria.getUuidBob(), entityTable.column("uuid_bob"));
            }
            if (criteria.getUuidRequiredBob() != null) {
                builder.buildFilterConditionForField(criteria.getUuidRequiredBob(), entityTable.column("uuid_required_bob"));
            }
        }
        return builder.buildConditions();
    }
}
