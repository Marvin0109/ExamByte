package exambyte.application.service.query;

import exambyte.application.dto.ReviewDTO;

import java.util.UUID;

public interface ReviewQueryService {

    ReviewDTO getReviewByAntwortId(UUID antwortId);

    boolean antwortHasReview(UUID antwortId);

    void createReview(String bewertung, double punkte, UUID antwortId, UUID reviewerId);

    UUID getReviewIdByAntwortId(UUID antwortId);

    void deleteReview(UUID id);
}
