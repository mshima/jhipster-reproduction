package tech.jhipster.sample.service.mapper;

import static tech.jhipster.sample.domain.EntityWithPaginationAndDTOAsserts.*;
import static tech.jhipster.sample.domain.EntityWithPaginationAndDTOTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityWithPaginationAndDTOMapperTest {

    private EntityWithPaginationAndDTOMapper entityWithPaginationAndDTOMapper;

    @BeforeEach
    void setUp() {
        entityWithPaginationAndDTOMapper = new EntityWithPaginationAndDTOMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEntityWithPaginationAndDTOSample1();
        var actual = entityWithPaginationAndDTOMapper.toEntity(entityWithPaginationAndDTOMapper.toDto(expected));
        assertEntityWithPaginationAndDTOAllPropertiesEquals(expected, actual);
    }
}
