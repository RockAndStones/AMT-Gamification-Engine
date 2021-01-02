package ch.heigvd.amt.gamification.services;

import ch.heigvd.amt.gamification.entities.*;
import ch.heigvd.amt.gamification.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProcessor {
    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointsUserRepository pointsUserRepository;

    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    PointsHistoryRepository pointsHistoryRepository;

    public void processEvent(ApplicationEntity app, EventEntity event){
        UserEntity user = userRepository.findByUserAppIdAndAppApiKey(event.getUserAppId(), app.getApiKey());
        for(RuleEntity r : ruleRepository.findAllByAppApiKey(app.getApiKey())){
            if (r.getEventType().equals(event.getEventType())){
                // Verify that a badge is won
                if (r.getBadge() != null) {
                    BadgeEntity badge = badgeRepository.findByNameAndAppApiKey(r.getBadge().getName(), app.getApiKey());
                    if (!user.getBadges().contains(badge) && badge.getUsable()) {
                        user.getBadges().add(badgeRepository.findByNameAndAppApiKey(r.getBadge().getName(), app.getApiKey()));
                    }
                }

                // Add the points to the right pointScale
                PointsUserEntity pointsUserEntity = addPointsToPointScale(user, r);
                // To keep the history of points we add it to the repository
                addPointsToHistory(event, r);

                // Add badges gained in the pointScale a user can win multiple stage at once
                for(StageEntity stage : stageRepository.findAllByPointScaleId(r.getPointScale().getId())){
                    if(stage.getPoints() <= pointsUserEntity.getPoints()){
                        if(!user.getBadges().contains(stage.getBadge()) && stage.getBadge().getUsable()) {
                            user.getBadges().add(stage.getBadge());
                        }
                    }
                }
                userRepository.save(user);
            }
        }
    }

    private void addPointsToHistory(EventEntity event, RuleEntity r) {
        PointsHistoryEntity pointsHistory = new PointsHistoryEntity();
        pointsHistory.setEvent(event);
        pointsHistory.setPointScale(r.getPointScale());
        pointsHistory.setNbPoints(r.getPointsToAdd());
        pointsHistoryRepository.save(pointsHistory);
    }

    private PointsUserEntity addPointsToPointScale(UserEntity user, RuleEntity r) {
        PointsUserEntity pointsUserEntity = pointsUserRepository.findPointsByUserIdAndPointScaleId(user.getId(), r.getPointScale().getId());
        if(pointsUserEntity == null){
            pointsUserEntity = new PointsUserEntity();
            pointsUserEntity.setPoints(0.0);
            pointsUserEntity.setPointScale(r.getPointScale());
            pointsUserEntity.setUser(user);
            pointsUserRepository.save(pointsUserEntity);
        }
        double currentPoints = pointsUserEntity.getPoints() + r.getPointsToAdd();
        pointsUserEntity.setPoints(currentPoints);
        pointsUserRepository.save(pointsUserEntity);
        return pointsUserEntity;
    }
}
