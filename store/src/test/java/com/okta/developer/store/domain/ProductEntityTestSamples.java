package com.okta.developer.store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.okta.developer.store.web.rest.TestUtil;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;

public class ProductEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductEntity getProductEntitySample1() {
        return new ProductEntity().id(1L).title("title1");
    }

    public static ProductEntity getProductEntitySample2() {
        return new ProductEntity().id(2L).title("title2");
    }

    public static ProductEntity getProductEntityRandomSampleGenerator() {
        return new ProductEntity().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
