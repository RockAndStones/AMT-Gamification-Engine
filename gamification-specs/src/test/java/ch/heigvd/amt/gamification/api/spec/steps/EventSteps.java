package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.Event;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.OffsetDateTime;

import static org.junit.Assert.assertEquals;

public class EventSteps {
    private Environment environment;
    private DefaultApi api;
    private World world;

    public EventSteps(Environment environment, World world){
        this.environment = environment;
        this.world = world;
        this.api = environment.getApi();
        api.getApiClient().setApiKey(World.getApp().getApiKey());
    }

    @Given("I have a event payload")
    public void i_have_a_event_payload() throws Throwable {
        world.setEvent(new ch.heigvd.amt.gamification.api.dto.Event()
                .userAppId("userId")
                .timestamp(OffsetDateTime.now())
                .eventType("type"));
    }

    @When("^I POST the event payload to the /events endpoint$")
    public void i_POST_the_event_payload_to_the_events_endpoint() throws Throwable {
        try {
            environment.setLastApiResponse(api.createEventWithHttpInfo(world.getEvent()));
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("^I send a GET to the /events endpoint$")
    public void iSendAGETToTheEventsEndpoint() {
        try {
            environment.setLastApiResponse(api.getEventsWithHttpInfo());
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @Then("I receive a {int} status code with a location header")
    public void iReceiveAStatusCodeWithALocationHeader(int arg0) {
    }

    @When("I send a GET to the URL in the location header")
    public void iSendAGETToTheURLInTheLocationHeader() {
        Integer id = Integer.parseInt(environment.getLastReceivedLocationHeader().substring(environment.getLastReceivedLocationHeader().lastIndexOf('/') + 1));
        try {
            environment.setLastApiResponse(api.getEventWithHttpInfo(id));
            environment.processApiResponse(environment.getLastApiResponse());
            world.setLastReceivedEvent((Event)environment.getLastApiResponse().getData());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @And("I receive a payload that is the same as the event payload")
    public void iReceiveAPayloadThatIsTheSameAsTheEventPayload() {
        assertEquals(world.getEvent(), world.getLastReceivedEvent());
    }
}
