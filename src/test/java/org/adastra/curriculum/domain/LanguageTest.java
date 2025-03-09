package org.adastra.curriculum.domain;

import static org.adastra.curriculum.domain.BiographyTestSamples.*;
import static org.adastra.curriculum.domain.LanguageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.adastra.curriculum.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LanguageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Language.class);
        Language language1 = getLanguageSample1();
        Language language2 = new Language();
        assertThat(language1).isNotEqualTo(language2);

        language2.setId(language1.getId());
        assertThat(language1).isEqualTo(language2);

        language2 = getLanguageSample2();
        assertThat(language1).isNotEqualTo(language2);
    }

    @Test
    void biographyTest() {
        Language language = getLanguageRandomSampleGenerator();
        Biography biographyBack = getBiographyRandomSampleGenerator();

        language.setBiography(biographyBack);
        assertThat(language.getBiography()).isEqualTo(biographyBack);

        language.biography(null);
        assertThat(language.getBiography()).isNull();
    }
}
