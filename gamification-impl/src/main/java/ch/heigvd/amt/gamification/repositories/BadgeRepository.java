package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.entities.BadgeEntity;
import org.springframework.data.repository.CrudRepository;

public interface BadgeRepository extends CrudRepository<BadgeEntity, Long> {
    Iterable<BadgeEntity> findAllByAppApiKey(String apiKey);
    BadgeEntity findByNameAndAppApiKey(String name, String apiKey);
}
