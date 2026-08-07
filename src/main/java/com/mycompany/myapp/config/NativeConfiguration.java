package com.mycompany.myapp.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class NativeConfiguration {

    public static class JHipsterNativeRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources().registerPattern("static/**");
            hints.resources().registerPattern("i18n/**");
            hints.reflection().registerType(java.util.Locale.class, hint -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_METHODS));
            hints.reflection().registerType(java.util.Calendar[].class, hint -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_METHODS));
            hints
                .reflection()
                .registerType(org.hibernate.binder.internal.BatchSizeBinder.class, hint ->
                    hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS)
                );
            hints
                .reflection()
                .registerType(com.zaxxer.hikari.HikariDataSource.class, hint -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_METHODS));
            hints.resources().registerPattern("config/liquibase/**");
            // jhipster-needle-add-native-hints - JHipster will add native hints here
        }
    }
}
