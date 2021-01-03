package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.entities.PointScaleEntity;
import org.springframework.data.repository.CrudRepository;

public interface PointScaleRepository extends CrudRepository<PointScaleEntity, Long> {
    Iterable<PointScaleEntity> findAllByAppApiKey(String apiKey);
    PointScaleEntity findByIdAndAppApiKey(long id, String apiKey);
    PointScaleEntity findByNameAndAppApiKey(String name, String apiKey);
}
