package exambyte.application.service.query;

import exambyte.application.dto.ProfessorDTO;
import exambyte.application.exception.NotFoundException;
import exambyte.application.mapper.ProfessorDTOMapper;
import exambyte.domain.model.user.Professor;
import exambyte.domain.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository repository;
    private final ProfessorDTOMapper mapper;

    public ProfessorServiceImpl(ProfessorRepository repository, ProfessorDTOMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<UUID> getProfIdByName(String name) {
        return getProfessorIdByName(name);
    }

    @Override
    public ProfessorDTO getProfessorById(UUID id) {
        return mapper.toDTO(getProfessor(id));
    }

    @Override
    public void saveProfessor(String name) {
        Professor professor = new Professor.ProfessorBuilder()
                .name(name)
                .build();
        repository.save(professor);
    }

    @Override
    public Optional<ProfessorDTO> getProfessorByName(String name) {
        Optional<Professor> professor = getProfByName(name);
        return professor.map(mapper::toDTO);
    }

    private Optional<UUID> getProfessorIdByName(String name) {
        Optional<Professor> professor = repository.findByName(name);
        return professor.map(Professor::id);
    }

    private Professor getProfessor(UUID id) {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    private Optional<Professor> getProfByName(String name) {
        return repository.findByName(name);
    }
}
