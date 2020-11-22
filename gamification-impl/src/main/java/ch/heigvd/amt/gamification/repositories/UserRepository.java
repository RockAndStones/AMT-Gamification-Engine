package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.entities.EventEntity;
import ch.heigvd.amt.gamification.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<EventEntity, Long> {
    Iterable<UserEntity> findAllByAppApiKey(String apiKey);
    UserEntity findByIdAndAppApiKey(String id, String ApiKey);

}
