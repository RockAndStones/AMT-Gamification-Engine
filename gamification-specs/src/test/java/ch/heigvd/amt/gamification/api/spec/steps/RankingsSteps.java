package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.*;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import ch.heigvd.amt.gamification.api.spec.helpers.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RankingsSteps {
    private Environment environment;
    private DefaultApi api;
    private World world;

    public RankingsSteps(Environment environment, World world) {
        this.environment = environment;
        this.world = world;
        this.api = environment.getApi();
        api.getApiClient().setApiKey(World.getApp().getApiKey());
    }


    @When("^I send a GET to the /rankings/byBadges endpoint$")
    public void iSendAGETToTheRankingsByBadgesEndpoint() {
        try {
            environment.setLastApiResponse(api.getRankingsByTotalBadgesWithHttpInfo(0, 20));
            environment.processApiResponse(environment.getLastApiResponse());
            PaginatedBadgesRankings badgesRankings = (PaginatedBadgesRankings)environment.getLastApiResponse().getData();
            world.setBadgesRankings(badgesRankings);
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("^I send a GET to the /rankings/byPoints endpoint$")
    public void iSendAGETToTheRankingsByPointsEndpoint() {
        try {
            environment.setLastApiResponse(api.getRankingsByTotalPointsWithHttpInfo(0, 20));
            environment.processApiResponse(environment.getLastApiResponse());
            PaginatedPointsRankings pointsRankings = (PaginatedPointsRankings)environment.getLastApiResponse().getData();
            world.setPointsRankings(pointsRankings);
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @Then("I have a paginated badges rankings")
    public void iHaveAPaginatedBadgesRankings() {
        assertNotNull(world.getBadgesRankings().getData());
        assertNotEquals(world.getBadgesRankings().getData().size(), 0);
    }

    @Then("I have a paginated points rankings")
    public void iHaveAPaginatedPointsRankings() {
        assertNotNull(world.getPointsRankings().getData());
        assertNotEquals(world.getPointsRankings().getData().size(), 0);
    }
}
