package com.mycompany.myapp.app.custom.service.mapper;

import static com.mycompany.myapp.app.custom.domain.CustomPackageParentAsserts.*;
import static com.mycompany.myapp.app.custom.domain.CustomPackageParentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomPackageParentMapperTest {

    private CustomPackageParentMapper customPackageParentMapper;

    @BeforeEach
    void setUp() {
        customPackageParentMapper = new CustomPackageParentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomPackageParentSample1();
        var actual = customPackageParentMapper.toEntity(customPackageParentMapper.toDto(expected));
        assertCustomPackageParentAllPropertiesEquals(expected, actual);
    }
}
