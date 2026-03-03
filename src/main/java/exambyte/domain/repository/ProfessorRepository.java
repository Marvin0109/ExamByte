package exambyte.domain.repository;

import exambyte.domain.model.aggregate.user.Professor;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository {

    Optional<Professor> findByName(String name);

    Optional<Professor> findById(UUID id);

    void save(Professor professor);

    void deleteAll();
}
