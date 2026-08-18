package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.FieldTestServiceImplEntityTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class FieldTestServiceImplEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldTestServiceImplEntity.class);
        FieldTestServiceImplEntity fieldTestServiceImplEntity1 = getFieldTestServiceImplEntitySample1();
        FieldTestServiceImplEntity fieldTestServiceImplEntity2 = new FieldTestServiceImplEntity();
        assertThat(fieldTestServiceImplEntity1).isNotEqualTo(fieldTestServiceImplEntity2);

        fieldTestServiceImplEntity2.setId(fieldTestServiceImplEntity1.getId());
        assertThat(fieldTestServiceImplEntity1).isEqualTo(fieldTestServiceImplEntity2);

        fieldTestServiceImplEntity2 = getFieldTestServiceImplEntitySample2();
        assertThat(fieldTestServiceImplEntity1).isNotEqualTo(fieldTestServiceImplEntity2);
    }
}
