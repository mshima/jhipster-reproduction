package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.EntityWithServiceImplAndPaginationTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class EntityWithServiceImplAndPaginationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityWithServiceImplAndPagination.class);
        EntityWithServiceImplAndPagination entityWithServiceImplAndPagination1 = getEntityWithServiceImplAndPaginationSample1();
        EntityWithServiceImplAndPagination entityWithServiceImplAndPagination2 = new EntityWithServiceImplAndPagination();
        assertThat(entityWithServiceImplAndPagination1).isNotEqualTo(entityWithServiceImplAndPagination2);

        entityWithServiceImplAndPagination2.setId(entityWithServiceImplAndPagination1.getId());
        assertThat(entityWithServiceImplAndPagination1).isEqualTo(entityWithServiceImplAndPagination2);

        entityWithServiceImplAndPagination2 = getEntityWithServiceImplAndPaginationSample2();
        assertThat(entityWithServiceImplAndPagination1).isNotEqualTo(entityWithServiceImplAndPagination2);
    }
}
