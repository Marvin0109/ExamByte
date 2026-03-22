package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.QuestionEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.UUID;

public interface QuestionDAO extends CrudRepository<QuestionEntity, UUID> {

    Collection<QuestionEntity> findByExamId(UUID examId);

    @NotNull Collection<QuestionEntity> findAll();
}