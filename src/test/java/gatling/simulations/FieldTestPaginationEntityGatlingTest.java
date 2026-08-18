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
 * Performance test for the FieldTestPaginationEntity entity.
 *
 * @see <a href="https://github.com/jhipster/generator-jhipster/tree/v9.2.0/generators/gatling#logging-tips">Logging tips</a>
 */
public class FieldTestPaginationEntityGatlingTest extends Simulation {

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
                http("Get all fieldTestPaginationEntities")
                    .get("/api/field-test-pagination-entities")
                    .headers(headersHttpAuthenticated)
                    .check(status().is(200))
            )
                .pause(Duration.ofSeconds(10), Duration.ofSeconds(20))
                .exec(
                    http("Create new fieldTestPaginationEntity")
                        .post("/api/field-test-pagination-entities")
                        .headers(headersHttpAuthenticated)
                        .body(
                            StringBody(
                                "{" +
                                    "\"stringAlice\": \"SAMPLE_TEXT\"" +
                                    ", \"stringRequiredAlice\": \"SAMPLE_TEXT\"" +
                                    ", \"stringMinlengthAlice\": \"SAMPLE_TEXT\"" +
                                    ", \"stringMaxlengthAlice\": \"SAMPLE_TEXT\"" +
                                    ", \"stringPatternAlice\": \"SAMPLE_TEXT\"" +
                                    ", \"integerAlice\": 0" +
                                    ", \"integerRequiredAlice\": 0" +
                                    ", \"integerMinAlice\": 0" +
                                    ", \"integerMaxAlice\": 0" +
                                    ", \"longAlice\": 0" +
                                    ", \"longRequiredAlice\": 0" +
                                    ", \"longMinAlice\": 0" +
                                    ", \"longMaxAlice\": 0" +
                                    ", \"floatAlice\": 0" +
                                    ", \"floatRequiredAlice\": 0" +
                                    ", \"floatMinAlice\": 0" +
                                    ", \"floatMaxAlice\": 0" +
                                    ", \"doubleRequiredAlice\": 0" +
                                    ", \"doubleMinAlice\": 0" +
                                    ", \"doubleMaxAlice\": 0" +
                                    ", \"bigDecimalRequiredAlice\": 0" +
                                    ", \"bigDecimalMinAlice\": 0" +
                                    ", \"bigDecimalMaxAlice\": 0" +
                                    ", \"localDateAlice\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"localDateRequiredAlice\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"instantAlice\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"instanteRequiredAlice\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"zonedDateTimeAlice\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"zonedDateTimeRequiredAlice\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"localTimeAlice\": null" +
                                    ", \"localTimeRequiredAlice\": null" +
                                    ", \"durationAlice\": null" +
                                    ", \"durationRequiredAlice\": null" +
                                    ", \"booleanAlice\": null" +
                                    ", \"booleanRequiredAlice\": null" +
                                    ", \"enumAlice\": \"ENUM_VALUE_1\"" +
                                    ", \"enumRequiredAlice\": \"ENUM_VALUE_1\"" +
                                    ", \"uuidAlice\": null" +
                                    ", \"uuidRequiredAlice\": null" +
                                    ", \"byteImageAlice\": null" +
                                    ", \"byteImageRequiredAlice\": null" +
                                    ", \"byteImageMinbytesAlice\": null" +
                                    ", \"byteImageMaxbytesAlice\": null" +
                                    ", \"byteAnyAlice\": null" +
                                    ", \"byteAnyRequiredAlice\": null" +
                                    ", \"byteAnyMinbytesAlice\": null" +
                                    ", \"byteAnyMaxbytesAlice\": null" +
                                    ", \"byteTextAlice\": null" +
                                    ", \"byteTextRequiredAlice\": null" +
                                    "}"
                            )
                        )
                        .asJson()
                        .check(status().is(201))
                        .check(headerRegex("Location", "(.*)").saveAs("new_fieldTestPaginationEntity_url"))
                )
                .exitHereIfFailed()
                .pause(10)
                .repeat(5)
                .on(
                    exec(
                        http("Get created fieldTestPaginationEntity")
                            .get("${new_fieldTestPaginationEntity_url}")
                            .headers(headersHttpAuthenticated)
                    ).pause(10)
                )
                .exec(
                    http("Delete created fieldTestPaginationEntity")
                        .delete("${new_fieldTestPaginationEntity_url}")
                        .headers(headersHttpAuthenticated)
                )
                .pause(10)
        );

    ScenarioBuilder users = scenario("Test the FieldTestPaginationEntity entity").exec(scn);

    {
        setUp(
            users.injectOpen(rampUsers(Integer.getInteger("users", 100)).during(Duration.ofMinutes(Integer.getInteger("ramp", 1))))
        ).protocols(httpConf);
    }
}
