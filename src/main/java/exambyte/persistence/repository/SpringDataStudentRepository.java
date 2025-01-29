package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface SpringDataStudentRepository{

    Optional<StudentEntityJDBC> findById(Long id);

    StudentEntityJDBC save(StudentEntityJDBC student);
}
