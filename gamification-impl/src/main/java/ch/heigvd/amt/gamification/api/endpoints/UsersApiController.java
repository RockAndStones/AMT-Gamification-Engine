package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.UsersApi;
import ch.heigvd.amt.gamification.api.model.Badge;
import ch.heigvd.amt.gamification.api.model.User;
import ch.heigvd.amt.gamification.api.model.UserInfo;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.BadgeEntity;
import ch.heigvd.amt.gamification.entities.UserEntity;
import ch.heigvd.amt.gamification.repositories.PointsUserRepository;
import ch.heigvd.amt.gamification.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UsersApiController implements UsersApi {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PointsUserRepository pointsUserRepository;

    @Autowired
    ServletRequest request;

    @Override
    public ResponseEntity<UserInfo> getUser(@ApiParam(value = "", required=true) @PathVariable("userAppId") String userAppId) {

        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        UserEntity existingUserEntity = userRepository.findByUserAppIdAndAppApiKey(userAppId, app.getApiKey());
        if (existingUserEntity == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(toUserInfo(existingUserEntity));
    }

    public ResponseEntity<List<User>> getUsers() {

        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAllByAppApiKey(app.getApiKey())) {
            users.add(toUser(userEntity));
        }
        return ResponseEntity.ok(users);
    }

    private User toUser(UserEntity entity) {
        User user = new User();
        user.setUserAppId(entity.getUserAppId());
        List<Badge> badges = new ArrayList<>();
        for (BadgeEntity badgeEntity : entity.getBadges()) {
            badges.add(toBadge(badgeEntity));
        }
        user.setBadges(badges);
        Double points = pointsUserRepository.sumPointsByUser(entity);
        if(points == null){
            user.setPoints(0);
        } else {
            user.setPoints(points.intValue());
        }
        return user;
    }

    private UserInfo toUserInfo(UserEntity entity) {
        UserInfo user = new UserInfo();
        List<Badge> badges = new ArrayList<>();
        for (BadgeEntity badgeEntity : entity.getBadges()) {
            badges.add(toBadge(badgeEntity));
        }
        user.setBadges(badges);
        Double points = pointsUserRepository.sumPointsByUser(entity);
        if(points == null){
            user.setPoints(0);
        } else {
            user.setPoints(points.intValue());
        }
        return user;
    }

    private Badge toBadge(BadgeEntity entity) {
        Badge badge = new Badge();
        badge.setName(entity.getName());
        badge.setDescription(entity.getDescription());
        return badge;
    }

}
