package exambyte.service.interfaces;

import exambyte.domain.aggregate.user.Professor;

import java.util.UUID;

public interface ProfessorService {

    Professor getProfessor(UUID fachId);

    void saveProfessor(Professor professor);

    Professor getProfessorByName(String name);

    UUID getProfessorFachId(String name);
}
