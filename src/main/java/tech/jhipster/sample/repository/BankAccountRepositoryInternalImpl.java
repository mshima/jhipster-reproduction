package tech.jhipster.sample.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.BankAccount;
import tech.jhipster.sample.domain.criteria.BankAccountCriteria;
import tech.jhipster.sample.domain.enumeration.BankAccountType;
import tech.jhipster.sample.repository.rowmapper.BankAccountRowMapper;
import tech.jhipster.sample.repository.rowmapper.ColumnConverter;
import tech.jhipster.sample.repository.rowmapper.UserRowMapper;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the BankAccount entity.
 */
@SuppressWarnings("unused")
class BankAccountRepositoryInternalImpl extends SimpleR2dbcRepository<BankAccount, Long> implements BankAccountRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final BankAccountRowMapper bankaccountMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("bank_account", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("jhi_user", "e_user");

    public BankAccountRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        BankAccountRowMapper bankaccountMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(BankAccount.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.bankaccountMapper = bankaccountMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<BankAccount> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<BankAccount> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = BankAccountSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, BankAccount.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<BankAccount> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<BankAccount> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<BankAccount> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<BankAccount> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<BankAccount> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private BankAccount process(Row row, RowMetadata metadata) {
        BankAccount entity = bankaccountMapper.apply(row, "e");
        entity.setUser(userMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends BankAccount> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<BankAccount> findByCriteria(BankAccountCriteria bankAccountCriteria, Pageable page) {
        return createQuery(page, buildConditions(bankAccountCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(BankAccountCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(BankAccountCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getGuid() != null) {
                builder.buildFilterConditionForField(criteria.getGuid(), entityTable.column("guid"));
            }
            if (criteria.getBankNumber() != null) {
                builder.buildFilterConditionForField(criteria.getBankNumber(), entityTable.column("bank_number"));
            }
            if (criteria.getAgencyNumber() != null) {
                builder.buildFilterConditionForField(criteria.getAgencyNumber(), entityTable.column("agency_number"));
            }
            if (criteria.getLastOperationDuration() != null) {
                builder.buildFilterConditionForField(criteria.getLastOperationDuration(), entityTable.column("last_operation_duration"));
            }
            if (criteria.getMeanOperationDuration() != null) {
                builder.buildFilterConditionForField(criteria.getMeanOperationDuration(), entityTable.column("mean_operation_duration"));
            }
            if (criteria.getMeanQueueDuration() != null) {
                builder.buildFilterConditionForField(criteria.getMeanQueueDuration(), entityTable.column("mean_queue_duration"));
            }
            if (criteria.getBalance() != null) {
                builder.buildFilterConditionForField(criteria.getBalance(), entityTable.column("balance"));
            }
            if (criteria.getOpeningDay() != null) {
                builder.buildFilterConditionForField(criteria.getOpeningDay(), entityTable.column("opening_day"));
            }
            if (criteria.getLastOperationDate() != null) {
                builder.buildFilterConditionForField(criteria.getLastOperationDate(), entityTable.column("last_operation_date"));
            }
            if (criteria.getActive() != null) {
                builder.buildFilterConditionForField(criteria.getActive(), entityTable.column("active"));
            }
            if (criteria.getAccountType() != null) {
                builder.buildFilterConditionForField(criteria.getAccountType(), entityTable.column("account_type"));
            }
            if (criteria.getUserId() != null) {
                builder.buildFilterConditionForField(criteria.getUserId(), userTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
