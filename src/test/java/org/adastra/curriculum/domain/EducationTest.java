package org.adastra.curriculum.domain;

import static org.adastra.curriculum.domain.BiographyTestSamples.*;
import static org.adastra.curriculum.domain.EducationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.adastra.curriculum.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EducationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Education.class);
        Education education1 = getEducationSample1();
        Education education2 = new Education();
        assertThat(education1).isNotEqualTo(education2);

        education2.setId(education1.getId());
        assertThat(education1).isEqualTo(education2);

        education2 = getEducationSample2();
        assertThat(education1).isNotEqualTo(education2);
    }

    @Test
    void biographyTest() {
        Education education = getEducationRandomSampleGenerator();
        Biography biographyBack = getBiographyRandomSampleGenerator();

        education.setBiography(biographyBack);
        assertThat(education.getBiography()).isEqualTo(biographyBack);

        education.biography(null);
        assertThat(education.getBiography()).isNull();
    }
}
