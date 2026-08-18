package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.EntityWithServiceClassAndPaginationTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class EntityWithServiceClassAndPaginationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityWithServiceClassAndPagination.class);
        EntityWithServiceClassAndPagination entityWithServiceClassAndPagination1 = getEntityWithServiceClassAndPaginationSample1();
        EntityWithServiceClassAndPagination entityWithServiceClassAndPagination2 = new EntityWithServiceClassAndPagination();
        assertThat(entityWithServiceClassAndPagination1).isNotEqualTo(entityWithServiceClassAndPagination2);

        entityWithServiceClassAndPagination2.setId(entityWithServiceClassAndPagination1.getId());
        assertThat(entityWithServiceClassAndPagination1).isEqualTo(entityWithServiceClassAndPagination2);

        entityWithServiceClassAndPagination2 = getEntityWithServiceClassAndPaginationSample2();
        assertThat(entityWithServiceClassAndPagination1).isNotEqualTo(entityWithServiceClassAndPagination2);
    }
}
