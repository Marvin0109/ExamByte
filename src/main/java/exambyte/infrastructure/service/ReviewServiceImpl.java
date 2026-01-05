package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.repository = reviewRepository;
    }

    @Override
    public void addReview(Review review) {
        repository.save(review);
    }

    @Override
    public Review getReviewByAntwortFachId(UUID antwortFachId) {
        return repository.findByAntwortFachId(antwortFachId);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteReview(UUID id) {
        repository.deleteReview(id);
    }
}
