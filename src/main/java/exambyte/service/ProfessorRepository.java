package exambyte.service;

import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository {

    Optional<ProfessorEntityJDBC> findByFachId(UUID fachId);

    void save(ProfessorEntityJDBC professor);
}
