package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.Given;

public class StageSteps {
    private Environment environment;
    private DefaultApi api;
    private World world;

    public StageSteps(Environment environment, World world){
        this.environment = environment;
        this.world = world;
        this.api = environment.getApi();
        api.getApiClient().setApiKey(World.getApp().getApiKey());
    }

    @Given("I have a stage payload")
    public void iHaveAStagePayload() {
        world.setStage(new ch.heigvd.amt.gamification.api.dto.Stage()
                .badge(world.getBadge())
                .points(10.0));
    }

    @Given("I have a stage payload with an unknown badge")
    public void iHaveAStagePayloadWithAnUnknownBadge() {
        world.setStage(new ch.heigvd.amt.gamification.api.dto.Stage()
                .badge(world.getBadge())
                .points(10.0));
    }

    @Given("I have a stage payload with negative points")
    public void iHaveAStagePayloadWithNegativePoints() {
        world.setStage(new ch.heigvd.amt.gamification.api.dto.Stage()
                .badge(world.getBadge())
                .points(-10.0));
    }
}
