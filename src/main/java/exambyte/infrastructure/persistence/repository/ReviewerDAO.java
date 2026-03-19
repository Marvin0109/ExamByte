package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.ReviewerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewerDAO extends CrudRepository<ReviewerEntity, UUID> {
    Optional<ReviewerEntity> findByName(String name);
}
