package exambyte.application.interfaces;

import exambyte.domain.aggregate.user.Professor;

import java.util.UUID;

public interface ProfessorService {

    Professor getProfessor(UUID fachId);

    void saveProfessor(String name);

    Professor getProfessorByName(String name);

    UUID getProfessorFachId(String name);
}
