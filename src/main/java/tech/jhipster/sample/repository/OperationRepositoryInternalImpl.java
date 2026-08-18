package tech.jhipster.sample.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
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
import tech.jhipster.sample.domain.Label;
import tech.jhipster.sample.domain.Operation;
import tech.jhipster.sample.repository.rowmapper.BankAccountRowMapper;
import tech.jhipster.sample.repository.rowmapper.OperationRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the Operation entity.
 */
@SuppressWarnings("unused")
class OperationRepositoryInternalImpl extends SimpleR2dbcRepository<Operation, Long> implements OperationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final BankAccountRowMapper bankaccountMapper;
    private final OperationRowMapper operationMapper;

    private static final Table entityTable = Table.aliased("operation", EntityManager.ENTITY_ALIAS);
    private static final Table bankAccountTable = Table.aliased("bank_account", "bankAccount");

    private static final EntityManager.LinkTable labelLink = new EntityManager.LinkTable(
        "rel_operation__label",
        "operation_id",
        "label_id"
    );

    public OperationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        BankAccountRowMapper bankaccountMapper,
        OperationRowMapper operationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Operation.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.bankaccountMapper = bankaccountMapper;
        this.operationMapper = operationMapper;
    }

    @Override
    public Flux<Operation> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Operation> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = OperationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(BankAccountSqlHelper.getColumns(bankAccountTable, "bankAccount"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(bankAccountTable)
            .on(Column.create("bank_account_id", entityTable))
            .equals(Column.create("id", bankAccountTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Operation.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Operation> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Operation> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Operation> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Operation> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Operation> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Operation process(Row row, RowMetadata metadata) {
        Operation entity = operationMapper.apply(row, "e");
        entity.setBankAccount(bankaccountMapper.apply(row, "bankAccount"));
        return entity;
    }

    @Override
    public <S extends Operation> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Operation> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager.updateLinkTable(labelLink, entity.getId(), entity.getLabels().stream().map(Label::getId)).then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(labelLink, entityId);
    }
}
