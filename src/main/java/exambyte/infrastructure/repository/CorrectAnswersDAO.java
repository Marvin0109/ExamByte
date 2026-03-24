package exambyte.infrastructure.repository;

import exambyte.infrastructure.entity.CorrectAnswersEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CorrectAnswersDAO extends CrudRepository<CorrectAnswersEntity, UUID> {

    Optional<CorrectAnswersEntity> findByQuestionId(UUID questionId);
}
