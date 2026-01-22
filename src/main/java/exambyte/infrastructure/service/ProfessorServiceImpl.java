package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.repository.ProfessorRepository;
import exambyte.infrastructure.exceptions.NichtVorhandenException;
import exambyte.domain.service.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository repository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository) {
        this.repository = professorRepository;
    }

    @Override
    public Professor getProfessor(UUID fachId) {
        return repository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    @Override
    public void saveProfessor(String name) {
        Professor professor = new Professor.ProfessorBuilder()
                .id(null)
                .fachId(null)
                .name(name)
                .build();
        repository.save(professor);
    }

    @Override
    public Optional<Professor> getProfessorByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Optional<UUID> getProfessorFachIdByName(String name) {
        Optional<Professor> professor = repository.findByName(name);
        return professor.map(Professor::uuid);
    }
}
