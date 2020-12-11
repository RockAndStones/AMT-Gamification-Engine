package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.api.model.BadgesRanking;
import ch.heigvd.amt.gamification.api.model.PointsRanking;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.PointScaleEntity;
import ch.heigvd.amt.gamification.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Iterable<UserEntity> findAllByAppApiKey(String apiKey);
    UserEntity findByUserAppIdAndAppApiKey(String userAppId, String apiKey);

    @Query(value =
            "SELECT " +
                "new ch.heigvd.amt.gamification.model.CustomPointsRanking(ue.userAppId, COALESCE(SUM(pue.points), 0.0)) " +
            "FROM UserEntity AS ue " +
            "LEFT JOIN PointsUserEntity AS pue " +
                "ON ue = pue.user " +
            "WHERE ue.app = :app " +
            "GROUP BY ue.userAppId " +
            "ORDER BY COALESCE(SUM(pue.points), 0) DESC")
    Page<PointsRanking> userRankingsByTotalPoints(@Param("app") ApplicationEntity app, Pageable p);

    @Query(value =
            "SELECT " +
                "new ch.heigvd.amt.gamification.model.CustomPointsRanking(ue.userAppId, COALESCE(SUM(pue.points), 0.0)) " +
            "FROM UserEntity AS ue " +
            "LEFT JOIN PointsUserEntity AS pue " +
                "ON ue = pue.user " +
            "WHERE ue.app = :app AND pue.pointScale = :pointScale " +
            "GROUP BY ue.userAppId " +
            "ORDER BY COALESCE(SUM(pue.points), 0) DESC")
    List<PointsRanking> userRankingsByTotalPoints(@Param("app") ApplicationEntity app, @Param("pointScale") PointScaleEntity pointScale);

    @Query(value =
            "SELECT " +
                "new ch.heigvd.amt.gamification.model.CustomPointsRanking(ue.userAppId, COALESCE(SUM(pue.points), 0.0)) " +
            "FROM UserEntity AS ue " +
            "LEFT JOIN PointsUserEntity AS pue ON ue = pue.user " +
            "LEFT JOIN PointScaleEntity AS pse ON pue.pointScale = pse " +
            "LEFT JOIN RuleEntity AS re ON pse = re.pointScale " +
            "WHERE re.app = :app AND re.eventType = :eventType " +
            "GROUP BY ue.id, ue.userAppId " +
            "ORDER BY COALESCE(SUM(pue.points), 0) DESC")
    List<PointsRanking> userRankingsByTotalPoints(@Param("app") ApplicationEntity app, @Param("eventType") String eventType);

    @Query(value =
            "SELECT " +
                "new ch.heigvd.amt.gamification.model.CustomBadgesRanking(ue.userAppId, SIZE(ue.badges)) " +
            "FROM UserEntity AS ue " +
            "WHERE ue.app = :app " +
            "ORDER BY SIZE(ue.badges)")
    List<BadgesRanking> userRankingsByBadges(@Param("app") ApplicationEntity app);
}
