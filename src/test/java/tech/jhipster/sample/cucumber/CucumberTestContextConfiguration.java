package tech.jhipster.sample.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import tech.jhipster.sample.IntegrationTest;
import tech.jhipster.sample.security.AuthoritiesConstants;

@ActiveProfiles({ "test", "testdev" })
@CucumberContextConfiguration
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_TIMEOUT)
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
public class CucumberTestContextConfiguration {}
