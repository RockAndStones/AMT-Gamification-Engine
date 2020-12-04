package ch.heigvd.amt.gamification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRankingDTO {
    private final String userId;
    private final Double points;

    public UserRankingDTO(String userId, int points) {
        this.userId = userId;
        this.points = (double)points;
    }
}
