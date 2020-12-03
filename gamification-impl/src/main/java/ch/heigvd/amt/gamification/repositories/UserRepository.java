package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.api.model.UserRanking;
import ch.heigvd.amt.gamification.dto.UserRankingDTO;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Iterable<UserEntity> findAllByAppApiKey(String apiKey);
    UserEntity findByUserAppIdAndAppApiKey(String userAppId, String apiKey);

    @Query(value =
            "SELECT " +
                "new ch.heigvd.amt.gamification.dto.UserRankingDTO(ue.userAppId, SUM(pue.points)) " +
            "FROM UserEntity AS ue " +
            "INNER JOIN PointsUserEntity AS pue " +
                "ON ue = pue.user " +
            "WHERE ue.app = :app " +
            "GROUP BY ue.userAppId")
    List<UserRankingDTO> userRankingsByTotalPoints(@Param("app") ApplicationEntity app);
}
