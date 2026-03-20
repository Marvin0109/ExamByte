package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.ReviewEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewDAO extends CrudRepository<ReviewEntity, UUID> {

    Optional<ReviewEntity> findByAnswerId(UUID id);
}
