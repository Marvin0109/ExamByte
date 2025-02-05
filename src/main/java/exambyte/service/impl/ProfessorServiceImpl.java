package exambyte.service.impl;

import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.repository.ProfessorRepository;
import exambyte.service.NichtVorhandenException;
import exambyte.service.interfaces.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository) {
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
        return professorRepository.findFachIdByName(name);
    }
}
