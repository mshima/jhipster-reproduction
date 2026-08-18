package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.FieldTestEnumWithValueTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class FieldTestEnumWithValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldTestEnumWithValue.class);
        FieldTestEnumWithValue fieldTestEnumWithValue1 = getFieldTestEnumWithValueSample1();
        FieldTestEnumWithValue fieldTestEnumWithValue2 = new FieldTestEnumWithValue();
        assertThat(fieldTestEnumWithValue1).isNotEqualTo(fieldTestEnumWithValue2);

        fieldTestEnumWithValue2.setId(fieldTestEnumWithValue1.getId());
        assertThat(fieldTestEnumWithValue1).isEqualTo(fieldTestEnumWithValue2);

        fieldTestEnumWithValue2 = getFieldTestEnumWithValueSample2();
        assertThat(fieldTestEnumWithValue1).isNotEqualTo(fieldTestEnumWithValue2);
    }
}
