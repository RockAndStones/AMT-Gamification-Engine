package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.UsersApi;
import ch.heigvd.amt.gamification.repositories.ApplicationRepository;
import ch.heigvd.amt.gamification.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UsersApiController implements UsersApi {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplicationRepository applicationRepository;


}
