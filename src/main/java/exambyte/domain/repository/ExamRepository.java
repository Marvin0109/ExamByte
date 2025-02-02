package exambyte.domain.repository;

import exambyte.domain.aggregate.exam.Exam;

import java.util.Optional;
import java.util.UUID;

public interface ExamRepository {

    Optional<Exam> findByFachId(UUID id);

    void save(Exam test);
}
