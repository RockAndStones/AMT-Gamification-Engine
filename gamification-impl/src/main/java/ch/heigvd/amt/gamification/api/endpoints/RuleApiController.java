package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.RulesApi;
import ch.heigvd.amt.gamification.api.model.Rule;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.RuleEntity;
import ch.heigvd.amt.gamification.repositories.ApplicationRepository;
import ch.heigvd.amt.gamification.repositories.BadgeRepository;
import ch.heigvd.amt.gamification.repositories.RuleRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;

@Controller
public class RuleApiController implements RulesApi {

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ServletRequest request;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Rule> createRule(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Rule rule) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        if(isNullOrEmpty(rule.getName()) || isNullOrEmpty(rule.getDescription()) ||
                isNullOrEmpty(rule.getEventType()) || rule.getPointsToAdd() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(rule.getBadgeName() != null) {
            if (badgeRepository.findByNameAndAppApiKey(rule.getBadgeName(), app.getApiKey()) == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }

        RuleEntity newRuleEntity = toRuleEntity(rule);
        newRuleEntity.setApp(app);
        ruleRepository.save(newRuleEntity);

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

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
