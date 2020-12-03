package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.Stage;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

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

    @When("I POST the pointscale payload to the /pointscales endpoint$")
    public void iPOSTThePointscalePayloadToThePointscalesEndpoint() {
        try {
            environment.setLastApiResponse(api.createPointScaleWithHttpInfo(world.getPointScale()));
            environment.processApiResponse(environment.getLastApiResponse());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }
}
