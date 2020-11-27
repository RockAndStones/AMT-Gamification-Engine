package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.ApplicationsApi;
import ch.heigvd.amt.gamification.api.model.Application;
import ch.heigvd.amt.gamification.api.model.InlineObject;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.repositories.ApplicationRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Controller
public class ApplicationsApiController implements ApplicationsApi {
    @Autowired
    ApplicationRepository applicationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Application> createApplication(@ApiParam(value = "", required = true) @Valid @RequestBody InlineObject inlineObject) {
        // todo: check conditions (unique name eventually, user authentication)
        ApplicationEntity appEntity = new ApplicationEntity();
        appEntity.setApiKey(ApplicationEntity.generateApiKey());
        appEntity.setName(inlineObject.getName());
        applicationRepository.save(appEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{name}")
                .buildAndExpand(appEntity.getName()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Application> getApplication(@ApiParam(required = true) @PathVariable("name") String name) {
        ApplicationEntity appEntity = applicationRepository.findByName(name);
        if (appEntity == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Application app = new Application();
        app.setName(appEntity.getName());
        app.setApiKey(appEntity.getApiKey());

        return ResponseEntity.ok(app);
    }
}
