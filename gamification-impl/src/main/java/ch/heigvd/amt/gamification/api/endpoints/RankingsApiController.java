package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.RankingsApi;
import ch.heigvd.amt.gamification.api.model.PaginatedUserRanking;
import ch.heigvd.amt.gamification.api.model.UserRanking;
import ch.heigvd.amt.gamification.dto.UserRankingDTO;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RankingsApiController implements RankingsApi {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ServletRequest request;

    @Override
    public ResponseEntity<PaginatedUserRanking> getRankingsByTotalPoints() {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        List<UserRankingDTO> userRankings = userRepository.userRankingsByTotalPoints(app);
        PaginatedUserRanking p = new PaginatedUserRanking();

        p.data(userRankings.stream()
                .map(this::userRankingFromDTO)
                .collect(Collectors.toList())
        );

        return ResponseEntity.ok(p);
    }

    @Override
    public ResponseEntity<PaginatedUserRanking> getRankingsByPointScalesPoints(Integer id) {
        return null;
    }

    private UserRanking userRankingFromDTO(UserRankingDTO userRankingDTO) {
        UserRanking ur = new UserRanking();
        ur.setUserId(userRankingDTO.getUserId());
        ur.setPoints(userRankingDTO.getPoints());

        return ur;
    }
}
