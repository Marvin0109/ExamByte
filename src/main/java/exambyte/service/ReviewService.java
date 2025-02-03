package exambyte.service;

import exambyte.domain.aggregate.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public UUID addReview(Review review) {
        reviewRepository.save(review);
        return review.getFachId();
    }

    public Review getReviewByAntwortFachId(UUID antwortFachId) {
        return reviewRepository.findByAntwortFachId(antwortFachId);
    }
}
