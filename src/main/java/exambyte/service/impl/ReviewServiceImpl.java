package exambyte.service.impl;

import exambyte.domain.aggregate.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import exambyte.service.interfaces.ReviewService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
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
