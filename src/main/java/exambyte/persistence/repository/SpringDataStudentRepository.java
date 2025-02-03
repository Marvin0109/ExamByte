package exambyte.persistence.repository;

import exambyte.persistence.entities.StudentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataStudentRepository extends CrudRepository<StudentEntity, Long> {

    Optional<StudentEntity> findByName(String name);

    Optional<StudentEntity> findByFachId(UUID fachId);

    StudentEntity save(StudentEntity student);
}
