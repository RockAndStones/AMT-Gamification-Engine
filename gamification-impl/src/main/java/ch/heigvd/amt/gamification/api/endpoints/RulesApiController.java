package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.RulesApi;
import ch.heigvd.amt.gamification.api.model.Badge;
import ch.heigvd.amt.gamification.api.model.Rule;
import ch.heigvd.amt.gamification.api.model.RuleInfo;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.BadgeEntity;
import ch.heigvd.amt.gamification.entities.PointScaleEntity;
import ch.heigvd.amt.gamification.entities.RuleEntity;
import ch.heigvd.amt.gamification.repositories.ApplicationRepository;
import ch.heigvd.amt.gamification.repositories.BadgeRepository;
import ch.heigvd.amt.gamification.repositories.PointScaleRepository;
import ch.heigvd.amt.gamification.repositories.RuleRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RulesApiController implements RulesApi {

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    PointScaleRepository pointScaleRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ServletRequest request;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Rule> createRule(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Rule rule) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        if(isNullOrEmpty(rule.getName()) || isNullOrEmpty(rule.getDescription()) ||
                isNullOrEmpty(rule.getEventType()) || rule.getPointsToAdd() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        PointScaleEntity pointScaleEntity = pointScaleRepository.findByIdAndAppApiKey(rule.getPointScaleId(), app.getApiKey());

        if(pointScaleEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        BadgeEntity badgeEntity = null;
        if(rule.getBadgeName() != null) {
            badgeEntity = badgeRepository.findByNameAndAppApiKey(rule.getBadgeName(), app.getApiKey());
            if (badgeEntity == null ||
                !badgeEntity.getUsable()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        // If a rule has the same apikey, same event type and same point scale id we refuse the creation
        // Because this could become a big problem and rules could become unmanageable
        if(ruleRepository.findByPointScaleIdAndEventTypeAndAppApiKey(rule.getPointScaleId(), rule.getEventType(), app.getApiKey()) != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        RuleEntity newRuleEntity = toRuleEntity(rule);
        newRuleEntity.setApp(app);
        newRuleEntity.setPointScale(pointScaleEntity);
        if(badgeEntity != null){
            newRuleEntity.setBadge(badgeEntity);
        }
        ruleRepository.save(newRuleEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newRuleEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Void> removeRule(@ApiParam(value = "",required = true) @PathVariable("id") Integer id) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        RuleEntity existingRuleEntity = ruleRepository.findByIdAndAppApiKey(id, app.getApiKey());
        if(existingRuleEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ruleRepository.delete(existingRuleEntity);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Rule> getRule(@ApiParam(value = "",required = true) @PathVariable("id") Integer id) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        RuleEntity existingRuleEntity = ruleRepository.findByIdAndAppApiKey(id, app.getApiKey());
        if(existingRuleEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toRule(existingRuleEntity));
    }

    public ResponseEntity<List<RuleInfo>> getRules(){
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");
        List<RuleInfo> rules = new ArrayList<>();
        for (RuleEntity ruleEntity : ruleRepository.findAllByAppApiKey(app.getApiKey())) {
            rules.add(toRuleInfo(ruleEntity));
        }
        return ResponseEntity.ok(rules);
    }

    private RuleEntity toRuleEntity(Rule rule) {
        RuleEntity entity = new RuleEntity();
        entity.setName(rule.getName());
        entity.setDescription(rule.getDescription());
        entity.setEventType(rule.getEventType());
        entity.setPointsToAdd(rule.getPointsToAdd());
        return entity;
    }

    private Rule toRule(RuleEntity entity) {
        Rule rule = new Rule();
        rule.setName(entity.getName());
        rule.setDescription(entity.getDescription());
        rule.setEventType(entity.getEventType());
        rule.setPointsToAdd(entity.getPointsToAdd());
        if(entity.getBadge() != null) {
            rule.setBadgeName(entity.getBadge().getName());
        } else {
            rule.setBadgeName("");
        }
        rule.setPointsToAdd(entity.getPointsToAdd());
        rule.setPointScaleId((int)entity.getPointScale().getId());
        return rule;
    }

    private RuleInfo toRuleInfo(RuleEntity entity) {
        RuleInfo rule = new RuleInfo();
        rule.setId((int) entity.getId());
        rule.setName(entity.getName());
        rule.setDescription(entity.getDescription());
        rule.setEventType(entity.getEventType());
        rule.setPointsToAdd(entity.getPointsToAdd());
        if(entity.getBadge() != null) {
            rule.setBadgeName(entity.getBadge().getName());
        } else {
            rule.setBadgeName("");
        }
        rule.setPointsToAdd(entity.getPointsToAdd());
        rule.setPointScaleId((int)entity.getPointScale().getId());
        return rule;
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
