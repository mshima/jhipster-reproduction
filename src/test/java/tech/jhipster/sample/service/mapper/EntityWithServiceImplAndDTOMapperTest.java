package tech.jhipster.sample.service.mapper;

import static tech.jhipster.sample.domain.EntityWithServiceImplAndDTOAsserts.*;
import static tech.jhipster.sample.domain.EntityWithServiceImplAndDTOTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityWithServiceImplAndDTOMapperTest {

    private EntityWithServiceImplAndDTOMapper entityWithServiceImplAndDTOMapper;

    @BeforeEach
    void setUp() {
        entityWithServiceImplAndDTOMapper = new EntityWithServiceImplAndDTOMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEntityWithServiceImplAndDTOSample1();
        var actual = entityWithServiceImplAndDTOMapper.toEntity(entityWithServiceImplAndDTOMapper.toDto(expected));
        assertEntityWithServiceImplAndDTOAllPropertiesEquals(expected, actual);
    }
}
