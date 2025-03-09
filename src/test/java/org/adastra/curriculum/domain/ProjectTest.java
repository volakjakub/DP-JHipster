package org.adastra.curriculum.domain;

import static org.adastra.curriculum.domain.BiographyTestSamples.*;
import static org.adastra.curriculum.domain.ProjectTestSamples.*;
import static org.adastra.curriculum.domain.SkillTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.adastra.curriculum.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = getProjectSample1();
        Project project2 = new Project();
        assertThat(project1).isNotEqualTo(project2);

        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);

        project2 = getProjectSample2();
        assertThat(project1).isNotEqualTo(project2);
    }

    @Test
    void biographyTest() {
        Project project = getProjectRandomSampleGenerator();
        Biography biographyBack = getBiographyRandomSampleGenerator();

        project.setBiography(biographyBack);
        assertThat(project.getBiography()).isEqualTo(biographyBack);

        project.biography(null);
        assertThat(project.getBiography()).isNull();
    }

    @Test
    void skillsTest() {
        Project project = getProjectRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        project.addSkills(skillBack);
        assertThat(project.getSkills()).containsOnly(skillBack);

        project.removeSkills(skillBack);
        assertThat(project.getSkills()).doesNotContain(skillBack);

        project.skills(new HashSet<>(Set.of(skillBack)));
        assertThat(project.getSkills()).containsOnly(skillBack);

        project.setSkills(new HashSet<>());
        assertThat(project.getSkills()).doesNotContain(skillBack);
    }
}
