package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.BankAccountTestSamples.*;
import static tech.jhipster.sample.domain.LabelTestSamples.*;
import static tech.jhipster.sample.domain.OperationTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class OperationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operation.class);
        Operation operation1 = getOperationSample1();
        Operation operation2 = new Operation();
        assertThat(operation1).isNotEqualTo(operation2);

        operation2.setId(operation1.getId());
        assertThat(operation1).isEqualTo(operation2);

        operation2 = getOperationSample2();
        assertThat(operation1).isNotEqualTo(operation2);
    }

    @Test
    void bankAccountTest() {
        Operation operation = getOperationRandomSampleGenerator();
        BankAccount bankAccountBack = getBankAccountRandomSampleGenerator();

        operation.setBankAccount(bankAccountBack);
        assertThat(operation.getBankAccount()).isEqualTo(bankAccountBack);

        operation.bankAccount(null);
        assertThat(operation.getBankAccount()).isNull();
    }

    @Test
    void labelTest() {
        Operation operation = getOperationRandomSampleGenerator();
        Label labelBack = getLabelRandomSampleGenerator();

        operation.addLabel(labelBack);
        assertThat(operation.getLabels()).containsOnly(labelBack);

        operation.removeLabel(labelBack);
        assertThat(operation.getLabels()).doesNotContain(labelBack);

        operation.labels(new HashSet<>(Set.of(labelBack)));
        assertThat(operation.getLabels()).containsOnly(labelBack);

        operation.setLabels(new HashSet<>());
        assertThat(operation.getLabels()).doesNotContain(labelBack);
    }
}
