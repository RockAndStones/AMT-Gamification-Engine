package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.api.model.Application;
import ch.heigvd.amt.gamification.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

interface UserRanking {
    String getUserId();
    int getPoints();
}

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Iterable<UserEntity> findAllByAppApiKey(String apiKey);
    UserEntity findByUserAppIdAndAppApiKey(String userAppId, String apiKey);

    @Query(value =
            "SELECT " +
                "ue.userAppId AS userId, " +
                "SUM(pue.points) AS points " +
            "FROM UserEntity AS ue " +
                "JOIN PointsUserEntity AS pue " +
            "WHERE ue.app = :app " +
            "GROUP BY ue.userAppId")
    Iterable<UserRanking> getUserRankingsOfApp(@Param("app") Application app);
}
