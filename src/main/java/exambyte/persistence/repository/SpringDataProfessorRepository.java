package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataProfessorRepository extends CrudRepository<ProfessorEntityJDBC, Long> {

    Optional<ProfessorEntityJDBC> findByFachId(UUID id);

    ProfessorEntityJDBC save(ProfessorEntityJDBC professor);
}
