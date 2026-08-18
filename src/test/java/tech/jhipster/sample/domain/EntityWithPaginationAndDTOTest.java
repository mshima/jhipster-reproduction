package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.EntityWithPaginationAndDTOTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class EntityWithPaginationAndDTOTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityWithPaginationAndDTO.class);
        EntityWithPaginationAndDTO entityWithPaginationAndDTO1 = getEntityWithPaginationAndDTOSample1();
        EntityWithPaginationAndDTO entityWithPaginationAndDTO2 = new EntityWithPaginationAndDTO();
        assertThat(entityWithPaginationAndDTO1).isNotEqualTo(entityWithPaginationAndDTO2);

        entityWithPaginationAndDTO2.setId(entityWithPaginationAndDTO1.getId());
        assertThat(entityWithPaginationAndDTO1).isEqualTo(entityWithPaginationAndDTO2);

        entityWithPaginationAndDTO2 = getEntityWithPaginationAndDTOSample2();
        assertThat(entityWithPaginationAndDTO1).isNotEqualTo(entityWithPaginationAndDTO2);
    }
}
