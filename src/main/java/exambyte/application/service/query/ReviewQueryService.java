package exambyte.application.service.query;

import exambyte.application.dto.ReviewDTO;

import java.util.UUID;

public interface ReviewQueryService {

    ReviewDTO getReviewByAntwortId(UUID antwortId);

    boolean antwortHasReview(UUID antwortId);

    void createReview(String bewertung, int punkte, UUID antwortId, UUID korrektorId);

    UUID getReviewIdByAntwortId(UUID antwortId);

    void deleteReview(UUID id);
}
