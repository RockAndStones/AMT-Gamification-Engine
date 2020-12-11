package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.entities.PointsUserEntity;
import ch.heigvd.amt.gamification.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PointsUserRepository extends CrudRepository<PointsUserEntity, Long> {
    PointsUserEntity findPointsByUserIdAndPointScaleId(long userId, long pointScaleId);

    @Query(value = "SELECT SUM(pue.points) FROM PointsUserEntity AS pue WHERE pue.user=:user")
    Double sumPointsByUser(@Param("user") UserEntity u);

    void deleteAllByPointScaleId(long pointScaleId);
}
