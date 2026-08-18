package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.FieldTestMapstructAndServiceClassEntityTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class FieldTestMapstructAndServiceClassEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldTestMapstructAndServiceClassEntity.class);
        FieldTestMapstructAndServiceClassEntity fieldTestMapstructAndServiceClassEntity1 =
            getFieldTestMapstructAndServiceClassEntitySample1();
        FieldTestMapstructAndServiceClassEntity fieldTestMapstructAndServiceClassEntity2 = new FieldTestMapstructAndServiceClassEntity();
        assertThat(fieldTestMapstructAndServiceClassEntity1).isNotEqualTo(fieldTestMapstructAndServiceClassEntity2);

        fieldTestMapstructAndServiceClassEntity2.setId(fieldTestMapstructAndServiceClassEntity1.getId());
        assertThat(fieldTestMapstructAndServiceClassEntity1).isEqualTo(fieldTestMapstructAndServiceClassEntity2);

        fieldTestMapstructAndServiceClassEntity2 = getFieldTestMapstructAndServiceClassEntitySample2();
        assertThat(fieldTestMapstructAndServiceClassEntity1).isNotEqualTo(fieldTestMapstructAndServiceClassEntity2);
    }
}
