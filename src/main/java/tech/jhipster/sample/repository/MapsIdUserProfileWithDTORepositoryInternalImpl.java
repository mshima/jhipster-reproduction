package tech.jhipster.sample.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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
import tech.jhipster.sample.domain.MapsIdUserProfileWithDTO;
import tech.jhipster.sample.repository.rowmapper.MapsIdUserProfileWithDTORowMapper;
import tech.jhipster.sample.repository.rowmapper.UserRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the MapsIdUserProfileWithDTO entity.
 */
@SuppressWarnings("unused")
class MapsIdUserProfileWithDTORepositoryInternalImpl
    extends SimpleR2dbcRepository<MapsIdUserProfileWithDTO, Long>
    implements MapsIdUserProfileWithDTORepositoryInternal
{

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final MapsIdUserProfileWithDTORowMapper mapsiduserprofilewithdtoMapper;

    private static final Table entityTable = Table.aliased("maps_id_user_profile_with_dto", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("jhi_user", "e_user");

    public MapsIdUserProfileWithDTORepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        MapsIdUserProfileWithDTORowMapper mapsiduserprofilewithdtoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(MapsIdUserProfileWithDTO.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.mapsiduserprofilewithdtoMapper = mapsiduserprofilewithdtoMapper;
    }

    @Override
    public Flux<MapsIdUserProfileWithDTO> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<MapsIdUserProfileWithDTO> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MapsIdUserProfileWithDTOSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, MapsIdUserProfileWithDTO.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<MapsIdUserProfileWithDTO> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<MapsIdUserProfileWithDTO> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<MapsIdUserProfileWithDTO> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<MapsIdUserProfileWithDTO> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<MapsIdUserProfileWithDTO> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private MapsIdUserProfileWithDTO process(Row row, RowMetadata metadata) {
        MapsIdUserProfileWithDTO entity = mapsiduserprofilewithdtoMapper.apply(row, "e");
        entity.setUser(userMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends MapsIdUserProfileWithDTO> Mono<S> save(S entity) {
        if (entity.getId() == null && entity.getUser() != null) {
            entity.setId(entity.getUser().getId());
            return entityManager.insert(entity);
        }
        return super.save(entity);
    }
}
