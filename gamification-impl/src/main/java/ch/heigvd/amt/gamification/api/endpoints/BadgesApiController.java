package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.BadgesApi;
import ch.heigvd.amt.gamification.api.model.Badge;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.BadgeEntity;
import ch.heigvd.amt.gamification.repositories.BadgeRepository;
import ch.heigvd.amt.gamification.repositories.RuleRepository;
import ch.heigvd.amt.gamification.repositories.StageRepository;
import ch.heigvd.amt.gamification.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BadgesApiController implements BadgesApi {

    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServletRequest request;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createBadge(@ApiParam("") @Valid @RequestBody(required = false) Badge badge) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        if (badge.getName() == null   || badge.getDescription() == null ||
            badge.getName().isEmpty() || badge.getDescription().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        if(badgeRepository.findByNameAndAppApiKey(badge.getName(), app.getApiKey()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        BadgeEntity newBadgeEntity = toBadgeEntity(badge);
        newBadgeEntity.setApp(app);
        badgeRepository.save(newBadgeEntity);

        URI locationName = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{name}")
                .buildAndExpand(newBadgeEntity.getName()).toUri();


        return ResponseEntity.created(locationName).build();
    }

    @Override
    public ResponseEntity<Badge> getBadge(@ApiParam(value = "",required=true) @PathVariable("name") String name) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        BadgeEntity existingBadgeEntity = badgeRepository.findByNameAndAppApiKey(name, app.getApiKey());
        if(existingBadgeEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toBadge(existingBadgeEntity));
    }

    public ResponseEntity<List<Badge>> getBadges() {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");
        List<Badge> badges = new ArrayList<>();
        for (BadgeEntity badgeEntity : badgeRepository.findAllByAppApiKey(app.getApiKey())) {
            badges.add(toBadge(badgeEntity));
        }
        return ResponseEntity.ok(badges);
    }

    @Override
    public ResponseEntity<Badge> putBadge(@ApiParam(value = "",required = true) @PathVariable("name") String name,
                                          @ApiParam(value = "If the badge has to be desactivated pass this value with false",defaultValue = "true") @Valid @RequestParam(value = "usable",required = false,defaultValue = "true") Boolean usable,
                                          @ApiParam("") @Valid @RequestBody(required = false) Badge badge) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");
        // TODO Better solution ?
        if(badge == null || badge.getName() == null || badge.getDescription() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        BadgeEntity existingBadgeEntity = badgeRepository.findByNameAndAppApiKey(name, app.getApiKey());
        if(existingBadgeEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if(!name.equals(badge.getName())) {
            if (badgeRepository.findByNameAndAppApiKey(badge.getName(), app.getApiKey()) != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
            existingBadgeEntity.setName(badge.getName());
        }
        existingBadgeEntity.setDescription(badge.getDescription());
        existingBadgeEntity.setUsable(usable);
        badgeRepository.save(existingBadgeEntity);

        return ResponseEntity.ok(toBadge(existingBadgeEntity));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> removeBadge(@ApiParam(value = "",required=true) @PathVariable("name") String name) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        BadgeEntity existingBadgeEntity = badgeRepository.findByNameAndAppApiKey(name, app.getApiKey());
        if(existingBadgeEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ruleRepository.deleteByBadgeId(existingBadgeEntity.getId());
        stageRepository.deleteByBadgeId(existingBadgeEntity.getId());
        badgeRepository.delete(existingBadgeEntity);
        return ResponseEntity.ok().build();
    }

    private BadgeEntity toBadgeEntity(Badge badge) {
        BadgeEntity entity = new BadgeEntity();
        entity.setName(badge.getName());
        entity.setDescription(badge.getDescription());
        entity.setUsable(true);
        return entity;
    }

    private Badge toBadge(BadgeEntity entity) {
        Badge badge = new Badge();
        badge.setName(entity.getName());
        badge.setDescription(entity.getDescription());
        return badge;
    }

}
