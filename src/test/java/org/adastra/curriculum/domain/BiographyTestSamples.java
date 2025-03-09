package org.adastra.curriculum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BiographyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Biography getBiographySample1() {
        return new Biography()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .title("title1")
            .phone("phone1")
            .email("email1")
            .street("street1")
            .city("city1")
            .country("country1")
            .position("position1")
            .image("image1");
    }

    public static Biography getBiographySample2() {
        return new Biography()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .title("title2")
            .phone("phone2")
            .email("email2")
            .street("street2")
            .city("city2")
            .country("country2")
            .position("position2")
            .image("image2");
    }

    public static Biography getBiographyRandomSampleGenerator() {
        return new Biography()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .street(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .position(UUID.randomUUID().toString())
            .image(UUID.randomUUID().toString());
    }
}
