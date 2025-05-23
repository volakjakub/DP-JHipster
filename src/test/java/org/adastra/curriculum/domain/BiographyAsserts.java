package org.adastra.curriculum.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class BiographyAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBiographyAllPropertiesEquals(Biography expected, Biography actual) {
        assertBiographyAutoGeneratedPropertiesEquals(expected, actual);
        assertBiographyAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBiographyAllUpdatablePropertiesEquals(Biography expected, Biography actual) {
        assertBiographyUpdatableFieldsEquals(expected, actual);
        assertBiographyUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBiographyAutoGeneratedPropertiesEquals(Biography expected, Biography actual) {
        assertThat(actual)
            .as("Verify Biography auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBiographyUpdatableFieldsEquals(Biography expected, Biography actual) {
        assertThat(actual)
            .as("Verify Biography relevant properties")
            .satisfies(a -> assertThat(a.getFirstName()).as("check firstName").isEqualTo(expected.getFirstName()))
            .satisfies(a -> assertThat(a.getLastName()).as("check lastName").isEqualTo(expected.getLastName()))
            .satisfies(a -> assertThat(a.getTitle()).as("check title").isEqualTo(expected.getTitle()))
            .satisfies(a -> assertThat(a.getPhone()).as("check phone").isEqualTo(expected.getPhone()))
            .satisfies(a -> assertThat(a.getEmail()).as("check email").isEqualTo(expected.getEmail()))
            .satisfies(a -> assertThat(a.getStreet()).as("check street").isEqualTo(expected.getStreet()))
            .satisfies(a -> assertThat(a.getCity()).as("check city").isEqualTo(expected.getCity()))
            .satisfies(a -> assertThat(a.getCountry()).as("check country").isEqualTo(expected.getCountry()))
            .satisfies(a -> assertThat(a.getPosition()).as("check position").isEqualTo(expected.getPosition()))
            .satisfies(a -> assertThat(a.getEmployedFrom()).as("check employedFrom").isEqualTo(expected.getEmployedFrom()))
            .satisfies(a -> assertThat(a.getImage()).as("check image").isEqualTo(expected.getImage()))
            .satisfies(a -> assertThat(a.getImageContentType()).as("check image contenty type").isEqualTo(expected.getImageContentType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBiographyUpdatableRelationshipsEquals(Biography expected, Biography actual) {
        // empty method
    }
}
