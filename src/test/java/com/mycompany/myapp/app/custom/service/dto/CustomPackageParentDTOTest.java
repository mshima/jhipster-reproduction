package com.mycompany.myapp.app.custom.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomPackageParentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomPackageParentDTO.class);
        CustomPackageParentDTO customPackageParentDTO1 = new CustomPackageParentDTO();
        customPackageParentDTO1.setId(1L);
        CustomPackageParentDTO customPackageParentDTO2 = new CustomPackageParentDTO();
        assertThat(customPackageParentDTO1).isNotEqualTo(customPackageParentDTO2);
        customPackageParentDTO2.setId(customPackageParentDTO1.getId());
        assertThat(customPackageParentDTO1).isEqualTo(customPackageParentDTO2);
        customPackageParentDTO2.setId(2L);
        assertThat(customPackageParentDTO1).isNotEqualTo(customPackageParentDTO2);
        customPackageParentDTO1.setId(null);
        assertThat(customPackageParentDTO1).isNotEqualTo(customPackageParentDTO2);
    }
}
