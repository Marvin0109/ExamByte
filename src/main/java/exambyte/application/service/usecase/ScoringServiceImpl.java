package exambyte.application.service.usecase;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.query.ReviewQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ScoringServiceImpl implements ScoringService {

    private final ReviewQueryService reviewQueryService;

    public ScoringServiceImpl(ReviewQueryService reviewQueryService) {
        this.reviewQueryService = reviewQueryService;
    }

    @Override
    public double berechneErreichtePunkte(List<AntwortDTO> antworten, Map<UUID, FrageDTO> fragen) {
        return antworten.stream()
                .mapToDouble(a -> {
                    FrageDTO f = fragen.get(a.frageFachId());
                    if (f == null) return 0;
                    ReviewDTO review = reviewQueryService.getReviewByAntwortId(a.fachId());
                    return review != null ? review.punkte() : 0;
                })
                .sum();
    }
}
