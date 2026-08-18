package tech.jhipster.sample.service.mapper;

import static tech.jhipster.sample.domain.MapsIdUserProfileWithDTOAsserts.*;
import static tech.jhipster.sample.domain.MapsIdUserProfileWithDTOTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapsIdUserProfileWithDTOMapperTest {

    private MapsIdUserProfileWithDTOMapper mapsIdUserProfileWithDTOMapper;

    @BeforeEach
    void setUp() {
        mapsIdUserProfileWithDTOMapper = new MapsIdUserProfileWithDTOMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMapsIdUserProfileWithDTOSample1();
        var actual = mapsIdUserProfileWithDTOMapper.toEntity(mapsIdUserProfileWithDTOMapper.toDto(expected));
        assertMapsIdUserProfileWithDTOAllPropertiesEquals(expected, actual);
    }
}
