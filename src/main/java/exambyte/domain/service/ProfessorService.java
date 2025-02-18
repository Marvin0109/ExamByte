package exambyte.domain.service;

import exambyte.domain.model.aggregate.user.Professor;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorService {

    Professor getProfessor(UUID fachId);

    void saveProfessor(String name);

    Optional<Professor> getProfessorByName(String name);

    Optional<UUID> getProfessorFachId(String name);
}
