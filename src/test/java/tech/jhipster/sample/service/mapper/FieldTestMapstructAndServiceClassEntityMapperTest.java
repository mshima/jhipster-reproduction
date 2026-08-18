package tech.jhipster.sample.service.mapper;

import static tech.jhipster.sample.domain.FieldTestMapstructAndServiceClassEntityAsserts.*;
import static tech.jhipster.sample.domain.FieldTestMapstructAndServiceClassEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldTestMapstructAndServiceClassEntityMapperTest {

    private FieldTestMapstructAndServiceClassEntityMapper fieldTestMapstructAndServiceClassEntityMapper;

    @BeforeEach
    void setUp() {
        fieldTestMapstructAndServiceClassEntityMapper = new FieldTestMapstructAndServiceClassEntityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFieldTestMapstructAndServiceClassEntitySample1();
        var actual = fieldTestMapstructAndServiceClassEntityMapper.toEntity(fieldTestMapstructAndServiceClassEntityMapper.toDto(expected));
        assertFieldTestMapstructAndServiceClassEntityAllPropertiesEquals(expected, actual);
    }
}
