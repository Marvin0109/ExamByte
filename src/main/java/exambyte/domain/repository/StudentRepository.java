package exambyte.domain.repository;

import exambyte.domain.aggregate.user.Student;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository {

    Optional<Student> findByName(String name);

    Optional<Student> findByFachId(UUID fachId);

    void save(Student student);
}
