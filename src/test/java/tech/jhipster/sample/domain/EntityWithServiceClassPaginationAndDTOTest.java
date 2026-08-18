package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.EntityWithServiceClassPaginationAndDTOTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class EntityWithServiceClassPaginationAndDTOTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityWithServiceClassPaginationAndDTO.class);
        EntityWithServiceClassPaginationAndDTO entityWithServiceClassPaginationAndDTO1 = getEntityWithServiceClassPaginationAndDTOSample1();
        EntityWithServiceClassPaginationAndDTO entityWithServiceClassPaginationAndDTO2 = new EntityWithServiceClassPaginationAndDTO();
        assertThat(entityWithServiceClassPaginationAndDTO1).isNotEqualTo(entityWithServiceClassPaginationAndDTO2);

        entityWithServiceClassPaginationAndDTO2.setId(entityWithServiceClassPaginationAndDTO1.getId());
        assertThat(entityWithServiceClassPaginationAndDTO1).isEqualTo(entityWithServiceClassPaginationAndDTO2);

        entityWithServiceClassPaginationAndDTO2 = getEntityWithServiceClassPaginationAndDTOSample2();
        assertThat(entityWithServiceClassPaginationAndDTO1).isNotEqualTo(entityWithServiceClassPaginationAndDTO2);
    }
}
