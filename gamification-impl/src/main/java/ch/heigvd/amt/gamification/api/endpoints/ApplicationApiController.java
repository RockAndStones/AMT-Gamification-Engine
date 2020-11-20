package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.ApplicationApi;
import ch.heigvd.amt.gamification.api.model.Application;
import ch.heigvd.amt.gamification.api.model.InlineObject;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.repositories.ApplicationRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

@Controller
public class ApplicationApiController implements ApplicationApi {
    @Autowired
    ApplicationRepository applicationRepository;

    public ResponseEntity<Application> newApplication(@ApiParam(value = "", required = true) @Valid @RequestBody InlineObject inlineObject) {
        // todo: check conditions (unique name eventually, user authentication)
        ApplicationEntity appEntity = new ApplicationEntity();
        appEntity.setApiKey(ApplicationEntity.generateApiKey());
        appEntity.setName(inlineObject.getName());
        applicationRepository.save(appEntity);

        Application app = new Application();
        app.setName(appEntity.getName());
        app.setApiKey(appEntity.getApiKey());
        return ResponseEntity.ok(app);
    }
}
