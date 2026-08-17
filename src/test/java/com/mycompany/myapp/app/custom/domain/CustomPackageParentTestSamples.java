package com.mycompany.myapp.app.custom.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomPackageParentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CustomPackageParent getCustomPackageParentSample1() {
        return new CustomPackageParent().id(1L).parentName("parentName1");
    }

    public static CustomPackageParent getCustomPackageParentSample2() {
        return new CustomPackageParent().id(2L).parentName("parentName2");
    }

    public static CustomPackageParent getCustomPackageParentRandomSampleGenerator() {
        return new CustomPackageParent().id(longCount.incrementAndGet()).parentName(UUID.randomUUID().toString());
    }
}
