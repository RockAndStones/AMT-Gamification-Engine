package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.entities.PointsUserEntity;
import ch.heigvd.amt.gamification.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PointsUserRepository extends CrudRepository<PointsUserEntity, Long> {
    PointsUserEntity findPointsByUserIdAndPointScaleId(long userId, long pointScaleId);

    @Query("SELECT SUM(points) FROM PointsUserEntity WHERE user=:user")
    double sumPointsByUser(@Param("user") UserEntity u);
}
