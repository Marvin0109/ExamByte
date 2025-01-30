package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SpringDataStudentRepository extends CrudRepository<StudentEntityJDBC, Long> {

    Optional<StudentEntityJDBC> findById(Long id);

    StudentEntityJDBC save(StudentEntityJDBC student);
}
