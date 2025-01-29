package exambyte.service;

import exambyte.persistence.entities.JDBC.StudentEntityJDBC;

import java.util.Optional;

public interface StudentRepository {

    Optional<StudentEntityJDBC> findById(Long id);

    void save(StudentEntityJDBC student);
}
