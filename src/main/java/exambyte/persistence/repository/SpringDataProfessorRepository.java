package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface SpringDataProfessorRepository extends CrudRepository<ProfessorEntityJDBC, Long> {

    Optional<ProfessorEntityJDBC> findById(Long id);

    ProfessorEntityJDBC save(ProfessorEntityJDBC professor);
}
