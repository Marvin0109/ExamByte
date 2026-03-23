package exambyte.infrastructure.service;

import exambyte.domain.model.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;

    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addReview(Review review) {
        repository.save(review);
    }

    @Override
    public Review getReviewByAnswerId(UUID answerId) {
        return repository.findByAnswerId(answerId);
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
