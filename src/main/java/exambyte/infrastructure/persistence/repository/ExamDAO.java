package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.ExamEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ExamDAO extends CrudRepository<ExamEntity, Long> {

    Collection<ExamEntity> findAll();

    Optional<ExamEntity> findByFachId(UUID id);

    ExamEntity save(ExamEntity test);
}
