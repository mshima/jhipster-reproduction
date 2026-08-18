package tech.jhipster.sample.service.mapper;

import static tech.jhipster.sample.domain.EntityWithServiceClassPaginationAndDTOAsserts.*;
import static tech.jhipster.sample.domain.EntityWithServiceClassPaginationAndDTOTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityWithServiceClassPaginationAndDTOMapperTest {

    private EntityWithServiceClassPaginationAndDTOMapper entityWithServiceClassPaginationAndDTOMapper;

    @BeforeEach
    void setUp() {
        entityWithServiceClassPaginationAndDTOMapper = new EntityWithServiceClassPaginationAndDTOMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEntityWithServiceClassPaginationAndDTOSample1();
        var actual = entityWithServiceClassPaginationAndDTOMapper.toEntity(entityWithServiceClassPaginationAndDTOMapper.toDto(expected));
        assertEntityWithServiceClassPaginationAndDTOAllPropertiesEquals(expected, actual);
    }
}
