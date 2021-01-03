package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.PointscalesApi;
import ch.heigvd.amt.gamification.api.model.*;
import ch.heigvd.amt.gamification.entities.*;
import ch.heigvd.amt.gamification.repositories.*;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
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
    PointsUserRepository pointsUserRepository;

    @Autowired
    PointsHistoryRepository pointsHistoryRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    ServletRequest request;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createPointScale(@ApiParam(value = "" ,required=true )  @Valid @RequestBody PointScale pointScale) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        if(pointScale.getStages() == null || pointScale.getStages().size() <= 0 || pointScale.getName() == null
           ||pointScale.getName().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(pointScaleRepository.findByNameAndAppApiKey(pointScale.getName(), app.getApiKey()) != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
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

        PointScaleEntity newPointScaleEntity = toPointScaleEntity(pointScale);
        newPointScaleEntity.setApp(app);
        pointScaleRepository.save(newPointScaleEntity);

        for (Stage newStage : pointScale.getStages()) {
            StageEntity newStageEntity = toStageEntity(newStage, app.getApiKey());
            newStageEntity.setApp(app);
            newStageEntity.setPointScale(newPointScaleEntity);
            stageRepository.save(newStageEntity);
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newPointScaleEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<List<PointScaleInfo>> getPointScales() {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        List<PointScaleInfo> pointScales = new ArrayList<>();
        for (PointScaleEntity pointScaleEntity : pointScaleRepository.findAllByAppApiKey(app.getApiKey())) {
            pointScales.add(toPointScaleInfo(pointScaleEntity));
        }

        return ResponseEntity.ok(pointScales);
    }

    @Override
    public ResponseEntity<PointScale> getPointScale(@ApiParam(value = "",required = true) @PathVariable("id") Integer id) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        PointScaleEntity existingPointScaleEntity = pointScaleRepository.findByIdAndAppApiKey(id, app.getApiKey());
        if(existingPointScaleEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toPointScale(existingPointScaleEntity));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> removePointScale(@ApiParam(value = "",required = true) @PathVariable("id") Integer id) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        PointScaleEntity existingPointScaleEntity = pointScaleRepository.findByIdAndAppApiKey(id, app.getApiKey());
        if(existingPointScaleEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        pointsUserRepository.deleteAllByPointScaleId(existingPointScaleEntity.getId());
        pointsHistoryRepository.deleteAllByPointScaleId(existingPointScaleEntity.getId());
        stageRepository.deleteAllByPointScaleId(existingPointScaleEntity.getId());
        ruleRepository.deleteAllByPointScaleId(existingPointScaleEntity.getId());
        pointScaleRepository.delete(existingPointScaleEntity);
        return ResponseEntity.ok().build();
    }

    private PointScale toPointScale(PointScaleEntity pointScaleEntity){
        PointScale pointScale = new PointScale();
        List<Stage> stages = new ArrayList<>();
        for (StageEntity stageEntity : stageRepository.findAllByPointScaleId(pointScaleEntity.getId())) {
            stages.add(toStage(stageEntity));
        }
        pointScale.setStages(stages);
        pointScale.setName(pointScaleEntity.getName());

        return pointScale;
    }

    private PointScaleInfo toPointScaleInfo(PointScaleEntity pointScaleEntity) {
        PointScaleInfo pointScale = new PointScaleInfo();
        List<Stage> stages = new ArrayList<>();
        for (StageEntity stageEntity : stageRepository.findAllByPointScaleId(pointScaleEntity.getId())) {
            stages.add(toStage(stageEntity));
        }
        pointScale.setStages(stages);
        pointScale.setId((int) pointScaleEntity.getId());
        pointScale.setName(pointScaleEntity.getName());

        return pointScale;
    }

    private Stage toStage(StageEntity stageEntity){
        Stage stage = new Stage();
        stage.setPoints(stageEntity.getPoints());
        stage.setBadge(toBadgeName(stageEntity.getBadge()));

        return stage;
    }

    private BadgeName toBadgeName(BadgeEntity entity) {
        BadgeName badge = new BadgeName();
        badge.setName(entity.getName());

        return badge;
    }

    private PointScaleEntity toPointScaleEntity(PointScale pointScale) {
        PointScaleEntity pointScaleEntity = new PointScaleEntity();
        pointScaleEntity.setName(pointScale.getName());
        return pointScaleEntity;
    }

    private StageEntity toStageEntity(Stage stage, String apiKey){
        StageEntity stageEntity = new StageEntity();
        stageEntity.setPoints(stage.getPoints());
        stageEntity.setBadge(badgeRepository.findByNameAndAppApiKey(stage.getBadge().getName(), apiKey));

        return stageEntity;
    }
}

