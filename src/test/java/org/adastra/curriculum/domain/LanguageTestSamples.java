package org.adastra.curriculum.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LanguageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Language getLanguageSample1() {
        return new Language().id(1L).expertise(1);
    }

    public static Language getLanguageSample2() {
        return new Language().id(2L).expertise(2);
    }

    public static Language getLanguageRandomSampleGenerator() {
        return new Language().id(longCount.incrementAndGet()).expertise(intCount.incrementAndGet());
    }
}
