package exambyte.service.interfaces;

import exambyte.domain.aggregate.exam.Review;

import java.util.UUID;

public interface ReviewService {

    UUID addReview(Review review);

    Review getReviewByAntwortFachId(UUID antwortFachId);
}
