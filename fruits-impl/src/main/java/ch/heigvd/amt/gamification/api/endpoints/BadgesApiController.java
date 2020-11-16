package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.BadgesApi;
import ch.heigvd.amt.gamification.api.model.Badge;
import ch.heigvd.amt.gamification.api.model.Event;
import ch.heigvd.amt.gamification.entities.BadgeEntity;
import ch.heigvd.amt.gamification.entities.EventEntity;
import ch.heigvd.amt.gamification.repositories.BadgeRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BadgesApiController implements BadgesApi {

    @Autowired
    BadgeRepository badgeRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createBadge(@ApiParam(value = ""  )  @Valid @RequestBody(required = false) Badge badge) {
        // TODO : verify
        BadgeEntity newBadgeEntity = toBadgeEntity(badge);
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
    public ResponseEntity<Badge> getBadge(@ApiParam(value = "",required=true) @PathVariable("name") String name) {
        BadgeEntity existingBadgeEntity = badgeRepository.findByName(name);
        if(existingBadgeEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toBadge(existingBadgeEntity));
    }

    public ResponseEntity<List<Badge>> getBadges() {
        List<Badge> badges = new ArrayList<>();
        for (BadgeEntity badgeEntity : badgeRepository.findAll()) {
            badges.add(toBadge(badgeEntity));
        }
        return ResponseEntity.ok(badges);
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
