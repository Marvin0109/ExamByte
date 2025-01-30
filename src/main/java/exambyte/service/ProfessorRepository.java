package exambyte.service;

import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository {

    Optional<ProfessorEntityJDBC> findByFachId(UUID id);

    void save(ProfessorEntityJDBC professor);
}
