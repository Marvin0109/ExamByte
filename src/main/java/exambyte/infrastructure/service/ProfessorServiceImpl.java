package exambyte.infrastructure.service;

import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.repository.ProfessorRepository;
import exambyte.infrastructure.NichtVorhandenException;
import exambyte.domain.service.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public Professor getProfessor(UUID fachId) {
        return professorRepository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    @Override
    public void saveProfessor(String name) {
        Professor professor = new Professor.ProfessorBuilder()
                .id(null)
                .fachId(null)
                .name(name)
                .build();
        professorRepository.save(professor);
    }

    @Override
    public Optional<Professor> getProfessorByName(String name) {
        return professorRepository.findByName(name);
    }

    @Override
    public UUID getProfessorFachId(String name) {
        return professorRepository.findFachIdByName(name);
    }
}
