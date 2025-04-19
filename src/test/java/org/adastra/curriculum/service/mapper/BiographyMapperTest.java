package org.adastra.curriculum.service.mapper;

import static org.adastra.curriculum.domain.BiographyAsserts.*;
import static org.adastra.curriculum.domain.BiographyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BiographyMapperTest {

    private BiographyMapper biographyMapper;

    @BeforeEach
    void setUp() {
        biographyMapper = new BiographyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBiographySample1();
        var actual = biographyMapper.toEntity(biographyMapper.toDto(expected));
        assertBiographyAllPropertiesEquals(expected, actual);
    }
}
