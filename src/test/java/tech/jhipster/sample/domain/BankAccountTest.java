package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.domain.BankAccountTestSamples.*;
import static tech.jhipster.sample.domain.OperationTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class BankAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankAccount.class);
        BankAccount bankAccount1 = getBankAccountSample1();
        BankAccount bankAccount2 = new BankAccount();
        assertThat(bankAccount1).isNotEqualTo(bankAccount2);

        bankAccount2.setId(bankAccount1.getId());
        assertThat(bankAccount1).isEqualTo(bankAccount2);

        bankAccount2 = getBankAccountSample2();
        assertThat(bankAccount1).isNotEqualTo(bankAccount2);
    }

    @Test
    void operationTest() {
        BankAccount bankAccount = getBankAccountRandomSampleGenerator();
        Operation operationBack = getOperationRandomSampleGenerator();

        bankAccount.addOperation(operationBack);
        assertThat(bankAccount.getOperations()).containsOnly(operationBack);
        assertThat(operationBack.getBankAccount()).isEqualTo(bankAccount);

        bankAccount.removeOperation(operationBack);
        assertThat(bankAccount.getOperations()).doesNotContain(operationBack);
        assertThat(operationBack.getBankAccount()).isNull();

        bankAccount.operations(new HashSet<>(Set.of(operationBack)));
        assertThat(bankAccount.getOperations()).containsOnly(operationBack);
        assertThat(operationBack.getBankAccount()).isEqualTo(bankAccount);

        bankAccount.setOperations(new HashSet<>());
        assertThat(bankAccount.getOperations()).doesNotContain(operationBack);
        assertThat(operationBack.getBankAccount()).isNull();
    }
}
