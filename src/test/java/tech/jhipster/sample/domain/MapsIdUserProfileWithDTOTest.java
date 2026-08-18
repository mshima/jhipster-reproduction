package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.MapsIdUserProfileWithDTOTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class MapsIdUserProfileWithDTOTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MapsIdUserProfileWithDTO.class);
        MapsIdUserProfileWithDTO mapsIdUserProfileWithDTO1 = getMapsIdUserProfileWithDTOSample1();
        MapsIdUserProfileWithDTO mapsIdUserProfileWithDTO2 = new MapsIdUserProfileWithDTO();
        assertThat(mapsIdUserProfileWithDTO1).isNotEqualTo(mapsIdUserProfileWithDTO2);

        mapsIdUserProfileWithDTO2.setId(mapsIdUserProfileWithDTO1.getId());
        assertThat(mapsIdUserProfileWithDTO1).isEqualTo(mapsIdUserProfileWithDTO2);

        mapsIdUserProfileWithDTO2 = getMapsIdUserProfileWithDTOSample2();
        assertThat(mapsIdUserProfileWithDTO1).isNotEqualTo(mapsIdUserProfileWithDTO2);
    }
}
