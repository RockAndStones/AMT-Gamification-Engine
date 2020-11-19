package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.BadgesApi;
import ch.heigvd.amt.gamification.api.model.Badge;
import ch.heigvd.amt.gamification.api.model.Event;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.BadgeEntity;
import ch.heigvd.amt.gamification.entities.EventEntity;
import ch.heigvd.amt.gamification.repositories.ApplicationRepository;
import ch.heigvd.amt.gamification.repositories.BadgeRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BadgesApiController implements BadgesApi {

    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createBadge(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = ""  ) @Valid @RequestBody(required = false) Badge badge) {
        ApplicationEntity app = applicationRepository.findByApiKey(xApiKey);
        if (app == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (badge.getName() == null   || badge.getDescription() == null ||
            badge.getName().isEmpty() || badge.getDescription().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        if(badgeRepository.findByNameAndAppApiKey(badge.getName(), xApiKey) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        BadgeEntity newBadgeEntity = toBadgeEntity(badge);
        newBadgeEntity.setApp(app);
        badgeRepository.save(newBadgeEntity);

//
//        URI locationId = ServletUriComponentsBuilder
//                .fromCurrentRequest().path("/{id}")
//                .buildAndExpand(newBadgeEntity.getId()).toUri();

        URI locationName = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{name}")
                .buildAndExpand(newBadgeEntity.getName()).toUri();


        return ResponseEntity.created(locationName).build();
    }

    @Override
    public ResponseEntity<Badge> getBadge(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "",required=true) @PathVariable("name") String name) {
        ApplicationEntity app = applicationRepository.findByApiKey(xApiKey);
        if (app == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        BadgeEntity existingBadgeEntity = badgeRepository.findByNameAndAppApiKey(name, xApiKey);
        if(existingBadgeEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toBadge(existingBadgeEntity));
    }

    public ResponseEntity<List<Badge>> getBadges(@RequestHeader(value = "X-API-KEY") String xApiKey) {
        List<Badge> badges = new ArrayList<>();
        for (BadgeEntity badgeEntity : badgeRepository.findAllByAppApiKey(xApiKey)) {
            badges.add(toBadge(badgeEntity));
        }
        return ResponseEntity.ok(badges);
    }

    @Override
    public ResponseEntity<Void> removeBadge(@ApiParam(value = "Application api key",required = true) @RequestHeader(value = "X-API-KEY",required = true) String X_API_KEY, @ApiParam(value = "",required = true) @PathVariable("name") String name) {
        ApplicationEntity app = applicationRepository.findByApiKey(X_API_KEY);
        if (app == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        BadgeEntity existingBadgeEntity = badgeRepository.findByNameAndAppApiKey(name, X_API_KEY);
        if(existingBadgeEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        badgeRepository.delete(existingBadgeEntity);
        return ResponseEntity.ok().build();
    }

    private BadgeEntity toBadgeEntity(Badge badge) {
        BadgeEntity entity = new BadgeEntity();
        entity.setName(badge.getName());
        entity.setDescription(badge.getDescription());
        return entity;
    }

    private Badge toBadge(BadgeEntity entity) {
        Badge badge = new Badge();
        badge.setName(entity.getName());
        badge.setDescription(entity.getDescription());
        return badge;
    }

}
