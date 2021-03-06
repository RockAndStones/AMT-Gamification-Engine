package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.PointScale;
import ch.heigvd.amt.gamification.api.dto.Rule;
import ch.heigvd.amt.gamification.api.dto.RuleInfo;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RuleSteps {
    private Environment environment;
    private DefaultApi api;
    private World world;

    public RuleSteps(Environment environment, World world){
        this.environment = environment;
        this.world = world;
        this.api = environment.getApi();
        api.getApiClient().setApiKey(World.getApp().getApiKey());
    }

    @Given("I have a rule payload")
    public void iHaveARulePayload() {
        world.setRule(new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRule")
                .description("This is the rule for a test")
                .eventType("TestEvent")
                .pointScaleId(world.getPointScaleInfo().getId())
                .pointsToAdd(10.0));
    }

    @Given("I have a rule payload with unknown badge")
    public void iHaveARulePayloadWithUnknownBadge() {
        world.setRule(new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRule")
                .description("This is the rule for a test")
                .eventType("TestEvent")
                .pointScaleId(world.getPointScaleInfo().getId())
                .pointsToAdd(10.0)
                .badgeName("UnknownBadge"));
    }

    @Given("I have a rule payload with an unknown pointscale")
    public void iHaveARulePayloadWithAnUnknownPointscale() {
        world.setRule(new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRule")
                .description("This is the rule for a test")
                .eventType("TestEvent")
                .pointScaleId(10)
                .pointsToAdd(10.0));
    }

    @Given("I have a rule payload with missing information")
    public void iHaveARulePayloadWithMissingInformation() {
        world.setRule(new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRule")
                .description("This is the rule for a test")
                .pointsToAdd(10.0));
    }

    @Given("I have a rule payload with same eventype and pointscale")
    public void iHaveARulePayloadWithSameEventypeAndPointscale() {
        world.setRule(new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRuleWithSameEventAndPointScale")
                .description("This is the rule for a test")
                .eventType("TestEvent")
                .pointScaleId(world.getPointScaleInfo().getId())
                .pointsToAdd(20.0));
    }

    @Given("I have a rule payload that add {int} points")
    public void iHaveARulePayloadThatAddPoints(int points) {
        world.setRule(new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRuleWith5Points")
                .description("This is the rule for a test that add five points")
                .eventType("TestEvent")
                .pointScaleId(world.getPointScaleInfo().getId())
                .pointsToAdd((double) points));
    }

    @Given("I have a rule payload that add {int} points and a badge")
    public void iHaveARulePayloadThatAddPointsAndABadge(int points) {
        world.setRule(new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRuleWith5PointsAndABadge")
                .description("This is the rule for a test that add five points and a beautiful badge")
                .eventType("EventForABeautifulBadge")
                .badgeName("MyTestBadge")
                .pointScaleId(world.getPointScaleInfo().getId())
                .pointsToAdd((double) points));
    }

    @Then("I have a rule id")
    public void iHaveARuleId() {
        assertNotEquals((int) world.getRuleInfo().getId(), -1);
    }

    @Given("I have an unknown rule id")
    public void iHaveAnUnknownRuleId() {
        world.setRuleInfo(new ch.heigvd.amt.gamification.api.dto.RuleInfo()
                .id(-1));
    }

    @When("I POST the rule payload to the /rules endpoint$")
    public void iPOSTTheRulePayloadToTheRulesEndpoint() {
        try {
            environment.setLastApiResponse(api.createRuleWithHttpInfo(world.getRule()));
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send DELETE the rule id to the \\/rules\\/\\{id} endpoint$")
    public void iSendDELETETheRuleIdToTheRulesIdEndpoint() {
        try {
            environment.setLastApiResponse(api.removeRuleWithHttpInfo(world.getRuleInfo().getId()));
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I GET the rule payload from the \\/rules endpoint$")
    public void iGETTheRulePayloadFromTheRulesEndpoint() {
        try {
            environment.setLastApiResponse(api.getRulesWithHttpInfo());
            environment.processApiResponse(environment.getLastApiResponse());
            List<RuleInfo> rules = (List<RuleInfo>) environment.getLastApiResponse().getData();
            world.setRuleInfo(rules.get(rules.size() - 1));
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a GET to the /rules/\\{id} endpoint$")
    public void iSendAGETToTheRulesIdEndpoint() {
        try {
            environment.setLastApiResponse(api.getRuleWithHttpInfo(world.getRuleInfo().getId()));
            environment.processApiResponse(environment.getLastApiResponse());
            world.setLastReceivedRule((Rule) environment.getLastApiResponse().getData());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a GET to the \\/rules endpoint$")
    public void iSendAGETToTheRulesEndpoint() {
        try {
            environment.setLastApiResponse(api.getRulesWithHttpInfo());
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @And("I receive a payload that is the same as the rule payload")
    public void iReceiveAPayloadThatIsTheSameAsTheLastRulePayload() {
        // The database doesn't send a badge null
        world.getRule().setBadgeName("");
        assertEquals(world.getRule(), world.getLastReceivedRule());
    }
}
