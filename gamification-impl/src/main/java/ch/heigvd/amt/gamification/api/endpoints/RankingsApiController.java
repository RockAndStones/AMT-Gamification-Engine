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
import javax.validation.constraints.Min;
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
    public ResponseEntity<PaginatedPointsRankings> getRankingsByTotalPoints(@Min(0) @Valid Integer page, @Min(0) @Valid Integer pageSize) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<PointsRanking> userRankings = userRepository.userRankingsByTotalPoints(app, pageable);

        PaginatedPointsRankings p = new PaginatedPointsRankings();
        p.data(userRankings.getContent());
        p.setPagination(getPagination(userRankings));

        return ResponseEntity.ok(p);
    }

    @Override
    public ResponseEntity<PaginatedPointsRankings> getRankingsByEventTypePoints(String eventType, @Min(0) @Valid Integer page, @Min(0) @Valid Integer pageSize) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<PointsRanking> userRankings = userRepository.userRankingsByTotalPoints(app, eventType, pageable);

        PaginatedPointsRankings p = new PaginatedPointsRankings();
        p.data(userRankings.getContent());
        p.setPagination(getPagination(userRankings));

        return ResponseEntity.ok(p);
    }

    @Override
    public ResponseEntity<PaginatedPointsRankings> getRankingsByPointScalesPoints(Long id, @Min(0) @Valid Integer page, @Min(0) @Valid Integer pageSize) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");
        PointScaleEntity pointScale = pointScaleRepository.findByIdAndAppApiKey(id, app.getApiKey());
        if (pointScale == null)
            return ResponseEntity.notFound().build();

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<PointsRanking> userRankings = userRepository.userRankingsByTotalPoints(app, pointScale, pageable);

        PaginatedPointsRankings p = new PaginatedPointsRankings();
        p.data(userRankings.getContent());
        p.setPagination(getPagination(userRankings));

        return ResponseEntity.ok(p);
    }

    @Override
    public ResponseEntity<PaginatedBadgesRankings> getRankingsByTotalBadges(@Min(0) @Valid Integer page, @Min(0) @Valid Integer pageSize) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<BadgesRanking> userRankings = userRepository.userRankingsByBadges(app, pageable);

        PaginatedBadgesRankings p = new PaginatedBadgesRankings();
        p.data(userRankings.getContent());
        p.setPagination(getPagination(userRankings));

        return ResponseEntity.ok(p);
    }

    public Pagination getPagination(Page<?> page) {
        Pagination pagination = new Pagination();
        pagination.setNumberOfItems(page.getTotalElements());
        pagination.setPage(page.getNumber());

        if (page.hasPrevious())
            pagination.setPrevious(String.format("%s?page=%d&pageSize=%d",
                    request.getRequestURI(),
                    page.getNumber() - 1,
                    page.getTotalElements()));

        if (page.hasNext())
            pagination.setNext(String.format("%s?page=%d&pageSize=%d",
                    request.getRequestURI(),
                    page.getNumber() + 1,
                    page.getTotalElements()));

        return pagination;
    }
}
