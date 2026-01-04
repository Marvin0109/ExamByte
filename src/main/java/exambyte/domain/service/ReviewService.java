package exambyte.domain.service;

import exambyte.domain.model.aggregate.exam.Review;

import java.util.UUID;

public interface ReviewService {

    void addReview(Review review);

    Review getReviewByAntwortFachId(UUID antwortFachId);

    void deleteAll();

    void deleteReview(UUID id);
}
