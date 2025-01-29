package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface SpringDataProfessorRepository{

    Optional<ProfessorEntityJDBC> findById(Long id);

    ProfessorEntityJDBC save(ProfessorEntityJDBC professor);
}
