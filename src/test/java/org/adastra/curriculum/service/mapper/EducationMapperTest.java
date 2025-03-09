package org.adastra.curriculum.service.mapper;

import static org.adastra.curriculum.domain.EducationAsserts.*;
import static org.adastra.curriculum.domain.EducationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EducationMapperTest {

    private EducationMapper educationMapper;

    @BeforeEach
    void setUp() {
        educationMapper = new EducationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEducationSample1();
        var actual = educationMapper.toEntity(educationMapper.toDto(expected));
        assertEducationAllPropertiesEquals(expected, actual);
    }
}
