package exambyte.infrastructure.service;

import exambyte.domain.model.user.Professor;
import exambyte.domain.repository.ProfessorRepository;
import exambyte.application.exception.NotFoundException;
import exambyte.domain.service.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository repository;

    public ProfessorServiceImpl(ProfessorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Professor getProfessor(UUID id) {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void saveProfessor(String name) {
        Professor professor = new Professor.ProfessorBuilder()
                .name(name)
                .build();
        repository.save(professor);
    }

    @Override
    public Optional<Professor> getProfessorByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Optional<UUID> getProfessorIdByName(String name) {
        Optional<Professor> professor = repository.findByName(name);
        return professor.map(Professor::id);
    }
}
