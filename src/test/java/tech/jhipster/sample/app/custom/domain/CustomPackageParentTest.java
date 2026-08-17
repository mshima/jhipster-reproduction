package tech.jhipster.sample.app.custom.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.sample.app.child.domain.CustomPackageChildTestSamples.*;
import static tech.jhipster.sample.app.custom.domain.CustomPackageParentTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tech.jhipster.sample.app.child.domain.CustomPackageChild;
import tech.jhipster.sample.web.rest.TestUtil;

class CustomPackageParentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomPackageParent.class);
        CustomPackageParent customPackageParent1 = getCustomPackageParentSample1();
        CustomPackageParent customPackageParent2 = new CustomPackageParent();
        assertThat(customPackageParent1).isNotEqualTo(customPackageParent2);

        customPackageParent2.setId(customPackageParent1.getId());
        assertThat(customPackageParent1).isEqualTo(customPackageParent2);

        customPackageParent2 = getCustomPackageParentSample2();
        assertThat(customPackageParent1).isNotEqualTo(customPackageParent2);
    }

    @Test
    void customPackageChildTest() {
        CustomPackageParent customPackageParent = getCustomPackageParentRandomSampleGenerator();
        CustomPackageChild customPackageChildBack = getCustomPackageChildRandomSampleGenerator();

        customPackageParent.addCustomPackageChild(customPackageChildBack);
        assertThat(customPackageParent.getCustomPackageChildren()).containsOnly(customPackageChildBack);
        assertThat(customPackageChildBack.getCustomPackageParent()).isEqualTo(customPackageParent);

        customPackageParent.removeCustomPackageChild(customPackageChildBack);
        assertThat(customPackageParent.getCustomPackageChildren()).doesNotContain(customPackageChildBack);
        assertThat(customPackageChildBack.getCustomPackageParent()).isNull();

        customPackageParent.customPackageChildren(new HashSet<>(Set.of(customPackageChildBack)));
        assertThat(customPackageParent.getCustomPackageChildren()).containsOnly(customPackageChildBack);
        assertThat(customPackageChildBack.getCustomPackageParent()).isEqualTo(customPackageParent);

        customPackageParent.setCustomPackageChildren(new HashSet<>());
        assertThat(customPackageParent.getCustomPackageChildren()).doesNotContain(customPackageChildBack);
        assertThat(customPackageChildBack.getCustomPackageParent()).isNull();
    }
}
