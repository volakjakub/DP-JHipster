package org.adastra.curriculum.domain;

import static org.adastra.curriculum.domain.BiographyTestSamples.*;
import static org.adastra.curriculum.domain.ProjectTestSamples.*;
import static org.adastra.curriculum.domain.SkillTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.adastra.curriculum.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Skill.class);
        Skill skill1 = getSkillSample1();
        Skill skill2 = new Skill();
        assertThat(skill1).isNotEqualTo(skill2);

        skill2.setId(skill1.getId());
        assertThat(skill1).isEqualTo(skill2);

        skill2 = getSkillSample2();
        assertThat(skill1).isNotEqualTo(skill2);
    }

    @Test
    void biographyTest() {
        Skill skill = getSkillRandomSampleGenerator();
        Biography biographyBack = getBiographyRandomSampleGenerator();

        skill.setBiography(biographyBack);
        assertThat(skill.getBiography()).isEqualTo(biographyBack);

        skill.biography(null);
        assertThat(skill.getBiography()).isNull();
    }

    @Test
    void projectsTest() {
        Skill skill = getSkillRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        skill.addProjects(projectBack);
        assertThat(skill.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getSkills()).containsOnly(skill);

        skill.removeProjects(projectBack);
        assertThat(skill.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getSkills()).doesNotContain(skill);

        skill.projects(new HashSet<>(Set.of(projectBack)));
        assertThat(skill.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getSkills()).containsOnly(skill);

        skill.setProjects(new HashSet<>());
        assertThat(skill.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getSkills()).doesNotContain(skill);
    }
}
