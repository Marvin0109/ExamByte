package exambyte.domain.repository;

import exambyte.domain.aggregate.user.Professor;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository {

    Optional<Professor> findByFachId(UUID fachId);

    void save(Professor professor);
}
