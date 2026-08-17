package com.mycompany.myapp.app.child.domain;

import static com.mycompany.myapp.app.child.domain.CustomPackageChildTestSamples.*;
import static com.mycompany.myapp.app.custom.domain.CustomPackageParentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.app.custom.domain.CustomPackageParent;
import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomPackageChildTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomPackageChild.class);
        CustomPackageChild customPackageChild1 = getCustomPackageChildSample1();
        CustomPackageChild customPackageChild2 = new CustomPackageChild();
        assertThat(customPackageChild1).isNotEqualTo(customPackageChild2);

        customPackageChild2.setId(customPackageChild1.getId());
        assertThat(customPackageChild1).isEqualTo(customPackageChild2);

        customPackageChild2 = getCustomPackageChildSample2();
        assertThat(customPackageChild1).isNotEqualTo(customPackageChild2);
    }

    @Test
    void customPackageParentTest() {
        CustomPackageChild customPackageChild = getCustomPackageChildRandomSampleGenerator();
        CustomPackageParent customPackageParentBack = getCustomPackageParentRandomSampleGenerator();

        customPackageChild.setCustomPackageParent(customPackageParentBack);
        assertThat(customPackageChild.getCustomPackageParent()).isEqualTo(customPackageParentBack);

        customPackageChild.customPackageParent(null);
        assertThat(customPackageChild.getCustomPackageParent()).isNull();
    }
}
