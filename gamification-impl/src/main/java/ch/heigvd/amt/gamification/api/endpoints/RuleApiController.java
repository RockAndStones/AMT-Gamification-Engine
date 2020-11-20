package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.RulesApi;
import ch.heigvd.amt.gamification.api.model.Rule;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.RuleEntity;
import ch.heigvd.amt.gamification.repositories.ApplicationRepository;
import ch.heigvd.amt.gamification.repositories.RuleRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

public class RuleApiController implements RulesApi {

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Rule> newRule(@ApiParam(value = "Application api key",required = true) @RequestHeader(value = "X-API-KEY",required = true) String X_API_KEY, @ApiParam("") @Valid @RequestBody(required = false) Rule rule) {
        ApplicationEntity app = applicationRepository.findByApiKey(X_API_KEY);
        if (app == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        RuleEntity newRuleEntity = toRuleEntity(rule);
        newRuleEntity.setApp(app);
        ruleRepository.save(newRuleEntity);
        Long id = newRuleEntity.getId();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newRuleEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    private RuleEntity toRuleEntity(Rule rule) {
        RuleEntity entity = new RuleEntity();
        entity.setName(rule.getName());
        entity.setDescription(rule.getDescription());
        entity.setEventType(rule.getEventType());
        entity.setPointsToAdd(rule.getPointsToAdd());
        if (rule.getBadgeName() != null){
            entity.setBadgeName(rule.getBadgeName());
        } else {
            entity.setBadgeName("");
        }
        if(rule.getPointToReach() != null){
            entity.setPointToReach(rule.getPointToReach());
        } else {
            entity.setPointToReach(0.0);
        }
        return entity;
    }

    private Rule toRule(RuleEntity entity) {
        Rule rule = new Rule();
        rule.setName(entity.getName());
        rule.setDescription(entity.getDescription());
        rule.setEventType(entity.getEventType());
        rule.setPointsToAdd(entity.getPointsToAdd());
        rule.setBadgeName(entity.getBadgeName());
        rule.setPointsToAdd(entity.getPointsToAdd());
        return rule;
    }
}
