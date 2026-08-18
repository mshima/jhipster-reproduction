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
 * Performance test for the FieldTestServiceImplEntity entity.
 *
 * @see <a href="https://github.com/jhipster/generator-jhipster/tree/v9.2.0/generators/gatling#logging-tips">Logging tips</a>
 */
public class FieldTestServiceImplEntityGatlingTest extends Simulation {

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
                http("Get all fieldTestServiceImplEntities")
                    .get("/api/field-test-service-impl-entities")
                    .headers(headersHttpAuthenticated)
                    .check(status().is(200))
            )
                .pause(Duration.ofSeconds(10), Duration.ofSeconds(20))
                .exec(
                    http("Create new fieldTestServiceImplEntity")
                        .post("/api/field-test-service-impl-entities")
                        .headers(headersHttpAuthenticated)
                        .body(
                            StringBody(
                                "{" +
                                    "\"stringMika\": \"SAMPLE_TEXT\"" +
                                    ", \"stringRequiredMika\": \"SAMPLE_TEXT\"" +
                                    ", \"stringMinlengthMika\": \"SAMPLE_TEXT\"" +
                                    ", \"stringMaxlengthMika\": \"SAMPLE_TEXT\"" +
                                    ", \"stringPatternMika\": \"SAMPLE_TEXT\"" +
                                    ", \"integerMika\": 0" +
                                    ", \"integerRequiredMika\": 0" +
                                    ", \"integerMinMika\": 0" +
                                    ", \"integerMaxMika\": 0" +
                                    ", \"longMika\": 0" +
                                    ", \"longRequiredMika\": 0" +
                                    ", \"longMinMika\": 0" +
                                    ", \"longMaxMika\": 0" +
                                    ", \"floatMika\": 0" +
                                    ", \"floatRequiredMika\": 0" +
                                    ", \"floatMinMika\": 0" +
                                    ", \"floatMaxMika\": 0" +
                                    ", \"doubleRequiredMika\": 0" +
                                    ", \"doubleMinMika\": 0" +
                                    ", \"doubleMaxMika\": 0" +
                                    ", \"bigDecimalRequiredMika\": 0" +
                                    ", \"bigDecimalMinMika\": 0" +
                                    ", \"bigDecimalMaxMika\": 0" +
                                    ", \"localDateMika\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"localDateRequiredMika\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"instantMika\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"instanteRequiredMika\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"zonedDateTimeMika\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"zonedDateTimeRequiredMika\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"localTimeMika\": null" +
                                    ", \"localTimeRequiredMika\": null" +
                                    ", \"durationMika\": null" +
                                    ", \"durationRequiredMika\": null" +
                                    ", \"booleanMika\": null" +
                                    ", \"booleanRequiredMika\": null" +
                                    ", \"enumMika\": \"ENUM_VALUE_1\"" +
                                    ", \"enumRequiredMika\": \"ENUM_VALUE_1\"" +
                                    ", \"uuidMika\": null" +
                                    ", \"uuidRequiredMika\": null" +
                                    ", \"byteImageMika\": null" +
                                    ", \"byteImageRequiredMika\": null" +
                                    ", \"byteImageMinbytesMika\": null" +
                                    ", \"byteImageMaxbytesMika\": null" +
                                    ", \"byteAnyMika\": null" +
                                    ", \"byteAnyRequiredMika\": null" +
                                    ", \"byteAnyMinbytesMika\": null" +
                                    ", \"byteAnyMaxbytesMika\": null" +
                                    ", \"byteTextMika\": null" +
                                    ", \"byteTextRequiredMika\": null" +
                                    "}"
                            )
                        )
                        .asJson()
                        .check(status().is(201))
                        .check(headerRegex("Location", "(.*)").saveAs("new_fieldTestServiceImplEntity_url"))
                )
                .exitHereIfFailed()
                .pause(10)
                .repeat(5)
                .on(
                    exec(
                        http("Get created fieldTestServiceImplEntity")
                            .get("${new_fieldTestServiceImplEntity_url}")
                            .headers(headersHttpAuthenticated)
                    ).pause(10)
                )
                .exec(
                    http("Delete created fieldTestServiceImplEntity")
                        .delete("${new_fieldTestServiceImplEntity_url}")
                        .headers(headersHttpAuthenticated)
                )
                .pause(10)
        );

    ScenarioBuilder users = scenario("Test the FieldTestServiceImplEntity entity").exec(scn);

    {
        setUp(
            users.injectOpen(rampUsers(Integer.getInteger("users", 100)).during(Duration.ofMinutes(Integer.getInteger("ramp", 1))))
        ).protocols(httpConf);
    }
}
