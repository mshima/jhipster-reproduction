package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.FieldTestEntityTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class FieldTestEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldTestEntity.class);
        FieldTestEntity fieldTestEntity1 = getFieldTestEntitySample1();
        FieldTestEntity fieldTestEntity2 = new FieldTestEntity();
        assertThat(fieldTestEntity1).isNotEqualTo(fieldTestEntity2);

        fieldTestEntity2.setId(fieldTestEntity1.getId());
        assertThat(fieldTestEntity1).isEqualTo(fieldTestEntity2);

        fieldTestEntity2 = getFieldTestEntitySample2();
        assertThat(fieldTestEntity1).isNotEqualTo(fieldTestEntity2);
    }
}
