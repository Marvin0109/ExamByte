package exambyte.application.service.usecase;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ScoringServiceImpl implements ScoringService {

    private final ReviewService reviewService;

    public ScoringServiceImpl(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public double berechneErreichtePunkte(List<AntwortDTO> antworten, Map<UUID, FrageDTO> fragen) {
        return antworten.stream()
                .mapToDouble(a -> {
                    FrageDTO f = fragen.get(a.frageFachId());
                    if (f == null) return 0;
                    Review review = reviewService.getReviewByAntwortFachId(a.fachId());
                    return review != null ? review.getPunkte() : 0;
                })
                .sum();
    }
}
