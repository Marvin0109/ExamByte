package exambyte.application.service.query;

import exambyte.application.dto.ReviewDTO;

import java.util.UUID;

public interface ReviewService {

    ReviewDTO getReviewByAnswerId(UUID answerId);

    boolean answerHasReview(UUID answerId);

    void createReview(String text, double points, UUID answerId, UUID reviewerId);
}
