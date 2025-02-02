package exambyte.service.repository.api;

import exambyte.persistence.entities.ExamEntity;

import java.util.Optional;
import java.util.UUID;

public interface ExamRepository {

    Optional<ExamEntity> findByFachId(UUID id);

    void save(ExamEntity test);
}
