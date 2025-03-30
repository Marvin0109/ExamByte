package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.ReviewEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewDAO extends CrudRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findByAntwortFachId(UUID id);

    Optional<ReviewEntity> findByFachId(UUID fachId);

    ReviewEntity save(ReviewEntity review);
}
