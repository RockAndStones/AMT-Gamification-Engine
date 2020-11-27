package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.entities.PointsUserEntity;
import org.springframework.data.repository.CrudRepository;

public interface PointsUserRepository extends CrudRepository<PointsUserEntity, Long> {
    PointsUserEntity findPointsByUserIdAndPointScaleId(long userId, long pointScaleId);
}
