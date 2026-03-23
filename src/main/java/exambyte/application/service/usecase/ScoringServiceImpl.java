package exambyte.application.service.usecase;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.query.ReviewService;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ScoringServiceImpl implements ScoringService {

    private final ReviewService reviewService;
    private final Clock clock;

    public ScoringServiceImpl(ReviewService reviewService, Clock clock) {
        this.reviewService = reviewService;
        this.clock = clock;
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock)
                .truncatedTo(ChronoUnit.MINUTES);
    }

    @Override
    public double accumulatedPoints(List<AnswerDTO> answers, Map<UUID, QuestionDTO> questionMap, LocalDateTime result) {
        return answers.stream()
                .mapToDouble(a -> {
                    QuestionDTO q = questionMap.get(a.questionId());
                    if (q == null) return 0;
                    ReviewDTO review = reviewService.getReviewByAnswerId(a.id());

                    UUID automaticReviewer = UUID.fromString("11111111-1111-1111-1111-111111111111");

                    LocalDateTime currentTime = now();
                    if (review == null) {
                        return 0;
                    }

                    boolean isAutomaticReview = review.reviewerId().equals(automaticReviewer);

                    boolean resultTimeReached = !currentTime.isBefore(result);

                    if (isAutomaticReview || resultTimeReached) {
                        return review.points();
                    }

                    return 0;
                })
                .sum();
    }
}
