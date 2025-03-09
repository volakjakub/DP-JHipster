package org.adastra.curriculum.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SkillAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSkillAllPropertiesEquals(Skill expected, Skill actual) {
        assertSkillAutoGeneratedPropertiesEquals(expected, actual);
        assertSkillAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSkillAllUpdatablePropertiesEquals(Skill expected, Skill actual) {
        assertSkillUpdatableFieldsEquals(expected, actual);
        assertSkillUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSkillAutoGeneratedPropertiesEquals(Skill expected, Skill actual) {
        assertThat(actual)
            .as("Verify Skill auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSkillUpdatableFieldsEquals(Skill expected, Skill actual) {
        assertThat(actual)
            .as("Verify Skill relevant properties")
            .satisfies(a -> assertThat(a.getName()).as("check name").isEqualTo(expected.getName()))
            .satisfies(a -> assertThat(a.getExpertise()).as("check expertise").isEqualTo(expected.getExpertise()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSkillUpdatableRelationshipsEquals(Skill expected, Skill actual) {
        assertThat(actual)
            .as("Verify Skill relationships")
            .satisfies(a -> assertThat(a.getBiography()).as("check biography").isEqualTo(expected.getBiography()))
            .satisfies(a -> assertThat(a.getProjects()).as("check projects").isEqualTo(expected.getProjects()));
    }
}
