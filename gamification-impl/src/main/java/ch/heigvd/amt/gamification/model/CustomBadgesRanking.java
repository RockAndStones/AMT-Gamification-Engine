package ch.heigvd.amt.gamification.model;

import ch.heigvd.amt.gamification.api.model.BadgesRanking;

public class CustomBadgesRanking extends BadgesRanking {
    public CustomBadgesRanking(String userId, int points) {
        super.setUserId(userId);
        super.setBadges(points);
    }
}
