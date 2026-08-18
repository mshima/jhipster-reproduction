package tech.jhipster.sample.service.mapper;

import static tech.jhipster.sample.domain.EntityWithDTOAsserts.*;
import static tech.jhipster.sample.domain.EntityWithDTOTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityWithDTOMapperTest {

    private EntityWithDTOMapper entityWithDTOMapper;

    @BeforeEach
    void setUp() {
        entityWithDTOMapper = new EntityWithDTOMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEntityWithDTOSample1();
        var actual = entityWithDTOMapper.toEntity(entityWithDTOMapper.toDto(expected));
        assertEntityWithDTOAllPropertiesEquals(expected, actual);
    }
}
