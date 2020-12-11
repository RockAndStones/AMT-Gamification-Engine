package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.RankingsApi;
import ch.heigvd.amt.gamification.api.model.*;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.PointScaleEntity;
import ch.heigvd.amt.gamification.repositories.PointScaleRepository;
import ch.heigvd.amt.gamification.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class RankingsApiController implements RankingsApi {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PointScaleRepository pointScaleRepository;

    @Autowired
    HttpServletRequest request;

    @Override
    public ResponseEntity<PaginatedPointsRankings> getRankingsByTotalPoints(@Valid Integer page, @Valid Integer pageSize) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<PointsRanking> userRankings = userRepository.userRankingsByTotalPoints(app, pageable);

        PaginatedPointsRankings p = new PaginatedPointsRankings();
        p.data(userRankings.getContent());

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
    public ResponseEntity<PaginatedPointsRankings> getRankingsByEventTypePoints(String eventType) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        List<PointsRanking> userRankings = userRepository.userRankingsByTotalPoints(app, eventType);
        PaginatedPointsRankings p = new PaginatedPointsRankings();
        p.data(userRankings);

        return ResponseEntity.ok(p);
    }

    @Override
    public ResponseEntity<PaginatedPointsRankings> getRankingsByPointScalesPoints(Long id) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");
        PointScaleEntity pointScale = pointScaleRepository.findByIdAndAppApiKey(id, app.getApiKey());
        if (pointScale == null)
            return ResponseEntity.notFound().build();

        List<PointsRanking> userRankings = userRepository.userRankingsByTotalPoints(app, pointScale);
        PaginatedPointsRankings p = new PaginatedPointsRankings();
        p.data(userRankings);

        return ResponseEntity.ok(p);
    }

    @Override
    public ResponseEntity<PaginatedBadgesRankings> getRankingsByTotalBadges() {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        List<BadgesRanking> userRankings = userRepository.userRankingsByBadges(app);
        PaginatedBadgesRankings p = new PaginatedBadgesRankings();
        p.data(userRankings);

        return ResponseEntity.ok(p);
    }
}
