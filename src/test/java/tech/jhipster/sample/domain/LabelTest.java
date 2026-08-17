package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.LabelTestSamples.*;
import static tech.jhipster.sample.domain.OperationTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class LabelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Label.class);
        Label label1 = getLabelSample1();
        Label label2 = new Label();
        assertThat(label1).isNotEqualTo(label2);

        label2.setId(label1.getId());
        assertThat(label1).isEqualTo(label2);

        label2 = getLabelSample2();
        assertThat(label1).isNotEqualTo(label2);
    }

    @Test
    void operationTest() {
        Label label = getLabelRandomSampleGenerator();
        Operation operationBack = getOperationRandomSampleGenerator();

        label.addOperation(operationBack);
        assertThat(label.getOperations()).containsOnly(operationBack);
        assertThat(operationBack.getLabels()).containsOnly(label);

        label.removeOperation(operationBack);
        assertThat(label.getOperations()).doesNotContain(operationBack);
        assertThat(operationBack.getLabels()).doesNotContain(label);

        label.operations(new HashSet<>(Set.of(operationBack)));
        assertThat(label.getOperations()).containsOnly(operationBack);
        assertThat(operationBack.getLabels()).containsOnly(label);

        label.setOperations(new HashSet<>());
        assertThat(label.getOperations()).doesNotContain(operationBack);
        assertThat(operationBack.getLabels()).doesNotContain(label);
    }
}
