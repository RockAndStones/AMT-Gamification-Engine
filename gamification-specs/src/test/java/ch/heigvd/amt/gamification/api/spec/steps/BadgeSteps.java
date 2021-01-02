package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.Badge;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

public class BadgeSteps {
    private Environment environment;
    private DefaultApi api;
    private World world;

    public BadgeSteps(Environment environment, World world){
        this.environment = environment;
        this.world = world;
        this.api = environment.getApi();
        api.getApiClient().setApiKey(World.getApp().getApiKey());
    }

    @Given("I have a badge payload")
    public void iHaveABadgePayload() {
        world.setBadge(new ch.heigvd.amt.gamification.api.dto.Badge()
                .name("MyTestBadge")
                .description("This is my test badge"));
    }

    @Given("I have an empty badge payload")
    public void iHaveAnEmptyBadgePayload() {
        world.setBadge(new ch.heigvd.amt.gamification.api.dto.Badge()
                .name("")
                .description(""));
    }

    @Given("I have an unknown badge payload")
    public void iHaveAnUnknownBadgePayload() {
        world.setBadge(new ch.heigvd.amt.gamification.api.dto.Badge()
                .name("unknownBadge")
                .description("My unknown badge"));
    }

    @Given("I have a wrong badge payload")
    public void iHaveAWrongBadgePayload() {
        world.setBadge(new ch.heigvd.amt.gamification.api.dto.Badge()
                .description("My unknown badge"));
    }

    @Given("I have a modified badge payload")
    public void iHaveAModifiedBadgePayload() {
        world.setBadge(new ch.heigvd.amt.gamification.api.dto.Badge()
                .name("MyModifiedBadge")
                .description("This is my modified badge"));
    }

    @Given("I have another badge payload")
    public void iHaveAnotherBadgePayload() {
        world.setBadge(new ch.heigvd.amt.gamification.api.dto.Badge()
                .name("MyOtherTestBadge")
                .description("This is my other test badge"));
    }

    @When("I POST the badge payload to the /badges endpoint$")
    public void iPOSTTheBadgePayloadToTheBadgesEndpoint() {
        try {
            environment.setLastApiResponse(api.createBadgeWithHttpInfo(world.getBadge()));
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a GET to the /badges endpoint$")
    public void iSendAGETToTheBadgesEndpoint() {
        try {
            environment.setLastApiResponse(api.getBadgesWithHttpInfo());
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a GET to the /badge/\\{name} endpoint$")
    public void iSendAGETToTheBadgeNameEndpoint() {
        try {
            environment.setLastApiResponse(api.getBadgeWithHttpInfo(world.getBadge().getName()));
            environment.processApiResponse(environment.getLastApiResponse());
            world.setLastReceivedBadge((Badge) environment.getLastApiResponse().getData());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a DELETE to the /badge/\\{name} endpoint$")
    public void iSendADELETEToTheBadgeNameEndpoint() {
        try {
            environment.setLastApiResponse(api.removeBadgeWithHttpInfo(world.getBadge().getName()));
            environment.processApiResponse(environment.getLastApiResponse());
            world.setLastReceivedBadge((Badge) environment.getLastApiResponse().getData());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a PUT to the /badge/\\{name} endpoint$")
    public void iSendAPUTToTheBadgeNameEndpoint() {
        try {
            environment.setLastApiResponse(api.putBadgeWithHttpInfo("MyTestBadge", true, world.getBadge()));
            environment.processApiResponse(environment.getLastApiResponse());
            world.setLastReceivedBadge((Badge) environment.getLastApiResponse().getData());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a PUT to the /badge/\\{name} endpoint with usable at false$")
    public void iSendAPUTToTheBadgeNameEndpointWithUnusableAtTrue() {
        try {
            environment.setLastApiResponse(api.putBadgeWithHttpInfo(world.getBadge().getName(), false, world.getBadge()));
            environment.processApiResponse(environment.getLastApiResponse());
            world.setLastReceivedBadge((Badge) environment.getLastApiResponse().getData());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a PUT to the /badge/\\{name} endpoint with usable at true$")
    public void iSendAPUTToTheBadgeNameEndpointWithUsableAtTrue() {
        try {
            environment.setLastApiResponse(api.putBadgeWithHttpInfo(world.getBadge().getName(), true, world.getBadge()));
            environment.processApiResponse(environment.getLastApiResponse());
            world.setLastReceivedBadge((Badge) environment.getLastApiResponse().getData());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @And("I receive a payload that is the same as the last badge payload")
    public void iReceiveAPayloadThatIsTheSameAsTheCreatedBadgePayload() {
        assertEquals(world.getBadge(), world.getLastReceivedBadge());
    }


}
