package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.RankingsApi;
import ch.heigvd.amt.gamification.api.model.PaginatedUserRanking;
import ch.heigvd.amt.gamification.api.model.Pagination;
import ch.heigvd.amt.gamification.api.model.PointScale;
import ch.heigvd.amt.gamification.api.model.UserRanking;
import ch.heigvd.amt.gamification.dto.UserRankingDTO;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.PointScaleEntity;
import ch.heigvd.amt.gamification.repositories.PointScaleRepository;
import ch.heigvd.amt.gamification.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class RankingsApiController implements RankingsApi {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PointScaleRepository pointScaleRepository;

    @Autowired
    HttpServletRequest request;

    @Override
    public ResponseEntity<PaginatedUserRanking> getRankingsByTotalPoints(@Valid Integer page, @Valid Integer pageSize) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        // todo implement pagination
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<UserRankingDTO> userRankings = userRepository.userRankingsByTotalPoints(app, pageable);

        PaginatedUserRanking p = new PaginatedUserRanking();
        p.data(userRankings.stream()
                .map(this::userRankingFromDTO)
                .collect(Collectors.toList())
        );

        Pagination pagination = new Pagination();
        pagination.setNumberOfItems(userRankings.getTotalElements());
        pagination.setPage(userRankings.getNumber());

        if (userRankings.hasPrevious())
            pagination.setPrevious(String.format("%s?page=%d&pageSize=%d",
                    request.getRequestURI(),
                    userRankings.getNumber() - 1,
                    userRankings.getTotalElements()));

        if (userRankings.hasNext())
            pagination.setNext(String.format("%s?page=%d&pageSize=%d",
                    request.getRequestURI(),
                    userRankings.getNumber() + 1,
                    userRankings.getTotalElements()));

        p.setPagination(pagination);

        return ResponseEntity.ok(p);
    }

    @Override
    public ResponseEntity<PaginatedUserRanking> getRankingsByEventTypePoints(String eventType) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        List<UserRankingDTO> userRankings = userRepository.userRankingsByTotalPoints(app, eventType);
        PaginatedUserRanking p = new PaginatedUserRanking();

        p.data(userRankings.stream()
                .map(this::userRankingFromDTO)
                .collect(Collectors.toList())
        );

        return ResponseEntity.ok(p);
    }

    @Override
    public ResponseEntity<PaginatedUserRanking> getRankingsByPointScalesPoints(Long id) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");
        PointScaleEntity pointScale = pointScaleRepository.findByIdAndAppApiKey(id, app.getApiKey());
        if (pointScale == null)
            return ResponseEntity.notFound().build();

        List<UserRankingDTO> userRankings = userRepository.userRankingsByTotalPoints(app, pointScale);
        PaginatedUserRanking p = new PaginatedUserRanking();

        p.data(userRankings.stream()
                .map(this::userRankingFromDTO)
                .collect(Collectors.toList())
        );

        return ResponseEntity.ok(p);
    }

    @Override
    public ResponseEntity<PaginatedUserRanking> getRankingsByTotalBadges() {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        List<UserRankingDTO> userRankings = userRepository.userRankingsByBadges(app);
        PaginatedUserRanking p = new PaginatedUserRanking();

        p.data(userRankings.stream()
                .map(this::userRankingFromDTO)
                .collect(Collectors.toList())
        );

        return ResponseEntity.ok(p);
    }

    private UserRanking userRankingFromDTO(UserRankingDTO userRankingDTO) {
        UserRanking ur = new UserRanking();
        ur.setUserId(userRankingDTO.getUserId());
        ur.setPoints(userRankingDTO.getPoints());

        return ur;
    }
}
