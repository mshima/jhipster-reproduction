package com.okta.developer.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.okta.developer.gateway.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class AuthorityTestSamples {

    public static Authority getAuthoritySample1() {
        return new Authority().name("name1");
    }

    public static Authority getAuthoritySample2() {
        return new Authority().name("name2");
    }

    public static Authority getAuthorityRandomSampleGenerator() {
        return new Authority().name(UUID.randomUUID().toString());
    }
}
