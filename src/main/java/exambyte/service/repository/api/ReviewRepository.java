package exambyte.service.repository.api;

import exambyte.persistence.entities.ReviewEntity;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {

    Optional<ReviewEntity> findByFachId(UUID fachId);

    void save(ReviewEntity review);
}
