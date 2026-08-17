package tech.jhipster.sample.app.child.service.mapper;

import static tech.jhipster.sample.app.child.domain.CustomPackageChildAsserts.*;
import static tech.jhipster.sample.app.child.domain.CustomPackageChildTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomPackageChildMapperTest {

    private CustomPackageChildMapper customPackageChildMapper;

    @BeforeEach
    void setUp() {
        customPackageChildMapper = new CustomPackageChildMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomPackageChildSample1();
        var actual = customPackageChildMapper.toEntity(customPackageChildMapper.toDto(expected));
        assertCustomPackageChildAllPropertiesEquals(expected, actual);
    }
}
