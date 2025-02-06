package exambyte.infrastructure.service;

import exambyte.domain.aggregate.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void addReview(Review review) {
        reviewRepository.save(review);
    }

    @Override
    public Review getReviewByAntwortFachId(UUID antwortFachId) {
        return reviewRepository.findByAntwortFachId(antwortFachId);

    }
}
