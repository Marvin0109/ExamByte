package exambyte.service;

import exambyte.persistence.entities.JDBC.StudentEntityJDBC;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository {

    Optional<StudentEntityJDBC> findByFachId(UUID id);

    void save(StudentEntityJDBC student);
}
