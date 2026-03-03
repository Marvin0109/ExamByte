package exambyte.domain.repository;

import exambyte.domain.model.aggregate.user.Student;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository {

    Optional<Student> findByName(String name);

    Optional<Student> findById(UUID id);

    Optional<UUID> findIdByName(String name);

    void save(Student student);
}
