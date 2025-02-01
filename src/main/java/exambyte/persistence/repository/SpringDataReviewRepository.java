package exambyte.persistence.repository;

import exambyte.persistence.entities.ReviewEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataReviewRepository extends CrudRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findByFachId(UUID fachId);

    ReviewEntity save(ReviewEntity review);
}
