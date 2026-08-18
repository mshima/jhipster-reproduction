package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.FieldTestInfiniteScrollEntityTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class FieldTestInfiniteScrollEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldTestInfiniteScrollEntity.class);
        FieldTestInfiniteScrollEntity fieldTestInfiniteScrollEntity1 = getFieldTestInfiniteScrollEntitySample1();
        FieldTestInfiniteScrollEntity fieldTestInfiniteScrollEntity2 = new FieldTestInfiniteScrollEntity();
        assertThat(fieldTestInfiniteScrollEntity1).isNotEqualTo(fieldTestInfiniteScrollEntity2);

        fieldTestInfiniteScrollEntity2.setId(fieldTestInfiniteScrollEntity1.getId());
        assertThat(fieldTestInfiniteScrollEntity1).isEqualTo(fieldTestInfiniteScrollEntity2);

        fieldTestInfiniteScrollEntity2 = getFieldTestInfiniteScrollEntitySample2();
        assertThat(fieldTestInfiniteScrollEntity1).isNotEqualTo(fieldTestInfiniteScrollEntity2);
    }
}
