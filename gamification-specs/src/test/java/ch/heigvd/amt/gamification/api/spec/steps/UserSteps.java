package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.Badge;
import ch.heigvd.amt.gamification.api.dto.Event;
import ch.heigvd.amt.gamification.api.dto.User;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UserSteps {
    private Environment environment;
    private DefaultApi api;
    private World world;

    public UserSteps(Environment environment, World world) {
        this.environment = environment;
        this.world = world;
        this.api = environment.getApi();
        api.getApiClient().setApiKey(World.getApp().getApiKey());
    }

    @Given("I have a event payload and a user")
    public void iHaveAEventPayloadAndAUser() {
        world.setEvent(new Event()
                .userAppId("userId")
                .timestamp(OffsetDateTime.now())
                .eventType("type"));
        world.setUser(new User()
                .userAppId("userId")
                .badges(new ArrayList<>()));
    }

    @When("I send a GET to the /users/\\{userAppId} endpoint$")
    public void iSendAGETToTheUsersUserAppIdEndpoint() {
        try {
            environment.setLastApiResponse(api.getUserWithHttpInfo(world.getUser().getUserAppId()));
            environment.processApiResponse(environment.getLastApiResponse());
            world.setLastReceivedUser((User) environment.getLastApiResponse().getData());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @Then("I receive a user payload with the same \\{userAppId}$")
    public void iReceiveAUserPayloadWithTheSameUserAppId() {
        assertEquals(world.getUser(), world.getLastReceivedUser());
    }

    @When("I send a GET to the \\/users endpoint$")
    public void iSendAGETToTheUsersEndpoint() {
        try {
            environment.setLastApiResponse(api.getUsersWithHttpInfo());
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }
}