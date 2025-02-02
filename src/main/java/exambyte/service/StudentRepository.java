package exambyte.service;

import exambyte.persistence.entities.StudentEntity;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository {

    Optional<StudentEntity> findByFachId(UUID fachId);

    void save(StudentEntity student);
}
