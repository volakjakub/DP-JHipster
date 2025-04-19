package org.adastra.curriculum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EducationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Education getEducationSample1() {
        return new Education().id(1L).school("school1");
    }

    public static Education getEducationSample2() {
        return new Education().id(2L).school("school2");
    }

    public static Education getEducationRandomSampleGenerator() {
        return new Education().id(longCount.incrementAndGet()).school(UUID.randomUUID().toString());
    }
}
