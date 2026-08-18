package tech.jhipster.sample.service.mapper;

import static tech.jhipster.sample.domain.EntityWithServiceImplPaginationAndDTOAsserts.*;
import static tech.jhipster.sample.domain.EntityWithServiceImplPaginationAndDTOTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityWithServiceImplPaginationAndDTOMapperTest {

    private EntityWithServiceImplPaginationAndDTOMapper entityWithServiceImplPaginationAndDTOMapper;

    @BeforeEach
    void setUp() {
        entityWithServiceImplPaginationAndDTOMapper = new EntityWithServiceImplPaginationAndDTOMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEntityWithServiceImplPaginationAndDTOSample1();
        var actual = entityWithServiceImplPaginationAndDTOMapper.toEntity(entityWithServiceImplPaginationAndDTOMapper.toDto(expected));
        assertEntityWithServiceImplPaginationAndDTOAllPropertiesEquals(expected, actual);
    }
}
