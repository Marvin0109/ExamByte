package exambyte.service;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;

import java.util.Optional;

public interface ProfessorRepository {

    Optional<ProfessorEntityJDBC> findById(Long id);

    void save(ProfessorEntityJDBC professor);
}
