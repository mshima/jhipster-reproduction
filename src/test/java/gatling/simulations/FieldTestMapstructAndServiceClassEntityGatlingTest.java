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
 * Performance test for the FieldTestMapstructAndServiceClassEntity entity.
 *
 * @see <a href="https://github.com/jhipster/generator-jhipster/tree/v9.2.0/generators/gatling#logging-tips">Logging tips</a>
 */
public class FieldTestMapstructAndServiceClassEntityGatlingTest extends Simulation {

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
                http("Get all fieldTestMapstructAndServiceClassEntities")
                    .get("/api/field-test-mapstruct-and-service-class-entities")
                    .headers(headersHttpAuthenticated)
                    .check(status().is(200))
            )
                .pause(Duration.ofSeconds(10), Duration.ofSeconds(20))
                .exec(
                    http("Create new fieldTestMapstructAndServiceClassEntity")
                        .post("/api/field-test-mapstruct-and-service-class-entities")
                        .headers(headersHttpAuthenticated)
                        .body(
                            StringBody(
                                "{" +
                                    "\"stringEva\": \"SAMPLE_TEXT\"" +
                                    ", \"stringRequiredEva\": \"SAMPLE_TEXT\"" +
                                    ", \"stringMinlengthEva\": \"SAMPLE_TEXT\"" +
                                    ", \"stringMaxlengthEva\": \"SAMPLE_TEXT\"" +
                                    ", \"stringPatternEva\": \"SAMPLE_TEXT\"" +
                                    ", \"integerEva\": 0" +
                                    ", \"integerRequiredEva\": 0" +
                                    ", \"integerMinEva\": 0" +
                                    ", \"integerMaxEva\": 0" +
                                    ", \"longEva\": 0" +
                                    ", \"longRequiredEva\": 0" +
                                    ", \"longMinEva\": 0" +
                                    ", \"longMaxEva\": 0" +
                                    ", \"floatEva\": 0" +
                                    ", \"floatRequiredEva\": 0" +
                                    ", \"floatMinEva\": 0" +
                                    ", \"floatMaxEva\": 0" +
                                    ", \"doubleRequiredEva\": 0" +
                                    ", \"doubleMinEva\": 0" +
                                    ", \"doubleMaxEva\": 0" +
                                    ", \"bigDecimalRequiredEva\": 0" +
                                    ", \"bigDecimalMinEva\": 0" +
                                    ", \"bigDecimalMaxEva\": 0" +
                                    ", \"localDateEva\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"localDateRequiredEva\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"instantEva\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"instanteRequiredEva\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"zonedDateTimeEva\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"zonedDateTimeRequiredEva\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"localTimeEva\": null" +
                                    ", \"localTimeRequiredEva\": null" +
                                    ", \"durationEva\": null" +
                                    ", \"durationRequiredEva\": null" +
                                    ", \"booleanEva\": null" +
                                    ", \"booleanRequiredEva\": null" +
                                    ", \"enumEva\": \"ENUM_VALUE_1\"" +
                                    ", \"enumRequiredEva\": \"ENUM_VALUE_1\"" +
                                    ", \"uuidEva\": null" +
                                    ", \"uuidRequiredEva\": null" +
                                    ", \"byteImageEva\": null" +
                                    ", \"byteImageRequiredEva\": null" +
                                    ", \"byteImageMinbytesEva\": null" +
                                    ", \"byteImageMaxbytesEva\": null" +
                                    ", \"byteAnyEva\": null" +
                                    ", \"byteAnyRequiredEva\": null" +
                                    ", \"byteAnyMinbytesEva\": null" +
                                    ", \"byteAnyMaxbytesEva\": null" +
                                    ", \"byteTextEva\": null" +
                                    ", \"byteTextRequiredEva\": null" +
                                    "}"
                            )
                        )
                        .asJson()
                        .check(status().is(201))
                        .check(headerRegex("Location", "(.*)").saveAs("new_fieldTestMapstructAndServiceClassEntity_url"))
                )
                .exitHereIfFailed()
                .pause(10)
                .repeat(5)
                .on(
                    exec(
                        http("Get created fieldTestMapstructAndServiceClassEntity")
                            .get("${new_fieldTestMapstructAndServiceClassEntity_url}")
                            .headers(headersHttpAuthenticated)
                    ).pause(10)
                )
                .exec(
                    http("Delete created fieldTestMapstructAndServiceClassEntity")
                        .delete("${new_fieldTestMapstructAndServiceClassEntity_url}")
                        .headers(headersHttpAuthenticated)
                )
                .pause(10)
        );

    ScenarioBuilder users = scenario("Test the FieldTestMapstructAndServiceClassEntity entity").exec(scn);

    {
        setUp(
            users.injectOpen(rampUsers(Integer.getInteger("users", 100)).during(Duration.ofMinutes(Integer.getInteger("ramp", 1))))
        ).protocols(httpConf);
    }
}
