package com.mycompany.myapp.app.child.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomPackageChildDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomPackageChildDTO.class);
        CustomPackageChildDTO customPackageChildDTO1 = new CustomPackageChildDTO();
        customPackageChildDTO1.setId(1L);
        CustomPackageChildDTO customPackageChildDTO2 = new CustomPackageChildDTO();
        assertThat(customPackageChildDTO1).isNotEqualTo(customPackageChildDTO2);
        customPackageChildDTO2.setId(customPackageChildDTO1.getId());
        assertThat(customPackageChildDTO1).isEqualTo(customPackageChildDTO2);
        customPackageChildDTO2.setId(2L);
        assertThat(customPackageChildDTO1).isNotEqualTo(customPackageChildDTO2);
        customPackageChildDTO1.setId(null);
        assertThat(customPackageChildDTO1).isNotEqualTo(customPackageChildDTO2);
    }
}
