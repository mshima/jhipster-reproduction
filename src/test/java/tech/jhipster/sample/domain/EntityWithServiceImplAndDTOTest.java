package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.EntityWithServiceImplAndDTOTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class EntityWithServiceImplAndDTOTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityWithServiceImplAndDTO.class);
        EntityWithServiceImplAndDTO entityWithServiceImplAndDTO1 = getEntityWithServiceImplAndDTOSample1();
        EntityWithServiceImplAndDTO entityWithServiceImplAndDTO2 = new EntityWithServiceImplAndDTO();
        assertThat(entityWithServiceImplAndDTO1).isNotEqualTo(entityWithServiceImplAndDTO2);

        entityWithServiceImplAndDTO2.setId(entityWithServiceImplAndDTO1.getId());
        assertThat(entityWithServiceImplAndDTO1).isEqualTo(entityWithServiceImplAndDTO2);

        entityWithServiceImplAndDTO2 = getEntityWithServiceImplAndDTOSample2();
        assertThat(entityWithServiceImplAndDTO1).isNotEqualTo(entityWithServiceImplAndDTO2);
    }
}
