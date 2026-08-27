package exambyte.infrastructure.repository;

import exambyte.infrastructure.entity.QuestionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.UUID;

public interface QuestionDAO extends CrudRepository<QuestionEntity, UUID> {

    Collection<QuestionEntity> findByExamId(UUID examId);

    Collection<QuestionEntity> findAll();
}