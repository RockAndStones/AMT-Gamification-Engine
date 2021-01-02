package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.Badge;
import ch.heigvd.amt.gamification.api.dto.Event;
import ch.heigvd.amt.gamification.api.dto.User;
import ch.heigvd.amt.gamification.api.dto.UserInfo;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.And;
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
                .points(0)
                .badges(new ArrayList<>()));
    }

    @Given("I have a user payload")
    public void iHaveAUserPayload() {
        world.setUser(new User()
                .userAppId("testUser")
                .badges(new ArrayList<>()));
    }

    @Given("I have an unknown user")
    public void iHaveAnUnknownUser() {
        world.setUser(new User()
                .userAppId("unknownUserId")
                .badges(new ArrayList<>()));
    }

    @When("I send a GET to the /users/\\{userAppId} endpoint$")
    public void iSendAGETToTheUsersUserAppIdEndpoint() {
        try {
            environment.setLastApiResponse(api.getUserWithHttpInfo(world.getUser().getUserAppId()));
            environment.processApiResponse(environment.getLastApiResponse());
            world.setUserInfo((UserInfo) environment.getLastApiResponse().getData());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @Then("I receive a user payload with the same \\{userAppId}$")
    public void iReceiveAUserPayloadWithTheSameUserAppId() {
        assertEquals(world.getUser().getBadges(), world.getUserInfo().getBadges());
        assertEquals(world.getUser().getPoints(), world.getUserInfo().getPoints());
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

    @And("user points are equal to {int}")
    public void userPointsAreEqualTo(int points) {
        assert world.getUserInfo().getPoints() != null;
        assertEquals(points, world.getUserInfo().getPoints().intValue());
    }

    @And("user badges last badge is equal to the badge payload")
    public void userBadgesAreEqualToTheBadgePayload() {
        assert world.getUserInfo().getBadges() != null;
        assertEquals(world.getBadge(), world.getUserInfo().getBadges().get(world.getUserInfo().getBadges().size() - 1));
    }

    @And("user badges is empty")
    public void userBadgesIsEmpty() {
        assert world.getUserInfo().getBadges() != null;
        assertEquals(0, world.getUserInfo().getBadges().size());
    }
}
