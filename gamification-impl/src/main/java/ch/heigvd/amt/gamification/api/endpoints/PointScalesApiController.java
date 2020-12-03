package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.PointscalesApi;
import ch.heigvd.amt.gamification.api.model.*;
import ch.heigvd.amt.gamification.entities.*;
import ch.heigvd.amt.gamification.repositories.ApplicationRepository;
import ch.heigvd.amt.gamification.repositories.BadgeRepository;
import ch.heigvd.amt.gamification.repositories.PointScaleRepository;
import ch.heigvd.amt.gamification.repositories.StageRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PointScalesApiController implements PointscalesApi {

    @Autowired
    PointScaleRepository pointScaleRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ServletRequest request;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createPointScale(@ApiParam(value = "" ,required=true )  @Valid @RequestBody PointScale pointScale) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        if(pointScale.getStages() == null || pointScale.getStages().size() <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        for (Stage stage: pointScale.getStages()) {
            BadgeEntity badge = badgeRepository.findByNameAndAppApiKey(stage.getBadge().getName(), app.getApiKey());
            if(stage.getPoints() < 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else if(stage.getBadge() == null || badge == null
            || !badge.getUsable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }

        PointScaleEntity newPointScaleEntity = toPointScaleEntity(pointScale, app.getApiKey());
        newPointScaleEntity.setApp(app);
        pointScaleRepository.save(newPointScaleEntity);

        for (Stage newStage : pointScale.getStages()) {
            StageEntity newStageEntity = toStageEntity(newStage, app.getApiKey());
            newStageEntity.setApp(app);
            newStageEntity.setPointScale(newPointScaleEntity);
            stageRepository.save(newStageEntity);
        }

        //TODO Que faut-il retourner ??
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newPointScaleEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<List<PointScale>> getPointScales() {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        List<PointScale> pointScales = new ArrayList<>();
        for (PointScaleEntity pointScaleEntity : pointScaleRepository.findAllByAppApiKey(app.getApiKey())) {
            pointScales.add(toPointScale(pointScaleEntity));
        }

        return ResponseEntity.ok(pointScales);
    }

    private PointScale toPointScale(PointScaleEntity pointScaleEntity){
        PointScale pointScale = new PointScale();
        List<Stage> stages = new ArrayList<>();
        for (StageEntity stageEntity : stageRepository.findAllByPointScaleId(pointScaleEntity.getId())) {
            stages.add(toStage(stageEntity));
        }
        pointScale.setStages(stages);

        return pointScale;
    }

    private Stage toStage(StageEntity stageEntity){
        Stage stage = new Stage();
        stage.setPoints(stageEntity.getPoints());
        stage.setBadge(toBadge(stageEntity.getBadge()));

        return stage;
    }

    private Badge toBadge(BadgeEntity entity) {
        Badge badge = new Badge();
        badge.setName(entity.getName());
        badge.setDescription(entity.getDescription());

        return badge;
    }

    private PointScaleEntity toPointScaleEntity(PointScale pointScale, String apiKey) {
        return new PointScaleEntity();
    }

    private StageEntity toStageEntity(Stage stage, String apiKey){
        StageEntity stageEntity = new StageEntity();
        stageEntity.setPoints(stage.getPoints());
        stageEntity.setBadge(badgeRepository.findByNameAndAppApiKey(stage.getBadge().getName(), apiKey));

        return stageEntity;
    }
}
