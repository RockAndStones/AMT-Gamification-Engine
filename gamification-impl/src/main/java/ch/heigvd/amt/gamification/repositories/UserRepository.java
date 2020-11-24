package ch.heigvd.amt.gamification.repositories;

import ch.heigvd.amt.gamification.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Iterable<UserEntity> findAllByAppApiKey(String apiKey);
    UserEntity findByUserAppIdAndAppApiKey(String userAppId, String apiKey);
}
