package com.mycompany.myapp.app.custom.domain;

import static com.mycompany.myapp.app.child.domain.CustomPackageChildTestSamples.*;
import static com.mycompany.myapp.app.custom.domain.CustomPackageParentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.app.child.domain.CustomPackageChild;
import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

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
