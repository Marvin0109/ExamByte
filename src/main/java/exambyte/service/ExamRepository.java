package exambyte.service;

import exambyte.persistence.entities.JDBC.ExamEntityJDBC;

import java.util.Optional;
import java.util.UUID;

public interface ExamRepository {

    Optional<ExamEntityJDBC> findByFachId(UUID id);

    void save(ExamEntityJDBC test);
}
