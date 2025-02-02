package exambyte.domain.repository;

import exambyte.domain.aggregate.exam.Review;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {

    Optional<Review> findByFachId(UUID fachId);

    void save(Review review);
}
