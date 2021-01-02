package ch.heigvd.amt.gamification.model;

import ch.heigvd.amt.gamification.api.model.PointsRanking;

public class CustomPointsRanking extends PointsRanking {
    public CustomPointsRanking(String userId, Double points) {
        super.setUserId(userId);
        super.setPoints(points);
    }

    public CustomPointsRanking(String userId, int points) {
        super.setUserId(userId);
        super.setPoints((double)points);
    }
}
