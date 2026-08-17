package com.mycompany.myapp.app.child.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomPackageChildTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CustomPackageChild getCustomPackageChildSample1() {
        return new CustomPackageChild().id(1L).childName("childName1");
    }

    public static CustomPackageChild getCustomPackageChildSample2() {
        return new CustomPackageChild().id(2L).childName("childName2");
    }

    public static CustomPackageChild getCustomPackageChildRandomSampleGenerator() {
        return new CustomPackageChild().id(longCount.incrementAndGet()).childName(UUID.randomUUID().toString());
    }
}
