package com.okta.developer.notification.config;

import reactor.blockhound.BlockHound;
import reactor.blockhound.integration.BlockHoundIntegration;

public class JHipsterBlockHoundIntegration implements BlockHoundIntegration {

    @Override
    public void applyTo(BlockHound.Builder builder) {
        builder.allowBlockingCallsInside("java.util.UUID", "randomUUID");
        builder.allowBlockingCallsInside("org.springframework.validation.beanvalidation.SpringValidatorAdapter", "validate");
        builder.allowBlockingCallsInside("com.okta.developer.notification.service.MailService", "sendEmailFromTemplate");
        builder.allowBlockingCallsInside("com.okta.developer.notification.security.DomainUserDetailsService", "createSpringSecurityUser");
        builder.allowBlockingCallsInside("org.mariadb.r2dbc.message.client.HandshakeResponse", "writeConnectAttributes");
        builder.allowBlockingCallsInside("org.mariadb.r2dbc.client.MariadbPacketDecoder", "decode");
        builder.allowBlockingCallsInside("org.mariadb.r2dbc.client.SimpleClient", "lambda$receive$18");
        builder.allowBlockingCallsInside("org.springframework.web.reactive.result.method.InvocableHandlerMethod", "invoke");
        builder.allowBlockingCallsInside("org.springdoc.core.service.OpenAPIService", "build");
        builder.allowBlockingCallsInside("org.springdoc.core.service.OpenAPIService", "getWebhooksClasses");
        builder.allowBlockingCallsInside("org.springdoc.core.service.AbstractRequestService", "build");
        builder.allowBlockingCallsInside("org.mariadb.r2dbc.client.SimpleClient", "sendCommand");
        // jhipster-needle-blockhound-integration - JHipster will add additional gradle plugins here
    }
}
