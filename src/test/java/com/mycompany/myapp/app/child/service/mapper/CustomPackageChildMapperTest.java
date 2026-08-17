package com.mycompany.myapp.app.child.service.mapper;

import static com.mycompany.myapp.app.child.domain.CustomPackageChildAsserts.*;
import static com.mycompany.myapp.app.child.domain.CustomPackageChildTestSamples.*;

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
