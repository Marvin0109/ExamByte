package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataStudentRepository extends CrudRepository<StudentEntityJDBC, Long> {

    Optional<StudentEntityJDBC> findByFachId(UUID id);

    StudentEntityJDBC save(StudentEntityJDBC student);
}
