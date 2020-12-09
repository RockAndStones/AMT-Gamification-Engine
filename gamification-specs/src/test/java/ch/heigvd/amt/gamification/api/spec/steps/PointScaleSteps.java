package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.PointScale;
import ch.heigvd.amt.gamification.api.dto.Stage;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class PointScaleSteps {
    private Environment environment;
    private DefaultApi api;
    private World world;
    private int id = -1;

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
            environment.setLastApiResponse(api.removePointScaleWithHttpInfo(id));
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I GET the pointscale payload from the \\/pointscales endpoint$")
    public void iGETThePointscalePayloadFromThePointscalesEndpoint() {
        try {
            environment.setLastApiResponse(api.getPointScalesWithHttpInfo());
            environment.processApiResponse(environment.getLastApiResponse());
            List<PointScale> pointScales = (List<PointScale>) environment.getLastApiResponse().getData();
            id = pointScales.size();
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @Given("I have a pointscale id")
    public void iHaveAPointscaleId() {
        assertNotEquals(id, -1);
    }
}
