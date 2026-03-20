package exambyte.application.service.query;

import exambyte.application.dto.ReviewDTO;

import java.util.UUID;

public interface ReviewQueryService {

    ReviewDTO getReviewByAnswerId(UUID answerId);

    boolean answerHasReview(UUID answerId);

    void createReview(String bewertung, double punkte, UUID answerId, UUID reviewerId);

    UUID getReviewIdByAnswerId(UUID answerId);

    void deleteReview(UUID id);
}
