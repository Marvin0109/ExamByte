package exambyte.application.interfaces;

import exambyte.domain.aggregate.exam.Review;

import java.util.UUID;

public interface ReviewService {

    void addReview(Review review);

    Review getReviewByAntwortFachId(UUID antwortFachId);
}
