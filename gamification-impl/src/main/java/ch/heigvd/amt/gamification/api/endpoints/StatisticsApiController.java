package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.StatisticsApi;
import ch.heigvd.amt.gamification.api.model.PointsProgression;
import ch.heigvd.amt.gamification.api.model.PointsProgressionData;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.PointScaleEntity;
import ch.heigvd.amt.gamification.entities.PointsHistoryEntity;
import ch.heigvd.amt.gamification.entities.UserEntity;
import ch.heigvd.amt.gamification.repositories.PointScaleRepository;
import ch.heigvd.amt.gamification.repositories.PointsHistoryRepository;
import ch.heigvd.amt.gamification.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletRequest;
import java.util.stream.Collectors;

@Controller
public class StatisticsApiController implements StatisticsApi {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PointScaleRepository pointScaleRepository;

    @Autowired
    PointsHistoryRepository pointsHistoryRepository;

    @Autowired
    ServletRequest request;

    @Override
    public ResponseEntity<PointsProgression> getUserOverallProgression(String userAppId) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");
        UserEntity u = userRepository.findByUserAppIdAndAppApiKey(userAppId, app.getApiKey());
        if (u == null)
            return ResponseEntity.notFound().build();

        PointsProgression pointsProgression = new PointsProgression();
        pointsProgression.setUserAppId(userAppId);
        pointsProgression.setData(pointsHistoryRepository.findAllByUser(userAppId)
                .stream()
                .map(this::pointsHistoryEntityToData)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(pointsProgression);
    }

    @Override
    public ResponseEntity<PointsProgression> getUserProgressionByPointScale(String userAppId, Long pointScaleId) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");
        UserEntity u = userRepository.findByUserAppIdAndAppApiKey(userAppId, app.getApiKey());
        if (u == null)
            return ResponseEntity.notFound().build();

        PointScaleEntity p = pointScaleRepository.findByIdAndAppApiKey(pointScaleId, app.getApiKey());
        if (p == null)
            return ResponseEntity.notFound().build();

        PointsProgression pointsProgression = new PointsProgression();
        pointsProgression.setUserAppId(userAppId);
        pointsProgression.setData(pointsHistoryRepository.findAllByUserAndPointScale(userAppId, p)
                .stream()
                .map(this::pointsHistoryEntityToData)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(pointsProgression);
    }

    private PointsProgressionData pointsHistoryEntityToData(PointsHistoryEntity pointsHistory) {
        PointsProgressionData p = new PointsProgressionData();
        p.setEventType(pointsHistory.getEvent().getEventType());
        p.setPoints(pointsHistory.getNbPoints());
        p.setTimestamp(pointsHistory.getEvent().getTimestamp());

        return p;
    }
}
