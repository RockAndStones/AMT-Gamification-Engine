package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.entities.PointScaleEntity;
import ch.heigvd.amt.gamification.entities.PointsHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PointsHistoryRepository extends CrudRepository<PointsHistoryEntity, Long> {
    @Query(value =
            "SELECT PointsHistoryEntity " +
            "FROM PointsHistoryEntity " +
            "WHERE event.userAppId = :user")
    List<PointsHistoryEntity> findAllByUser(String user);

    @Query(value =
            "SELECT PointsHistoryEntity " +
            "FROM PointsHistoryEntity " +
            "WHERE event.userAppId = :user AND pointScale = :pointScale")
    List<PointsHistoryEntity> findAllByUserAndPointScale(String user, PointScaleEntity pointScale);
}
