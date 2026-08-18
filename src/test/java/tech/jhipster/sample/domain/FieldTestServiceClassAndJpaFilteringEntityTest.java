package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.FieldTestServiceClassAndJpaFilteringEntityTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class FieldTestServiceClassAndJpaFilteringEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldTestServiceClassAndJpaFilteringEntity.class);
        FieldTestServiceClassAndJpaFilteringEntity fieldTestServiceClassAndJpaFilteringEntity1 =
            getFieldTestServiceClassAndJpaFilteringEntitySample1();
        FieldTestServiceClassAndJpaFilteringEntity fieldTestServiceClassAndJpaFilteringEntity2 =
            new FieldTestServiceClassAndJpaFilteringEntity();
        assertThat(fieldTestServiceClassAndJpaFilteringEntity1).isNotEqualTo(fieldTestServiceClassAndJpaFilteringEntity2);

        fieldTestServiceClassAndJpaFilteringEntity2.setId(fieldTestServiceClassAndJpaFilteringEntity1.getId());
        assertThat(fieldTestServiceClassAndJpaFilteringEntity1).isEqualTo(fieldTestServiceClassAndJpaFilteringEntity2);

        fieldTestServiceClassAndJpaFilteringEntity2 = getFieldTestServiceClassAndJpaFilteringEntitySample2();
        assertThat(fieldTestServiceClassAndJpaFilteringEntity1).isNotEqualTo(fieldTestServiceClassAndJpaFilteringEntity2);
    }
}
