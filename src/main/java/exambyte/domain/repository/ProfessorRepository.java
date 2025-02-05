package exambyte.domain.repository;

import exambyte.domain.aggregate.user.Professor;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository {

    Optional<Professor> findByName(String name);

    Optional<Professor> findByFachId(UUID fachId);

    UUID findFachIdByName(String name);

    void save(Professor professor);
}
