package ch.heigvd.amt.gamification.services;

import ch.heigvd.amt.gamification.api.model.Application;
import ch.heigvd.amt.gamification.api.model.Event;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.EventEntity;
import ch.heigvd.amt.gamification.entities.RuleEntity;
import ch.heigvd.amt.gamification.entities.UserEntity;
import ch.heigvd.amt.gamification.repositories.BadgeRepository;
import ch.heigvd.amt.gamification.repositories.RuleRepository;
import ch.heigvd.amt.gamification.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProcessor {
    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BadgeRepository badgeRepository;

    public void processEvent(ApplicationEntity app, EventEntity event){
        UserEntity user = userRepository.findByUserAppIdAndAppApiKey(event.getUserAppId(), app.getApiKey());
        for(RuleEntity r : ruleRepository.findAllByAppApiKey(app.getApiKey())){
            if (r.getEventType().equals(event.getEventType())){
                if(!r.getBadgeName().isEmpty()){
                    double currentPoints = user.getPoints();
                    currentPoints = currentPoints + r.getPointsToAdd();
                    user.setPoints(currentPoints);
                    user.getBadges().add(badgeRepository.findByNameAndAppApiKey(r.getBadgeName(), app.getApiKey()));
                } else {
                    double currentPoints = user.getPoints();
                    currentPoints = currentPoints + r.getPointsToAdd();
                    user.setPoints(currentPoints);
                }
            }
        }
    }
}
