package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.Application;
import ch.heigvd.amt.gamification.api.dto.InlineObject;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ApplicationSteps {
    private Environment environment;
    private DefaultApi api;
    private World world;

    public ApplicationSteps(Environment environment, World world){
        this.environment = environment;
        this.world = world;
        this.api = environment.getApi();
    }

    @Given("there is an Application server")
    public void thereIsAApplicationServer() throws Throwable {
        assertNotNull(api);
    }

    @Given("I have an application payload")
    public void iHaveAnApplicationPayload() {
        world.setObj(new InlineObject().name("MyTestApp2"));
    }

    @When("I POST the application payload to the /application endpoint$")
    public void iPOSTTheApplicationPayloadToTheApplicationEndpoint() {
        try {
            environment.setLastApiResponseApp(api.newApplicationWithHttpInfo(world.getObj()));
            environment.processApiResponse(environment.getLastApiResponseApp());
            World.setApp((Application) environment.getLastApiResponseApp().getData());
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @Then("I receive a {int} status code")
    public void i_receive_a_status_code(int expectedStatusCode) throws Throwable {
        assertEquals(expectedStatusCode, environment.getLastStatusCode());
    }
}
