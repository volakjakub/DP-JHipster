package org.adastra.curriculum.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.adastra.curriculum.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BiographyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BiographyDTO.class);
        BiographyDTO biographyDTO1 = new BiographyDTO();
        biographyDTO1.setId(1L);
        BiographyDTO biographyDTO2 = new BiographyDTO();
        assertThat(biographyDTO1).isNotEqualTo(biographyDTO2);
        biographyDTO2.setId(biographyDTO1.getId());
        assertThat(biographyDTO1).isEqualTo(biographyDTO2);
        biographyDTO2.setId(2L);
        assertThat(biographyDTO1).isNotEqualTo(biographyDTO2);
        biographyDTO1.setId(null);
        assertThat(biographyDTO1).isNotEqualTo(biographyDTO2);
    }
}
