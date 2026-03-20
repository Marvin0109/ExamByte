package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.Review;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {

    Review findByAnswerId(UUID id);

    Optional<Review> findById(UUID id);

    void save(Review review);

    void deleteAll();

    void deleteReview(UUID id);
}
