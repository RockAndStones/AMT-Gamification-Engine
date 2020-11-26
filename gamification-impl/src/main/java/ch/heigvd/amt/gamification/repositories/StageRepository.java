package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.entities.StageEntity;
import org.springframework.data.repository.CrudRepository;

public interface StageRepository extends CrudRepository<StageEntity, Long> {
    StageEntity findByIdAndAppApiKey(long id, String apiKey);
}
