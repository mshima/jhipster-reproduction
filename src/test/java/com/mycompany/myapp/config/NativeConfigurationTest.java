package com.mycompany.myapp.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.aot.hint.predicate.RuntimeHintsPredicates.resource;

import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;

class NativeConfigurationTest {

    @Test
    void shouldRegisterStaticWebApplicationResources() {
        RuntimeHints hints = new RuntimeHints();

        new NativeConfiguration.JHipsterNativeRuntimeHints().registerHints(hints, getClass().getClassLoader());

        assertThat(resource().forResource("static/resource.txt").test(hints)).isTrue();
        assertThat(resource().forResource("i18n/resource.txt").test(hints)).isTrue();
        // jhipster-needle-add-native-hints-test - JHipster will add native hints here
    }
}
