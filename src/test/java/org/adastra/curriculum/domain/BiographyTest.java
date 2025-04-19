package org.adastra.curriculum.domain;

import static org.adastra.curriculum.domain.BiographyTestSamples.*;
import static org.adastra.curriculum.domain.EducationTestSamples.*;
import static org.adastra.curriculum.domain.LanguageTestSamples.*;
import static org.adastra.curriculum.domain.ProjectTestSamples.*;
import static org.adastra.curriculum.domain.SkillTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
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

    @Test
    void educationsTest() {
        Biography biography = getBiographyRandomSampleGenerator();
        Education educationBack = getEducationRandomSampleGenerator();

        biography.addEducations(educationBack);
        assertThat(biography.getEducations()).containsOnly(educationBack);
        assertThat(educationBack.getBiography()).isEqualTo(biography);

        biography.removeEducations(educationBack);
        assertThat(biography.getEducations()).doesNotContain(educationBack);
        assertThat(educationBack.getBiography()).isNull();

        biography.educations(new HashSet<>(Set.of(educationBack)));
        assertThat(biography.getEducations()).containsOnly(educationBack);
        assertThat(educationBack.getBiography()).isEqualTo(biography);

        biography.setEducations(new HashSet<>());
        assertThat(biography.getEducations()).doesNotContain(educationBack);
        assertThat(educationBack.getBiography()).isNull();
    }

    @Test
    void languagesTest() {
        Biography biography = getBiographyRandomSampleGenerator();
        Language languageBack = getLanguageRandomSampleGenerator();

        biography.addLanguages(languageBack);
        assertThat(biography.getLanguages()).containsOnly(languageBack);
        assertThat(languageBack.getBiography()).isEqualTo(biography);

        biography.removeLanguages(languageBack);
        assertThat(biography.getLanguages()).doesNotContain(languageBack);
        assertThat(languageBack.getBiography()).isNull();

        biography.languages(new HashSet<>(Set.of(languageBack)));
        assertThat(biography.getLanguages()).containsOnly(languageBack);
        assertThat(languageBack.getBiography()).isEqualTo(biography);

        biography.setLanguages(new HashSet<>());
        assertThat(biography.getLanguages()).doesNotContain(languageBack);
        assertThat(languageBack.getBiography()).isNull();
    }

    @Test
    void skillsTest() {
        Biography biography = getBiographyRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        biography.addSkills(skillBack);
        assertThat(biography.getSkills()).containsOnly(skillBack);
        assertThat(skillBack.getBiography()).isEqualTo(biography);

        biography.removeSkills(skillBack);
        assertThat(biography.getSkills()).doesNotContain(skillBack);
        assertThat(skillBack.getBiography()).isNull();

        biography.skills(new HashSet<>(Set.of(skillBack)));
        assertThat(biography.getSkills()).containsOnly(skillBack);
        assertThat(skillBack.getBiography()).isEqualTo(biography);

        biography.setSkills(new HashSet<>());
        assertThat(biography.getSkills()).doesNotContain(skillBack);
        assertThat(skillBack.getBiography()).isNull();
    }

    @Test
    void projectsTest() {
        Biography biography = getBiographyRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        biography.addProjects(projectBack);
        assertThat(biography.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getBiography()).isEqualTo(biography);

        biography.removeProjects(projectBack);
        assertThat(biography.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getBiography()).isNull();

        biography.projects(new HashSet<>(Set.of(projectBack)));
        assertThat(biography.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getBiography()).isEqualTo(biography);

        biography.setProjects(new HashSet<>());
        assertThat(biography.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getBiography()).isNull();
    }
}
