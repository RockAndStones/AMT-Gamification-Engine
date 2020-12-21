package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.PointScale;
import ch.heigvd.amt.gamification.api.dto.PointScaleInfo;
import ch.heigvd.amt.gamification.api.dto.Stage;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PointScaleSteps {
    private Environment environment;
    private DefaultApi api;
    private World world;

    public PointScaleSteps(Environment environment, World world){
        this.environment = environment;
        this.world = world;
        this.api = environment.getApi();
        api.getApiClient().setApiKey(World.getApp().getApiKey());
    }

    @Given("I have a pointscale payload")
    public void iHaveAPointscalePayload() {
        List<Stage> stages = new ArrayList<>();
        stages.add(world.getStage());
        world.setPointScale(new ch.heigvd.amt.gamification.api.dto.PointScale()
                .stages(stages));
    }

    @Given("I have a pointscale payload without stages")
    public void iHaveAPointscalePayloadWithoutStages() {
        world.setPointScale(new ch.heigvd.amt.gamification.api.dto.PointScale());
    }

    @When("I POST the pointscale payload to the /pointscales endpoint$")
    public void iPOSTThePointscalePayloadToThePointscalesEndpoint() {
        try {
            environment.setLastApiResponse(api.createPointScaleWithHttpInfo(world.getPointScale()));
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send DELETE the pointscale id to the \\/pointscales\\/\\{id} endpoint$")
    public void iSendDELETEThePointscaleIdToThePointscalesIdEndpoint() {
        try {
            environment.setLastApiResponse(api.removePointScaleWithHttpInfo(world.getPointScaleInfo().getId()));
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a GET to the /pointscales endpoint$")
    public void iSendAGETToThePointscalesEndpoint() {
        try {
            environment.setLastApiResponse(api.getPointScalesWithHttpInfo());
            environment.processApiResponse(environment.getLastApiResponse());
            List<PointScaleInfo> pointScales = (List<PointScaleInfo>) environment.getLastApiResponse().getData();
            world.setPointScaleInfo(pointScales.get(pointScales.size() - 1));
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a GET to the /pointscales/\\{id} endpoint$")
    public void iSendAGETToThePointscalesIdEndpoint() {
        try {
            System.out.println(world.getPointScaleInfo().getId());
            environment.setLastApiResponse(api.getPointScaleWithHttpInfo(world.getPointScaleInfo().getId()));
            environment.processApiResponse(environment.getLastApiResponse());
            PointScale pointScale = (PointScale) environment.getLastApiResponse().getData();
            world.setLastReceivedPointScale(pointScale);
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @Then("I have a pointscale id")
    public void iHaveAPointscaleId() {
        assertNotEquals((int) world.getPointScaleInfo().getId(), -1);
    }

    @Given("I have an unknown pointscale id")
    public void iHaveAnUnknownPointscaleId() {
        world.setPointScaleInfo(new ch.heigvd.amt.gamification.api.dto.PointScaleInfo()
                .id(-1));
    }

    @And("I receive a payload that is the same as the previous pointscale payload")
    public void iReceiveAPayloadThatIsTheSameAsThePreviousPointscalePayload() {
        assertEquals(world.getPointScale(), world.getLastReceivedPointScale());
    }

    @Then("stages are empty")
    public void stagesAreEmpty() {
        assertEquals(0, world.getLastReceivedPointScale().getStages().size());
    }
}
