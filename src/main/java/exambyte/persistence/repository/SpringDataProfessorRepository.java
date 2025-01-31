package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataProfessorRepository extends CrudRepository<ProfessorEntityJDBC, Long> {

    Optional<ProfessorEntityJDBC> findByFachId(UUID fachId);

    ProfessorEntityJDBC save(ProfessorEntityJDBC professor);
}
