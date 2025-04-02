package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.Exam;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ExamRepository {

    Collection<Exam> findAll();

    Optional<Exam> findByFachId(UUID id);

    void save(Exam test);

    Optional<UUID> findByStartTime(LocalDateTime startTime);
}
