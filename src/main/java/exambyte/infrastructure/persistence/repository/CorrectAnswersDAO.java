package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.CorrectAnswersEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CorrectAnswersDAO extends CrudRepository<CorrectAnswersEntity, UUID> {

    Optional<CorrectAnswersEntity> findByFrageId(UUID frageId);
}
