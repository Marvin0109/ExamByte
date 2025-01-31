package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.ExamEntityJDBC;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataExamRepository extends CrudRepository<ExamEntityJDBC, Long> {

    Optional<ExamEntityJDBC> findByFachId(UUID id);

    ExamEntityJDBC save(ExamEntityJDBC test);
}
