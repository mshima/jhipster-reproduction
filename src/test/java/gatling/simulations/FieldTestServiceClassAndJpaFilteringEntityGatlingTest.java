package gatling.simulations;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.css;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.header;
import static io.gatling.javaapi.http.HttpDsl.headerRegex;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * Performance test for the FieldTestServiceClassAndJpaFilteringEntity entity.
 *
 * @see <a href="https://github.com/jhipster/generator-jhipster/tree/v9.2.0/generators/gatling#logging-tips">Logging tips</a>
 */
public class FieldTestServiceClassAndJpaFilteringEntityGatlingTest extends Simulation {

    String baseURL = Optional.ofNullable(System.getProperty("baseURL")).orElse("http://localhost:8080");

    HttpProtocolBuilder httpConf = http
        .baseUrl(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")
        .silentResources(); // Silence all resources like css or js so they don't clutter the results

    Map<String, String> headersHttp = Map.of("Accept", "application/json");

    Map<String, String> headersHttpAuthentication = Map.of("Content-Type", "application/json", "Accept", "application/json");

    Map<String, String> headersHttpAuthenticated = Map.of("Accept", "application/json", "Authorization", "${access_token}");

    ChainBuilder scn = exec(http("First unauthenticated request").get("/api/account").headers(headersHttp).check(status().is(401)))
        .exitHereIfFailed()
        .pause(10)
        .exec(
            http("Authentication")
                .post("/api/authenticate")
                .headers(headersHttpAuthentication)
                .body(StringBody("{\"username\":\"admin\", \"password\":\"admin\"}"))
                .asJson()
                .check(header("Authorization").saveAs("access_token"))
        )
        .exitHereIfFailed()
        .pause(2)
        .exec(http("Authenticated request").get("/api/account").headers(headersHttpAuthenticated).check(status().is(200)))
        .pause(10)
        .repeat(2)
        .on(
            exec(
                http("Get all fieldTestServiceClassAndJpaFilteringEntities")
                    .get("/api/field-test-service-class-and-jpa-filtering-entities")
                    .headers(headersHttpAuthenticated)
                    .check(status().is(200))
            )
                .pause(Duration.ofSeconds(10), Duration.ofSeconds(20))
                .exec(
                    http("Create new fieldTestServiceClassAndJpaFilteringEntity")
                        .post("/api/field-test-service-class-and-jpa-filtering-entities")
                        .headers(headersHttpAuthenticated)
                        .body(
                            StringBody(
                                "{" +
                                    "\"stringBob\": \"SAMPLE_TEXT\"" +
                                    ", \"stringRequiredBob\": \"SAMPLE_TEXT\"" +
                                    ", \"stringMinlengthBob\": \"SAMPLE_TEXT\"" +
                                    ", \"stringMaxlengthBob\": \"SAMPLE_TEXT\"" +
                                    ", \"stringPatternBob\": \"SAMPLE_TEXT\"" +
                                    ", \"integerBob\": 0" +
                                    ", \"integerRequiredBob\": 0" +
                                    ", \"integerMinBob\": 0" +
                                    ", \"integerMaxBob\": 0" +
                                    ", \"longBob\": 0" +
                                    ", \"longRequiredBob\": 0" +
                                    ", \"longMinBob\": 0" +
                                    ", \"longMaxBob\": 0" +
                                    ", \"floatBob\": 0" +
                                    ", \"floatRequiredBob\": 0" +
                                    ", \"floatMinBob\": 0" +
                                    ", \"floatMaxBob\": 0" +
                                    ", \"doubleRequiredBob\": 0" +
                                    ", \"doubleMinBob\": 0" +
                                    ", \"doubleMaxBob\": 0" +
                                    ", \"bigDecimalRequiredBob\": 0" +
                                    ", \"bigDecimalMinBob\": 0" +
                                    ", \"bigDecimalMaxBob\": 0" +
                                    ", \"localDateBob\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"localDateRequiredBob\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"instantBob\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"instanteRequiredBob\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"zonedDateTimeBob\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"zonedDateTimeRequiredBob\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"localTimeBob\": null" +
                                    ", \"localTimeRequiredBob\": null" +
                                    ", \"durationBob\": null" +
                                    ", \"durationRequiredBob\": null" +
                                    ", \"booleanBob\": null" +
                                    ", \"booleanRequiredBob\": null" +
                                    ", \"enumBob\": \"ENUM_VALUE_1\"" +
                                    ", \"enumRequiredBob\": \"ENUM_VALUE_1\"" +
                                    ", \"uuidBob\": null" +
                                    ", \"uuidRequiredBob\": null" +
                                    ", \"byteImageBob\": null" +
                                    ", \"byteImageRequiredBob\": null" +
                                    ", \"byteImageMinbytesBob\": null" +
                                    ", \"byteImageMaxbytesBob\": null" +
                                    ", \"byteAnyBob\": null" +
                                    ", \"byteAnyRequiredBob\": null" +
                                    ", \"byteAnyMinbytesBob\": null" +
                                    ", \"byteAnyMaxbytesBob\": null" +
                                    ", \"byteTextBob\": null" +
                                    ", \"byteTextRequiredBob\": null" +
                                    "}"
                            )
                        )
                        .asJson()
                        .check(status().is(201))
                        .check(headerRegex("Location", "(.*)").saveAs("new_fieldTestServiceClassAndJpaFilteringEntity_url"))
                )
                .exitHereIfFailed()
                .pause(10)
                .repeat(5)
                .on(
                    exec(
                        http("Get created fieldTestServiceClassAndJpaFilteringEntity")
                            .get("${new_fieldTestServiceClassAndJpaFilteringEntity_url}")
                            .headers(headersHttpAuthenticated)
                    ).pause(10)
                )
                .exec(
                    http("Delete created fieldTestServiceClassAndJpaFilteringEntity")
                        .delete("${new_fieldTestServiceClassAndJpaFilteringEntity_url}")
                        .headers(headersHttpAuthenticated)
                )
                .pause(10)
        );

    ScenarioBuilder users = scenario("Test the FieldTestServiceClassAndJpaFilteringEntity entity").exec(scn);

    {
        setUp(
            users.injectOpen(rampUsers(Integer.getInteger("users", 100)).during(Duration.ofMinutes(Integer.getInteger("ramp", 1))))
        ).protocols(httpConf);
    }
}
