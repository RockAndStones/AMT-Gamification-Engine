package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.entities.PointScaleEntity;
import ch.heigvd.amt.gamification.entities.PointsHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PointsHistoryRepository extends CrudRepository<PointsHistoryEntity, Long> {
    @Query(value =
            "SELECT phe " +
            "FROM PointsHistoryEntity AS phe " +
            "WHERE phe.event.userAppId = :user")
    List<PointsHistoryEntity> findAllByUser(String user);

    @Query(value =
            "SELECT phe " +
            "FROM PointsHistoryEntity AS phe " +
            "WHERE phe.event.userAppId = :user AND phe.pointScale = :pointScale")
    List<PointsHistoryEntity> findAllByUserAndPointScale(String user, PointScaleEntity pointScale);
}
