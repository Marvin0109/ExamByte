package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.Exam;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ExamRepository {

    Collection<Exam> findAll();

    Optional<Exam> findById(UUID id);

    void save(Exam exam);

    Optional<UUID> findByStartTime(LocalDateTime start);

    void deleteById(UUID id);

    void deleteAll();
}
