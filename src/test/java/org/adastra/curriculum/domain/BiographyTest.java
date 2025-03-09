package org.adastra.curriculum.domain;

import static org.adastra.curriculum.domain.BiographyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.adastra.curriculum.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BiographyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Biography.class);
        Biography biography1 = getBiographySample1();
        Biography biography2 = new Biography();
        assertThat(biography1).isNotEqualTo(biography2);

        biography2.setId(biography1.getId());
        assertThat(biography1).isEqualTo(biography2);

        biography2 = getBiographySample2();
        assertThat(biography1).isNotEqualTo(biography2);
    }
}
