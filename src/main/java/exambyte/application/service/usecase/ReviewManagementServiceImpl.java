package exambyte.application.service.usecase;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.query.ReviewQueryService;
import exambyte.application.service.query.AntwortQueryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewManagementServiceImpl implements ReviewManagementService {

    private final ReviewQueryService reviewQueryService;
    private final AntwortQueryService antwortQueryService;

    public ReviewManagementServiceImpl(ReviewQueryService reviewQueryService,
                                       AntwortQueryService antwortQueryService) {
        this.reviewQueryService = reviewQueryService;
        this.antwortQueryService = antwortQueryService;
    }

    @Override
    public double getReviewCoverage(UUID examId) {
        List<AntwortDTO> antworten = antwortQueryService.getFreeResponseAntwortenForExam(examId);

        List<ReviewDTO> reviewsTotal = new ArrayList<>();

        for (AntwortDTO antwortDTO : antworten) {
            ReviewDTO reviewDTO = reviewQueryService.getReviewByAntwortId(antwortDTO.id());
            if (reviewDTO != null) {
                reviewsTotal.add(reviewDTO);
            }
        }

        double coverage = antworten.isEmpty()
                ? 0.0
                : (double) reviewsTotal.size() / antworten.size() * 100;

        return Math.round(coverage * 100.0) / 100.0;
    }

    @Override
    public boolean submitHasReview(UUID examId, UUID studentId) {
        List<AntwortDTO> antworten = antwortQueryService.getFreeResponseAntwortenForExam(examId);

        List<UUID> studentAntwortList = antworten.stream()
                .filter(a -> a.studentId().equals(studentId))
                .map(AntwortDTO::id)
                .toList();

        for (UUID uuid : studentAntwortList) {
            if (reviewQueryService.getReviewByAntwortId(uuid) == null) {
                return false;
            }
        }

        return true;
    }
}
