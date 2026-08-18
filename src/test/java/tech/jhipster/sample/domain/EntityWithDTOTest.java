package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.EntityWithDTOTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class EntityWithDTOTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityWithDTO.class);
        EntityWithDTO entityWithDTO1 = getEntityWithDTOSample1();
        EntityWithDTO entityWithDTO2 = new EntityWithDTO();
        assertThat(entityWithDTO1).isNotEqualTo(entityWithDTO2);

        entityWithDTO2.setId(entityWithDTO1.getId());
        assertThat(entityWithDTO1).isEqualTo(entityWithDTO2);

        entityWithDTO2 = getEntityWithDTOSample2();
        assertThat(entityWithDTO1).isNotEqualTo(entityWithDTO2);
    }
}
