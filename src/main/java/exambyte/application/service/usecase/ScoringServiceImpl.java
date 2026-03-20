package exambyte.application.service.usecase;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.service.ReviewService;
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
    public double berechneErreichtePunkte(List<AnswerDTO> answers, Map<UUID, FrageDTO> fragen, LocalDateTime result) {
        return answers.stream()
                .mapToDouble(a -> {
                    FrageDTO f = fragen.get(a.frageId());
                    if (f == null) return 0;
                    Review review = reviewService.getReviewByAnswerId(a.id());

                    UUID automaticReviewer = UUID.fromString("11111111-1111-1111-1111-111111111111");

                    LocalDateTime currentTime = now();
                    if (review == null) {
                        return 0;
                    }

                    boolean isAutomaticReview = review.getReviewerId().equals(automaticReviewer);

                    boolean resultTimeReached = !currentTime.isBefore(result);

                    if (isAutomaticReview || resultTimeReached) {
                        return review.getPunkte();
                    }

                    return 0;
                })
                .sum();
    }
}
