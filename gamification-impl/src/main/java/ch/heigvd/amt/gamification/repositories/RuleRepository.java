package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.entities.RuleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RuleRepository extends CrudRepository<RuleEntity, Long> {
    Iterable<RuleEntity> findAllByAppApiKey(String apiKey);
    RuleEntity findByIdAndAppApiKey(long id, String apiKey);
    RuleEntity findByPointScaleIdAndEventTypeAndAppApiKey(long id, String eventType, String apiKey);
    void deleteAllByPointScaleId(long pointScaleId);
}
