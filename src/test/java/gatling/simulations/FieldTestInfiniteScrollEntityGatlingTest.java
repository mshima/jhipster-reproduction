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
 * Performance test for the FieldTestInfiniteScrollEntity entity.
 *
 * @see <a href="https://github.com/jhipster/generator-jhipster/tree/v9.2.0/generators/gatling#logging-tips">Logging tips</a>
 */
public class FieldTestInfiniteScrollEntityGatlingTest extends Simulation {

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
                http("Get all fieldTestInfiniteScrollEntities")
                    .get("/api/field-test-infinite-scroll-entities")
                    .headers(headersHttpAuthenticated)
                    .check(status().is(200))
            )
                .pause(Duration.ofSeconds(10), Duration.ofSeconds(20))
                .exec(
                    http("Create new fieldTestInfiniteScrollEntity")
                        .post("/api/field-test-infinite-scroll-entities")
                        .headers(headersHttpAuthenticated)
                        .body(
                            StringBody(
                                "{" +
                                    "\"stringHugo\": \"SAMPLE_TEXT\"" +
                                    ", \"stringRequiredHugo\": \"SAMPLE_TEXT\"" +
                                    ", \"stringMinlengthHugo\": \"SAMPLE_TEXT\"" +
                                    ", \"stringMaxlengthHugo\": \"SAMPLE_TEXT\"" +
                                    ", \"stringPatternHugo\": \"SAMPLE_TEXT\"" +
                                    ", \"integerHugo\": 0" +
                                    ", \"integerRequiredHugo\": 0" +
                                    ", \"integerMinHugo\": 0" +
                                    ", \"integerMaxHugo\": 0" +
                                    ", \"longHugo\": 0" +
                                    ", \"longRequiredHugo\": 0" +
                                    ", \"longMinHugo\": 0" +
                                    ", \"longMaxHugo\": 0" +
                                    ", \"floatHugo\": 0" +
                                    ", \"floatRequiredHugo\": 0" +
                                    ", \"floatMinHugo\": 0" +
                                    ", \"floatMaxHugo\": 0" +
                                    ", \"doubleRequiredHugo\": 0" +
                                    ", \"doubleMinHugo\": 0" +
                                    ", \"doubleMaxHugo\": 0" +
                                    ", \"bigDecimalRequiredHugo\": 0" +
                                    ", \"bigDecimalMinHugo\": 0" +
                                    ", \"bigDecimalMaxHugo\": 0" +
                                    ", \"localDateHugo\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"localDateRequiredHugo\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"instantHugo\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"instanteRequiredHugo\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"zonedDateTimeHugo\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"zonedDateTimeRequiredHugo\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"localTimeHugo\": null" +
                                    ", \"localTimeRequiredHugo\": null" +
                                    ", \"durationHugo\": null" +
                                    ", \"durationRequiredHugo\": null" +
                                    ", \"booleanHugo\": null" +
                                    ", \"booleanRequiredHugo\": null" +
                                    ", \"enumHugo\": \"ENUM_VALUE_1\"" +
                                    ", \"enumRequiredHugo\": \"ENUM_VALUE_1\"" +
                                    ", \"uuidHugo\": null" +
                                    ", \"uuidRequiredHugo\": null" +
                                    ", \"byteImageHugo\": null" +
                                    ", \"byteImageRequiredHugo\": null" +
                                    ", \"byteImageMinbytesHugo\": null" +
                                    ", \"byteImageMaxbytesHugo\": null" +
                                    ", \"byteAnyHugo\": null" +
                                    ", \"byteAnyRequiredHugo\": null" +
                                    ", \"byteAnyMinbytesHugo\": null" +
                                    ", \"byteAnyMaxbytesHugo\": null" +
                                    ", \"byteTextHugo\": null" +
                                    ", \"byteTextRequiredHugo\": null" +
                                    "}"
                            )
                        )
                        .asJson()
                        .check(status().is(201))
                        .check(headerRegex("Location", "(.*)").saveAs("new_fieldTestInfiniteScrollEntity_url"))
                )
                .exitHereIfFailed()
                .pause(10)
                .repeat(5)
                .on(
                    exec(
                        http("Get created fieldTestInfiniteScrollEntity")
                            .get("${new_fieldTestInfiniteScrollEntity_url}")
                            .headers(headersHttpAuthenticated)
                    ).pause(10)
                )
                .exec(
                    http("Delete created fieldTestInfiniteScrollEntity")
                        .delete("${new_fieldTestInfiniteScrollEntity_url}")
                        .headers(headersHttpAuthenticated)
                )
                .pause(10)
        );

    ScenarioBuilder users = scenario("Test the FieldTestInfiniteScrollEntity entity").exec(scn);

    {
        setUp(
            users.injectOpen(rampUsers(Integer.getInteger("users", 100)).during(Duration.ofMinutes(Integer.getInteger("ramp", 1))))
        ).protocols(httpConf);
    }
}
