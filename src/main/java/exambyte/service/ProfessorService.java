package exambyte.service;

import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public Professor getProfessor(UUID fachId) {
        return professorRepository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    public void saveProfessor(Professor professor) {
        professorRepository.save(professor);
    }

    public Professor getProfessorByName(String name) {
        return professorRepository.findByName(name)
                .orElseThrow(NichtVorhandenException::new);
    }

    public UUID getProfessorFachId(String name) {
        return professorRepository.getFachId(name);
    }
}
