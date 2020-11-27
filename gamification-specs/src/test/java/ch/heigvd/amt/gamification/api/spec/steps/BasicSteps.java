package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.api.dto.*;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.ApiResponse;
import ch.heigvd.amt.gamification.api.DefaultApi;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apiguardian.api.API;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BasicSteps {

    private Environment environment;
    private DefaultApi api;
    static String APIKEY = "";

    Event event;
    Badge badge;
    Rule rule;
    Application app;
    InlineObject obj;
    PointScale pointScale;
    Stage stage;

    private ApiResponse lastApiResponse;
    private ApiResponse lastApiResponseApp;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;

    private String lastReceivedLocationHeader;
    private Event lastReceivedEvent;
    private Badge lastReceivedBadge;

    public BasicSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
        api.getApiClient().setApiKey(APIKEY);
    }

    @Given("there is an Application server")
    public void thereIsAApplicationServer() throws Throwable {
        assertNotNull(api);
    }

    @Given("I have an application payload")
    public void iHaveAnApplicationPayload() {
        obj = new InlineObject().name("MyTestApp2");
    }

    @When("I POST the application payload to the /application endpoint$")
    public void iPOSTTheApplicationPayloadToTheApplicationEndpoint() {
        try {
            lastApiResponseApp = api.newApplicationWithHttpInfo(obj);
            processApiResponse(lastApiResponseApp);
            app = (Application) lastApiResponseApp.getData();
            APIKEY = app.getApiKey();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @Given("I have a event payload")
    public void i_have_a_event_payload() throws Throwable {
        event = new ch.heigvd.amt.gamification.api.dto.Event()
                .userAppId("userId")
                .timestamp(OffsetDateTime.now())
                .eventType("type");
//                .eventProperties(new Object());
    }

    //todo: fix test
    @When("^I POST the event payload to the /events endpoint$")
    public void i_POST_the_event_payload_to_the_events_endpoint() throws Throwable {
        try {
            lastApiResponse = api.createEventWithHttpInfo(event);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @Then("I receive a {int} status code")
    public void i_receive_a_status_code(int expectedStatusCode) throws Throwable {
        assertEquals(expectedStatusCode, lastStatusCode);
    }

    //todo: fix test
    @When("^I send a GET to the /events endpoint$")
    public void iSendAGETToTheEventsEndpoint() {
        try {
            lastApiResponse = api.getEventsWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @Then("I receive a {int} status code with a location header")
    public void iReceiveAStatusCodeWithALocationHeader(int arg0) {
    }

    @When("I send a GET to the URL in the location header")
    public void iSendAGETToTheURLInTheLocationHeader() {
        Integer id = Integer.parseInt(lastReceivedLocationHeader.substring(lastReceivedLocationHeader.lastIndexOf('/') + 1));
        try {
            lastApiResponse = api.getEventWithHttpInfo(id);
            processApiResponse(lastApiResponse);
            lastReceivedEvent = (Event)lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I receive a payload that is the same as the event payload")
    public void iReceiveAPayloadThatIsTheSameAsTheEventPayload() {
        assertEquals(event, lastReceivedEvent);
    }

    @Given("I have a badge payload")
    public void iHaveABadgePayload() {
        badge = new ch.heigvd.amt.gamification.api.dto.Badge()
                .name("MyTestBadge")
                .description("This is my test badge");
    }

    @Given("I have an empty badge payload")
    public void iHaveAnEmptyBadgePayload() {
        badge = new ch.heigvd.amt.gamification.api.dto.Badge()
                .name("")
                .description("");
    }

    @When("I POST the badge payload to the /badges endpoint$")
    public void iPOSTTheBadgePayloadToTheBadgesEndpoint() {
        try {
            lastApiResponse = api.createBadgeWithHttpInfo(badge);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("I send a GET to the /badges endpoint$")
    public void iSendAGETToTheBadgesEndpoint() {
        try {
            lastApiResponse = api.getBadgesWithHttpInfo();
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @When("I send a GET to the /badge/\\{name} endpoint$")
    public void iSendAGETToTheBadgeNameEndpoint() {
        try {
            lastApiResponse = api.getBadgeWithHttpInfo(badge.getName());
            processApiResponse(lastApiResponse);
            lastReceivedBadge = (Badge) lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @And("I receive a payload that is the same as the created badge payload")
    public void iReceiveAPayloadThatIsTheSameAsTheCreatedBadgePayload() {
        assertEquals(badge, lastReceivedBadge);
    }

    @When("I send a DELETE to the /badge/\\{name} endpoint$")
    public void iSendADELETEToTheBadgeNameEndpoint() {
        try {
            lastApiResponse = api.removeBadgeWithHttpInfo(badge.getName());
            processApiResponse(lastApiResponse);
            lastReceivedBadge = (Badge) lastApiResponse.getData();
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @Given("I have a rule payload")
    public void iHaveARulePayload() {
        rule = new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRule")
                .description("This is the rule for a test")
                .eventType("TestEvent")
                .pointScaleId(1)
                .pointsToAdd(10.0);
    }

    @Given("I have a rule payload with unknown badge")
    public void iHaveARulePayloadWithUnknownBadge() {
        rule = new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRule")
                .description("This is the rule for a test")
                .eventType("TestEvent")
                .pointScaleId(1)
                .pointsToAdd(10.0)
                .badgeName("UnknownBadge");
    }

    @Given("I have a rule payload with missing information")
    public void iHaveARulePayloadWithMissingInformation() {
        rule = new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRule")
                .description("This is the rule for a test")
                .pointsToAdd(10.0);
    }

    @When("I POST the rule payload to the /rules endpoint$")
    public void iPOSTTheRulePayloadToTheRulesEndpoint() {
        try {
            lastApiResponse = api.createRuleWithHttpInfo(rule);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    @Given("I have a stage payload")
    public void iHaveAStagePayload() {
        stage = new ch.heigvd.amt.gamification.api.dto.Stage()
                .badge(badge)
                .points(10.0);
    }

    @Given("I have a pointscale payload")
    public void iHaveAPointscalePayload() {
        List<Stage> stages = new ArrayList<>();
        stages.add(stage);
        pointScale = new ch.heigvd.amt.gamification.api.dto.PointScale()
                .stages(stages);
    }

    @When("I POST the pointscale payload to the /pointscales endpoint$")
    public void iPOSTThePointscalePayloadToThePointscalesEndpoint() {
        try {
            lastApiResponse = api.createPointScaleWithHttpInfo(pointScale);
            processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            processApiException(e);
        }
    }

    private void processApiResponse(ApiResponse apiResponse) {
        lastApiResponse = apiResponse;
        lastApiCallThrewException = false;
        lastApiException = null;
        lastStatusCode = lastApiResponse.getStatusCode();
        List<String> locationHeaderValues = (List<String>)lastApiResponse.getHeaders().get("Location");
        lastReceivedLocationHeader = locationHeaderValues != null ? locationHeaderValues.get(0) : null;
    }

    private void processApiException(ApiException apiException) {
        lastApiCallThrewException = true;
        lastApiResponse = null;
        lastApiException = apiException;
        lastStatusCode = lastApiException.getCode();
    }
}
