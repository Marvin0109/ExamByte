package exambyte.persistence.repository;

import exambyte.persistence.entities.ExamEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataExamRepository extends CrudRepository<ExamEntity, Long> {

    Optional<ExamEntity> findByFachId(UUID id);

    ExamEntity save(ExamEntity test);
}
