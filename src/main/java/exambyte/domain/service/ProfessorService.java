package exambyte.domain.service;

import exambyte.domain.aggregate.user.Professor;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorService {

    Professor getProfessor(UUID fachId);

    void saveProfessor(String name);

    Optional<Professor> getProfessorByName(String name);

    UUID getProfessorFachId(String name);
}
