package ch.heigvd.amt.gamification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRankingDTO {
    private final String userId;
    private final Double points;
}
