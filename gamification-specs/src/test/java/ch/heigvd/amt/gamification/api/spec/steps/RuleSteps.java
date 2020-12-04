package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.PointScale;
import ch.heigvd.amt.gamification.api.dto.Rule;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class RuleSteps {
    private Environment environment;
    private DefaultApi api;
    private World world;
    private int id = -1;

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
                .pointScaleId(2)
                .pointsToAdd(10.0));
    }

    @Given("I have a rule payload with unknown badge")
    public void iHaveARulePayloadWithUnknownBadge() {
        world.setRule(new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRule")
                .description("This is the rule for a test")
                .eventType("TestEvent")
                .pointScaleId(2)
                .pointsToAdd(10.0)
                .badgeName("UnknownBadge"));
    }

    @Given("I have a rule payload with missing information")
    public void iHaveARulePayloadWithMissingInformation() {
        world.setRule(new ch.heigvd.amt.gamification.api.dto.Rule()
                .name("MyTestRule")
                .description("This is the rule for a test")
                .pointsToAdd(10.0));
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

    @Given("I have a rule id")
    public void iHaveARuleId() {
        assertNotEquals(id, -1);
    }

    @When("I send DELETE the rule id to the \\/rules\\/\\{id} endpoint$")
    public void iSendDELETETheRuleIdToTheRulesIdEndpoint() {
        try {
            environment.setLastApiResponse(api.removeRuleWithHttpInfo(id));
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
            List<Rule> rules = (List<Rule>) environment.getLastApiResponse().getData();
            id = rules.size();
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }
}
