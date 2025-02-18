package exambyte.infrastructure.persistence.repository;

import exambyte.infrastructure.persistence.entities.StudentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataStudentRepository extends CrudRepository<StudentEntity, Long> {

    Optional<StudentEntity> findByName(String name);

    Optional<StudentEntity> findByFachId(UUID fachId);

    UUID findFachIdByName(String name);

    StudentEntity save(StudentEntity student);
}
