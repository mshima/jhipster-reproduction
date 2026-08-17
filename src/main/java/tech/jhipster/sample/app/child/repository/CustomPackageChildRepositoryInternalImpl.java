package tech.jhipster.sample.app.child.repository;

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
import tech.jhipster.sample.app.child.domain.CustomPackageChild;
import tech.jhipster.sample.app.child.repository.rowmapper.CustomPackageChildRowMapper;
import tech.jhipster.sample.app.custom.repository.CustomPackageParentSqlHelper;
import tech.jhipster.sample.app.custom.repository.rowmapper.CustomPackageParentRowMapper;
import tech.jhipster.sample.repository.EntityManager;
import tech.jhipster.sample.repository.UserSqlHelper;
import tech.jhipster.sample.repository.rowmapper.UserRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the CustomPackageChild entity.
 */
@SuppressWarnings("unused")
class CustomPackageChildRepositoryInternalImpl
    extends SimpleR2dbcRepository<CustomPackageChild, Long>
    implements CustomPackageChildRepositoryInternal
{

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final CustomPackageParentRowMapper custompackageparentMapper;
    private final CustomPackageChildRowMapper custompackagechildMapper;

    private static final Table entityTable = Table.aliased("custom_package_child", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("jhi_user", "e_user");
    private static final Table customPackageParentTable = Table.aliased("custom_package_parent", "customPackageParent");

    public CustomPackageChildRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        CustomPackageParentRowMapper custompackageparentMapper,
        CustomPackageChildRowMapper custompackagechildMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(CustomPackageChild.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.custompackageparentMapper = custompackageparentMapper;
        this.custompackagechildMapper = custompackagechildMapper;
    }

    @Override
    public Flux<CustomPackageChild> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<CustomPackageChild> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CustomPackageChildSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        columns.addAll(CustomPackageParentSqlHelper.getColumns(customPackageParentTable, "customPackageParent"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable))
            .leftOuterJoin(customPackageParentTable)
            .on(Column.create("custom_package_parent_id", entityTable))
            .equals(Column.create("id", customPackageParentTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, CustomPackageChild.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CustomPackageChild> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<CustomPackageChild> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private CustomPackageChild process(Row row, RowMetadata metadata) {
        CustomPackageChild entity = custompackagechildMapper.apply(row, "e");
        entity.setUser(userMapper.apply(row, "user"));
        entity.setCustomPackageParent(custompackageparentMapper.apply(row, "customPackageParent"));
        return entity;
    }

    @Override
    public <S extends CustomPackageChild> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
